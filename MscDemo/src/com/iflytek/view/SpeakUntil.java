package com.iflytek.view;

import com.iflytek.cloud.speech.*;
import com.iflytek.util.Version;

/**
 * 功能描述
 *
 * @author: liyue
 * @date: 2022年12月02日 16:37
 */
public class SpeakUntil {
    private String mSavePath = "./tts_test.pcm";

    public void Speak(String str){
        StringBuffer param = new StringBuffer();
        param.append( "appid=" + Version.getAppid() );
//		param.append( ","+SpeechConstant.LIB_NAME_32+"=myMscName" );
        SpeechUtility.createUtility( param.toString() );

        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer( );
        //2.合成参数设置，详见《iFlytek MSC Reference Manual》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);//引擎类型
        mTts.setParameter(SpeechConstant.BACKGROUND_SOUND, "0");//背景音
        mTts.setParameter(SpeechConstant.PITCH, "50");//音调
        mTts.setParameter(SpeechConstant.VOICE_NAME, "小燕");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,mSavePath);// 路径
        mTts.setParameter(SpeechConstant.TTS_BUFFER_EVENT, "1");// 是否合成音频
        //设置合成音频保存位置（可自定义保存位置），保存在“./iflytek.pcm”
        //如果不需要保存合成音频，注释该行代码
        String curPath = System.getProperty("user.dir");
        mTts.setParameter( ResourceUtil.TTS_RES_PATH, curPath );
        //3.开始合成
        //合成监听器

        mTts.startSpeaking(str, mSynListener);


    }
    SynthesizerListener mSynListener = new SynthesizerListener(){
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {}

        @Override
        public void onEvent(int i, int i1, int i2, int i3, Object o, Object o1) {

        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {}
        //开始播放
        public void onSpeakBegin() {}
        //暂停播放
        public void onSpeakPaused() {}
        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {}
        //恢复播放回调接口
        public void onSpeakResumed() {}
    };
}
