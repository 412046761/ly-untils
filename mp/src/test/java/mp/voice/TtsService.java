package mp.voice;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.InputStream;

@Slf4j
@Component
public class TtsService {

    @Resource
    private Authentication authentication;

    /**
     * 合成音频
     */
    public byte[] genAudioBytes(String textToSynthesize, String locale, String gender, String voiceName) {

        String accessToken = authentication.genAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            return new byte[0];
        }
        try {
            HttpsURLConnection webRequest = HttpsConnection.getHttpsConnection(TtsConst.TTS_SERVICE_URI);
            webRequest.setDoInput(true);
            webRequest.setDoOutput(true);
            webRequest.setConnectTimeout(5000);
            webRequest.setReadTimeout(300000);
            webRequest.setRequestMethod("POST");

            webRequest.setRequestProperty("Content-Type", "application/ssml+xml");
            webRequest.setRequestProperty("X-Microsoft-OutputFormat", TtsConst.AUDIO_24KHZ_48KBITRATE_MONO_MP3);
            webRequest.setRequestProperty("Authorization", "Bearer " + accessToken);
            webRequest.setRequestProperty("X-Search-AppId", "07D3234E49CE426DAA29772419F436CC");
            webRequest.setRequestProperty("X-Search-ClientID", "1ECFAE91408841A480F00935DC390962");
            webRequest.setRequestProperty("User-Agent", "TTSAndroid");
            webRequest.setRequestProperty("Accept", "*/*");

            String body = XmlDom.createDom(locale, gender, voiceName, textToSynthesize);
            if (StringUtils.isEmpty(body)) {
                return new byte[0];
            }
            byte[] bytes = body.getBytes();
            webRequest.setRequestProperty("content-length", String.valueOf(bytes.length));
            webRequest.connect();
            DataOutputStream dop = new DataOutputStream(webRequest.getOutputStream());
            dop.write(bytes);
            dop.flush();
            dop.close();

            InputStream inSt = webRequest.getInputStream();
            ByteArray ba = new ByteArray();

            int rn2 = 0;
            int bufferLength = 4096;
            byte[] buf2 = new byte[bufferLength];
            while ((rn2 = inSt.read(buf2, 0, bufferLength)) > 0) {
                ba.cat(buf2, 0, rn2);
            }

            inSt.close();
            webRequest.disconnect();

            return ba.getArray();
        } catch (Exception e) {
            log.error("Synthesis tts speech failed {}", e.getMessage());
        }
        return null;
    }
}
