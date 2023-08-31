//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.offlinetts.controller;

import com.example.offlinetts.Utils.XunFeiTtsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Api(
        tags = {"文字转语音"}
)
@RestController
@RequestMapping({"web/tts"})
public class TtsController {
    private static final Logger log = LoggerFactory.getLogger(TtsController.class);
    public String PATH = "Z:\\test.wav";

    public TtsController() {
    }

    @ApiOperation("文字转语音")
    @PostMapping({"text_to_audio"})
    public void textToAudio(String text, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //音频数据
        byte[] audioByte = null;
        //过滤图片,h5标签
        text = text.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "").trim();
        //调用后台服务接口获取音频base64
        log.info("请求文字转语音内容：{}", text);
        audioByte = XunFeiTtsUtil.convertTextOffLine(text);
        if (audioByte != null) {
            log.info("请求文字转语音响应成功：{}", text);
        }
        try {
        } catch (Exception e) {
            log.error("【文字转语音接口调用异常】", e);
        }
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



//
//    public byte[] readWav() {
//        byte[] byt = null;
//
//        try {
//            File f = new File(this.PATH);
//            if (!f.exists()) {
//                log.info(this.PATH + " 文件不存在!");
//                return null;
//            }
//
//            InputStream s = new FileInputStream(f);
//            byt = new byte[s.available()];
//            s.read(byt);
//            s.close();
//        } catch (IOException var4) {
//            var4.printStackTrace();
//        }
//
//        return byt;
//    }
//
//    class CallableImpl implements Callable {
//        CallableImpl() {
//        }
//
//        public byte[] call() {
//            return TtsController.this.readWav();
//        }
//    }
}
