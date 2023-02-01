package mp.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mp.until.XunFeiTtsUtil;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Base64;

@RestController
@Api(tags = "文字转语音")
@RequestMapping("web/xunfei")
public class TextToAudioController {

    private static final Logger log = LoggerFactory.getLogger(TextToAudioController.class);

    @ApiOperation(value = "文字转语音", notes = "文字转语音")
    @PostMapping(value = "text_to_audio")
    public void textToAudio(String text, HttpServletRequest request , HttpServletResponse response) throws IOException {
        if (StringUtils.isNotBlank(text)) {
            //过滤图片,h5标签
            text = text.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "").trim();
            //调用后台服务接口获取音频base64
            String result = "";
            try {
                result = XunFeiTtsUtil.convertText(text);
            } catch (Exception e) {
               // log.("【文字转语音接口调用异常】", e);
            }
            //音频数据
            byte[] audioByte = Base64.getDecoder().decode(result);
          //以@RestController

            response.setContentType("application/octet-stream;charset=UTF-8");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            try {
            //音频流
                os.write(audioByte);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                os.flush();
                os.close();
            }
        }
    }


    public static final String PATH = "D:\\test.wav";
    @ApiOperation(value = "文字转语音", notes = "文字转语音")
    @PostMapping(value = "text_to_audio2")
    public void textToAudio2(String text, HttpServletRequest request , HttpServletResponse response) throws IOException {

        if (StringUtils.isNotBlank(text)) {
            //过滤图片,h5标签
            text = text.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "").trim();
            //调用后台服务接口获取音频base64
            try {
                // 下面是构建文件流把生成语音文件
                ActiveXComponent ax;
                ax = new ActiveXComponent("Sapi.SpVoice");

                //运行时输出语音内容
                Dispatch spVoice = ax.getObject();
                ax = new ActiveXComponent("Sapi.SpFileStream");
                Dispatch spFileStream = ax.getObject();

                ax = new ActiveXComponent("Sapi.SpAudioFormat");
                Dispatch spAudioFormat = ax.getObject();

                //设置音频流格式
                Dispatch.put(spAudioFormat, "Type", new Variant(22));
                //设置文件输出流格式
                Dispatch.putRef(spFileStream, "Format", spAudioFormat);
                //调用输出 文件流打开方法，创建一个.wav文件
                Dispatch.call(spFileStream, "Open", new Variant(PATH), new Variant(3), new Variant(true));
                //设置声音对象的音频输出流为输出文件对象
                Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
                //设置音量 0到100
                Dispatch.put(spVoice, "Volume", new Variant(100));
                //设置朗读速度
                Dispatch.put(spVoice, "Rate", new Variant(-2));
                //开始朗读
                Dispatch.call(spVoice, "Speak", new Variant(text));
                //关闭输出文件
                Dispatch.call(spFileStream, "Close");
                Dispatch.putRef(spVoice, "AudioOutputStream", null);

                spVoice.safeRelease();
                ax.safeRelease();

            } catch (Exception e) {
                // log.("【文字转语音接口调用异常】", e);
            }
            byte[] byt = null;
            try{
                File f = new File(PATH);
                InputStream s = new FileInputStream (f);
                //音频数据
                byt = new byte[s.available()];
                s.read(byt);
                s.close();
            }catch (IOException e){e.printStackTrace();}

            // 以@RestController
            response.setContentType("application/octet-stream;charset=UTF-8");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            try {
                //音频流
                assert byt != null;
                os.write(byt);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                os.flush();
                os.close();
            }
        }
    }
}
