package com.example.offlinetts.controller;

import cn.hutool.core.util.StrUtil;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author: liyue
 * @date: 2022年11月01日 15:04
 */
@Api(tags = "文字转语音")
@Slf4j
@RestController
@RequestMapping("web/tts")
public class TtsController {

    // TODO:PATH  服务器Z盘
    public String PATH = "Z:\\test.wav";

    @ApiOperation(value = "文字转语音")
    @PostMapping(value = "text_to_audio")
    public void textToAudio(String text, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = System.getProperty("user.dir");
        PATH = path + "\\test.wav";
        log.info(path);

        byte[] byt = null;
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Thread saveWav = new Thread(new Runnable() {
                @Override
                public void run() {
                    saveWav(text);
                }
            });
            Callable readWav =  new CallableImpl();
            executor.submit(saveWav);
            Future future = executor.submit(readWav);
            byt = (byte[]) future.get();
            executor.shutdown();
        } catch (Exception e) {
            log.info("【调用异常】:"+e);
        }
        if(byt == null)
            return;
        response.setContentType(request.getContentType());
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        try {
            //音频流
            os.write(byt);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            os.flush();
            os.close();
        }
    }

    /**
     * 有返回值的线程需要实现Callable接口
     */
    class CallableImpl implements Callable {
        @Override
        public byte[] call(){
            return readWav();
        }
    }

    /**
     * text 转语音
     */
    public void saveWav(String text) {
        if (StrUtil.isBlank(text)) return;
        //过滤图片,h5标签
        text = text.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "").trim();
        try {
            ActiveXComponent ax = null;
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
            log.info("【文字转语音接口调用异常】:"+e);
        }
    }

    public byte[] readWav() {
        byte[] byt = null;
        try {
            File f = new File(PATH);
            if (!f.exists()) {
                log.info(PATH + " 文件不存在!");
                return null;
            }
            InputStream s = new FileInputStream(f);
            //音频数据
            byt = new byte[s.available()];
            s.read(byt);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byt;
    }
}
