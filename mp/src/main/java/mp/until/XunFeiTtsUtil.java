package mp.until;

import com.beust.jcommander.internal.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okio.ByteString;
import org.springframework.data.redis.util.ByteUtils;
import okhttp3.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文字转语音
 */
public class XunFeiTtsUtil {
    //讯飞四个注入参数，保存在配置文件，便于复用和避免代码上传gitee后泄漏
    private static String hostUrl = "https://tts-api.xfyun.cn/v2/tts";
    private static String appid = "ce0e1edb";
    private static String apiSecret = "OWMwYTU1ZmVjYTBmOWFkNzFiYjNiOTM2";
    private static String apiKey = "bc7ed0328c8791345add3a4a7e6534c4";

    public static final Gson json = new Gson();
    private static String base64 = "";
    private static volatile boolean lock = true;

    public static void main(String[] args) throws Exception {
        String wsUrl = convertText("请王天一到A101科室2B诊台");

    }
    /**
     * 将文本转换为MP3格语音base64文件
     *
     * @param text 要转换的文本（如JSON串）
     * @return 转换后的base64文件
     * @throws IOException 异常
     */
    public static String convertText(String text) throws Exception {
        lock = true;
        base64 = "";
        // 构建鉴权url
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        //将url中的 schema http://和https://分别替换为ws:// 和 wss://
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        List<byte[]> list = Lists.newArrayList();
        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                try {
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //发送数据
                JsonObject frame = new JsonObject();
                JsonObject business = new JsonObject();
                JsonObject common = new JsonObject();
                JsonObject data = new JsonObject();
                // 填充common
                common.addProperty("app_id", appid);
                //填充business,AUE属性lame是MP3格式，raw是PCM格式
                business.addProperty("aue", "lame");
                business.addProperty("sfl", 1);
                business.addProperty("tte", "UTF8");//小语种必须使用UNICODE编码
                business.addProperty("vcn", "xiaoyan");//到控制台-我的应用-语音合成-添加试用或购买发音人，添加后即显示该发音人参数值，若试用未添加的发音人会报错11200
                business.addProperty("pitch", 50);
                business.addProperty("speed", 50);
                //填充data
                data.addProperty("status", 2);//固定位2
                try {
                    data.addProperty("text", Base64.getEncoder().encodeToString(text.getBytes("utf8")));
                    //使用小语种须使用下面的代码，此处的unicode指的是 utf16小端的编码方式，即"UTF-16LE"”
                    //data.addProperty("text", Base64.getEncoder().encodeToString(text.getBytes("UTF-16LE")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //填充frame
                frame.add("common", common);
                frame.add("business", business);
                frame.add("data", data);
                webSocket.send(frame.toString());
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                //处理返回数据
                System.out.println("receive=>");
                ResponseData resp = null;
                try {
                    resp = json.fromJson(text, ResponseData.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (resp != null) {
                    if (resp.getCode() != 0) {
                        System.out.println("error=>" + resp.getMessage() + " sid=" + resp.getSid());
                        return;
                    }
                    if (resp.getData() != null) {
                        String result = resp.getData().audio;
                        byte[] audio = Base64.getDecoder().decode(result);
                        list.add(audio);
                        // 说明数据全部返回完毕，可以关闭连接，释放资源
                        if (resp.getData().status == 2) {
                            String is = base64Concat(list);
                            base64 = is;
                            lock = false;
                            webSocket.close(1000, "");
                        }
                    }
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                System.out.println("socket closing");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                System.out.println("socket closed");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                System.out.println("connection failed" + response.message());
            }
        });
        while (lock) {
        }
        return base64;
    }

    /**
     *  * base64拼接
     *
     */
    static String base64Concat(List<byte[]> list) {
        int length = 0;
        for (byte[] b : list) {
            length += b.length;
        }
        byte[] retByte = new byte[length];
        for (byte[] b : list) {
            retByte = ByteUtils.concat(retByte, b);
        }
        return cn.hutool.core.codec.Base64.encode(retByte);
    }

    /**
     *  * 获取权限地址
     *  *
     *  * @param hostUrl
     *  * @param apiKey
     *  * @param apiSecret
     *  * @return
     *
     */
    private static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").
                append("date: ").append(date).append("\n").
                append("GET ").append(url.getPath()).append(" HTTP/1.1");
        Charset charset = Charset.forName("UTF-8");
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        String authorization = String.format("hmac username=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder().
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(charset))).
                addQueryParameter("date", date).
                addQueryParameter("host", url.getHost()).
                build();
        return httpUrl.toString();
    }


    public static class ResponseData {
        private int code;
        private String message;
        private String sid;
        private Data data;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return this.message;
        }

        public String getSid() {
            return sid;
        }

        public Data getData() {
            return data;
        }
    }

    private static class Data {
        //标志音频是否返回结束  status=1，表示后续还有音频返回，status=2表示所有的音频已经返回
        private int status;
        //返回的音频，base64 编码
        private String audio;
        // 合成进度
        private String ced;
    }
}
