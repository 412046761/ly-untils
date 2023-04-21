package common.until;


import java.net.URLEncoder;

/**
* 网络图片文字识别
*/
public class WebImage {


    /**
    * 重要提示代码中所需工具类
    */
    public static String webImage() {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/webimage";
        try {
            // 本地文件路径
            String filePath = "./mp/src/main/resources/picture/mh1.png";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.72b968b29adb193e98c15db4fa77fef4.2592000.1684570707.282335-32630705";

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}