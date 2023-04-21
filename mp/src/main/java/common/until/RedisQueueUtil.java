package common.until;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import mp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Redis
 */
@Slf4j
@Component
public class RedisQueueUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired(required=false)
    private Jackson2JsonRedisSerializer jackson2JsonRedisSerializer;

    static String PREFIX_GUIDE_QUEUE_ID ="K11";
    /**
     * 入队
     */
    public void rightPush(String queueId, User user) {
        queueId = PREFIX_GUIDE_QUEUE_ID +queueId;
        redisTemplate.opsForList().rightPush(queueId, user);
    }
    // 队首离队
    public User headLeaveQueue(String roomCode) {
        roomCode = PREFIX_GUIDE_QUEUE_ID +roomCode;
        AtomicReference<User> patientVoAtomicReference = new AtomicReference<>();
        User leftPop = (User) redisTemplate.opsForList().leftPop(roomCode);
        patientVoAtomicReference.set(leftPop);
        return patientVoAtomicReference.get();
    }
    /**
     * 删除队列
     */
    public void unlinkReisKey(String queueId) {
        queueId = PREFIX_GUIDE_QUEUE_ID +queueId;
        redisTemplate.unlink(queueId);
    }

    /**
     * @return Long长度
     */
    public Long getQueueLength(String roomCode) {
        roomCode = PREFIX_GUIDE_QUEUE_ID +roomCode;
        return redisTemplate.opsForList().size(roomCode);
    }

    // 获取队列数据
    public List<User> getQueueData(String roomCode) {

        roomCode = PREFIX_GUIDE_QUEUE_ID +roomCode;
        return redisTemplate.opsForList().range(roomCode, 0, -1);
    }

    // 获取队列数据
    public User getPatient(String queueId,String patientId) {
        queueId = PREFIX_GUIDE_QUEUE_ID +queueId;
        StringBuilder luaScript = new StringBuilder();
        luaScript.append( "\nlocal index = 0" );
        luaScript.append( "\nlocal valueTo ='' " );
        luaScript.append( "\nlocal key = KEYS[1]" );
        luaScript.append( "\nlocal paintsTo = redis.call('lrange', key,0,-1)");
        luaScript.append( "\nfor j = 1, #paintsTo do" );
        luaScript.append( "\nlocal obj ='' " );
        luaScript.append(deserialize("paintsTo[j]","obj"));
        luaScript.append( "\nif(obj['Id']==ARGV[1])" );
        luaScript.append( "\nthen index = j" );
        luaScript.append( "\nvalueTo = paintsTo[j]" );
        luaScript.append( "\nend" );
        luaScript.append( "\nend" );
        luaScript.append( "\nreturn valueTo" );
        log.info(luaScript.toString());
        RedisScript<String> redisScript = new DefaultRedisScript<String>(luaScript.toString(),
                String.class);
        List<String> keys =  new ArrayList<>();
        keys.add(queueId);
        String execute = stringRedisTemplate.execute(redisScript, keys,patientId);
        if(StringUtils.isEmpty(execute)){
            return null;
        }
        Object deserialize = jackson2JsonRedisSerializer.deserialize(execute.getBytes());
        User patientVO = (User) deserialize;
        return patientVO;
    }

    // 队尾添加数据
    public Boolean rightPushIfNotExist(String queueId, String patientId, User patientVO) {
        queueId = PREFIX_GUIDE_QUEUE_ID +queueId;
        StringBuilder luaScript = new StringBuilder();
        luaScript.append( "\nlocal index = 0" );
        luaScript.append( "\nlocal valueTo ='' " );
        luaScript.append( "\nlocal key = KEYS[1]" );
        luaScript.append( "\nlocal paintsTo = redis.call('lrange', key,0,-1)");
        luaScript.append( "\nfor j = 1, #paintsTo do" );
        luaScript.append( "\nlocal obj ='' " );
        luaScript.append(deserialize("paintsTo[j]","obj"));
        luaScript.append( "\nif(obj['Id']==ARGV[1])" );
        luaScript.append( "\nthen index = j" );
        luaScript.append( "\nvalueTo = paintsTo[j]" );
        luaScript.append( "\nend" );
        luaScript.append( "\nend" );
        luaScript.append( "\nif(index==0)" );
        luaScript.append( "\nthen redis.call('rpush',key,ARGV[2])" );
        luaScript.append( "\nend" );
        luaScript.append( "\nreturn true" );
        log.info(luaScript.toString());
        RedisScript<Boolean> redisScript = new DefaultRedisScript<Boolean>(luaScript.toString(),
                Boolean.class);
        List<String> keys = new ArrayList<>();
        keys.add(queueId);
        byte[] serialize = jackson2JsonRedisSerializer.serialize(patientVO);
        String value = null;
        try {
            value = new String(serialize,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Boolean execute = stringRedisTemplate.execute(redisScript, keys,patientId,value);
        return execute;
    }
    /**
     * 特定对象的反序列化
     * @param str
     * @return
     */
    public String  deserialize(String str,String springObj){
        StringBuilder luaScript = new StringBuilder();
        luaScript.append( "\n"+springObj+" = string.gsub("+str+",'%[\""+User.class.getName()+"\",','')" );
        luaScript.append( "\n"+springObj+"= string.gsub("+springObj+",'%]$','')" );
        luaScript.append( "\n"+springObj+" = string.gsub("+springObj+",'(\".-\"):(.-,)','[%1]=%2')" );
        luaScript.append( "\n"+springObj+" = string.gsub("+springObj+",',(\".-\"):(.-})',',[%1]=%2')" );
        luaScript.append( "\n"+springObj+" = string.gsub("+springObj+",'null','\"null\"')" );
        luaScript.append( "\n"+springObj+" = loadstring('return'.."+springObj+")()" );
        return luaScript.toString();
    }

    // 得到某人队列中的位置
    public List<Integer> getOnesPosition(String roomCode, User patientVo) {
        roomCode = PREFIX_GUIDE_QUEUE_ID +roomCode;
        List<User> queueData = getQueueData(roomCode);
        int myPositionBeforeNum = queueData.indexOf(patientVo);
        int myPosition = myPositionBeforeNum + 1;
        int size = queueData.size();
        // 当前排队res[0]人，您排在第res[1]位，前面还有res[2]位。
        List<Integer> result = new ArrayList<>();
        result.add(size);
        result.add(myPosition);
        result.add(myPositionBeforeNum);
        return result;
    }

    // 插队操作
    public void jumpAQueue(String roomCode, User jumpChecker, User targetChecker, Integer jumpType) {
        roomCode = PREFIX_GUIDE_QUEUE_ID +roomCode;
        // 在target前面插队
        if (jumpType.equals(1)) {
            redisTemplate.opsForList().leftPush(roomCode, targetChecker, jumpChecker);
        }
        // 在target后面插队
        if (jumpType.equals(2)) {
            redisTemplate.opsForList().rightPush(roomCode, targetChecker, jumpChecker);
        }
    }

    /**
     * 在指定坐标位置插入(替换)新值
     *
     * index不存在，报错（ERR index out of range）
     * key不存在，报错（ERR no such key）
     * 从左侧插入
     * @param roomCode
     * @return
     */
    public void set(String roomCode, Long index, User patientVo) {
        roomCode = PREFIX_GUIDE_QUEUE_ID +roomCode;
        redisTemplate.opsForList().set(roomCode,index,patientVo);
    }


    /**
     * 对某个位置id设置新值
     * @param roomCode
     * @param paintId
     * @return
     */
    public Boolean updateById(String roomCode, String paintId, User patientVo){
        roomCode = PREFIX_GUIDE_QUEUE_ID +roomCode;
        StringBuilder luaScript = new StringBuilder();
        luaScript.append( " local key = KEYS[1]" );
        luaScript.append( "\nlocal paints = redis.call('lrange', key,0,-1)" );
        luaScript.append( "\nlocal index = 0" );
        luaScript.append( "\nfor i = 1, #paints do" );
        luaScript.append( "\nif(string.find(paints[i],'\"Id\":\"'..ARGV[1]..'\",'))" );
        luaScript.append( "\nthen index = i" );
        luaScript.append( "\nend" );
        luaScript.append( "\nend" );
        luaScript.append( "\nif(index==0)" );
        luaScript.append( "\nthen return false" );
        luaScript.append( "\nend" );
        luaScript.append( "\nredis.call('lset',key,index-1,ARGV[2])" );
        luaScript.append( "\nreturn true" );
        log.info(luaScript.toString());
        RedisScript<Boolean> redisScript = new DefaultRedisScript<>(luaScript.toString(),
                Boolean.class);

        List<String> keys = new ArrayList<>();
        keys.add(roomCode);
        byte[] serialize = jackson2JsonRedisSerializer.serialize(patientVo);
        try {
            String value = new String(serialize,"utf-8");
            Boolean execute = stringRedisTemplate.execute(redisScript, keys, paintId, value);
            return execute;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }


}
