package com.iflytek.view;

import java.io.File;

/**
 *  测试语音合成
 */
public class Test {
    static String  mText = "请赵行名到三号诊室2诊台。";

    public static void main(String[] args) {
        new SpeakUntil().Speak(mText);
//        File f = new File("./tts_test.pcm");
//        new ShellUtils().tranPcmToWavFile(f);
    }


 }
