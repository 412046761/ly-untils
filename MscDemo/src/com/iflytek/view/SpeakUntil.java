//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.iflytek.view;

import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.iflytek.cloud.speech.SynthesizerListener;
import com.iflytek.util.Version;.1;

public class SpeakUntil {
    private String mSavePath = "./tts_test.pcm";
    SynthesizerListener mSynListener = new 1(this);

    public SpeakUntil() {
    }

    public void Speak(String str) {
        StringBuffer param = new StringBuffer();
        param.append("appid=" + Version.getAppid());
        SpeechUtility.createUtility(param.toString());
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer();
        mTts.setParameter("engine_type", "cloud");
        mTts.setParameter("background_sound", "0");
        mTts.setParameter("pitch", "50");
        mTts.setParameter("voice_name", "小燕");
        mTts.setParameter("speed", "50");
        mTts.setParameter("volume", "80");
        mTts.setParameter("tts_audio_path", this.mSavePath);
        mTts.setParameter("tts_buf_event", "1");
        String curPath = System.getProperty("user.dir");
        mTts.setParameter("tts_res_path", curPath);
        mTts.startSpeaking(str, this.mSynListener);
    }
}
