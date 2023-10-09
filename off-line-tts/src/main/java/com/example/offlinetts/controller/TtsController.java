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

    @ApiOperation("文字转语音")
    @PostMapping({"text_to_audio"})
    public void textToAudio(String text, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //音频数据
        byte[] audioByte = null;
        //过滤图片,h5标签
        text = text.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "").trim();
        //获取音频
        audioByte = XunFeiTtsUtil.convertTextOffLine(text);

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
