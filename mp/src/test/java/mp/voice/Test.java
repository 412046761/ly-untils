package mp.voice;

import javax.annotation.Resource;

/**
 * 功能描述
 */
public class Test {
    @Resource
    private TtsService ttsService;

    /**
     * 生成中文音频信息
     */
    public byte[] getZHAudioBuffer(String gender, String chapterContent, String locale, String voiceName) {
        byte[] audioBuffer;
        if (chapterContent.length() <= 2600) {
            audioBuffer = ttsService.genAudioBytes(chapterContent, locale, gender, voiceName);
        } else {
            byte[] audioBuffer1 = ttsService.genAudioBytes(chapterContent.substring(0, chapterContent.length() / 2), locale, gender, voiceName);
            byte[] audioBuffer2 = ttsService.genAudioBytes(chapterContent.substring(chapterContent.length() / 2), locale, gender, voiceName);
            ByteArray byteArray = new ByteArray(audioBuffer1);
            byteArray.cat(audioBuffer2);
            audioBuffer = byteArray.getArray();
        }
        return audioBuffer;
    }

    /**
     * 生成英文音频信息
     */
    public byte[] getUSAudioBuffer(String gender, String chapterContent, String locale, String voiceName) {
        String[] words = chapterContent.split(" ");
        byte[] audioBuffer;
        int maxLength = 1500;
        if (words.length <= maxLength) {
            audioBuffer = ttsService.genAudioBytes(chapterContent, locale, gender, voiceName);
        } else {
            String[] part1 = new String[maxLength];
            String[] part2 = new String[words.length - maxLength];
            for (int i = 0; i < words.length; i++) {
                if (i < maxLength) {
                    part1[i] = words[i];
                } else {
                    part2[i - maxLength] = words[i];
                }
            }
            byte[] audioBuffer1 = ttsService.genAudioBytes(String.join(" ", part1), locale, gender, voiceName);
            byte[] audioBuffer2 = ttsService.genAudioBytes(String.join(" ", part2), locale, gender, voiceName);
            ByteArray byteArray = new ByteArray(audioBuffer1);
            byteArray.cat(audioBuffer2);
            audioBuffer = byteArray.getArray();
        }
        return audioBuffer;
    }
}
