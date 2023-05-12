package com.iflytek.view;

import java.io.File;

public class Test {
    static String mText = "请赵行名到三号诊室2诊台。";

    public Test() {
    }

    public static void main(String[] args) {
        try {
            (new SpeakUntil()).Speak(mText);
        } catch (Exception var2) {
        }

        File f = new File("./tts_test.pcm");
        (new ShellUtils()).tranPcmToWavFile(f);
    }
}
