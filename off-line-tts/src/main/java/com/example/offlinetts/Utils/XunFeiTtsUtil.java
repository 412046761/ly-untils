package com.example.offlinetts.Utils;


import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 科大讯飞
 */
@Component
public class  XunFeiTtsUtil {
    private static String appid = "bad00a1d";
    private static String base64 = "";

    private static volatile boolean lock = true;
    // resources\static\tts 内容需要放到 System.out.println(System.getProperty("java.library.path")) 路径下;
     public static String ttsPath = "D:\\Program Files\\JDK\\jdk1.8.0_112\\bin\\";
//    public static String ttsPath = "/usr/lib64/";

    /**
     * 离线文本转换为语音
     *
     * @param text 要转换的文本（如JSON串）
     * @return 转换后的byte[]
     * @throws IOException 异常
     */
    public static byte[] convertTextOffLine(String text) throws Exception {
        //登录参数,appid与msc库绑定,请勿随意改动
        String loginParams = "appid = " + appid + ", work_dir = .";
        String session_begin_params = "engine_type = local, voice_name = xiaoyan, text_encoding = UTF-8, tts_res_path = fo|" + ttsPath + "xiaoyan.jet;fo|" + ttsPath + "common.jet, sample_rate = 16000, speed = 50, volume = 100, pitch = 50, rdn = 2";
        String sessionId = null;
        RandomAccessFile raf = null;
        byte[] audioByte = new byte[0];
        try {
            //登录
            int loginCode = MscLibrary.INSTANCE.MSPLogin(null, null, loginParams);

            if (loginCode != 0) {
                //登录失败
                return null;
            }

            //初始session
            IntByReference errCode = new IntByReference();
            sessionId = MscLibrary.INSTANCE.QTTSSessionBegin(session_begin_params, errCode);

            if (errCode.getValue() != 0) {
                //会话失败
                return null;
            }

            //放入文本
            int textPutCode = MscLibrary.INSTANCE.QTTSTextPut(sessionId, text, text.getBytes().length, null);

            if (textPutCode != 0) {
                //放入文本失败
                return null;
            }

            //写入空的头格式
            raf = new RandomAccessFile("tts_sample.wav", "rw");
            raf.write(new byte[44]);

            //  List<byte[]> list = Lists.newArrayList();
            int dataSize = 0;
            IntByReference audioLen = new IntByReference();
            IntByReference synthStatus = new IntByReference();
            while (true) {
                Pointer pointer = MscLibrary.INSTANCE.QTTSAudioGet(sessionId, audioLen, synthStatus, errCode);
                if (pointer != null && audioLen.getValue() > 0) {
                    //        list.add(pointer.getByteArray(0, audioLen.getValue()));
                    // 写入合成内容
                    raf.write(pointer.getByteArray(0, audioLen.getValue()));
                    // 记录数据长度
                    dataSize += audioLen.getValue();
                }
                //转换异常或转换结束跳出循环
                if (errCode.getValue() != 0 || synthStatus.getValue() == 2) {
                    break;
                }
            }
            if (textPutCode != 0) {
                //获取转换数据失败
                return null;
            }
            // list.add(0, getWavHeader(dataSize, 16000, 32000, 1, 16));
            //定位到文件起始位置
            raf.seek(0);
            //写入真实头格式
            raf.write(getWavHeader(dataSize, 16000, 32000, 1, 16));
            audioByte = toByteArrayMapped("tts_sample.wav");
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "请确认 'resources//static//tts’ 内容已放到 " + System.getProperty("java.library.path") + "之一路径下" ;
            System.out.println(msg);
        } finally {
            if (sessionId != null) {
                MscLibrary.INSTANCE.QTTSSessionEnd(sessionId, "Normal");
            }
            MscLibrary.INSTANCE.MSPLogout();
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return audioByte;
    }


    /**
     *
     * <p>Title: toByteArrayMapped</p>
     * <p>Description: 读取大文件</p>
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArrayMapped(String filename) throws IOException {

        FileChannel fc = null;
        RandomAccessFile rf=new RandomAccessFile(filename, "r");
        try {
            fc = rf.getChannel();
            MappedByteBuffer mappedByteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            byte[] result = new byte[(int) fc.size()];
            if (mappedByteBuffer.remaining() > 0) {
                mappedByteBuffer.get(result, 0, mappedByteBuffer.remaining());
            }
            return result;
        }  catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                rf.close();
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * jna 调第三方动态库
     */
    public interface MscLibrary extends Library {

//        MscLibrary INSTANCE = Native.load(ttsPath + "libmsc.so", MscLibrary.class);

        MscLibrary INSTANCE = Native.load(ttsPath + "msc_x64.dll", MscLibrary.class);

        int MSPLogin(String username, String password, String param);

        int MSPLogout();

        String QTTSSessionBegin(String params, IntByReference errorCode);

        int QTTSTextPut(String sessionID, String textString, int textLen, String params);

        Pointer QTTSAudioGet(String sessionID, IntByReference audioLen, IntByReference synthStatus, IntByReference errorCode);

        int QTTSSessionEnd(String sessionID, String hints);
    }

    /**
     * @param totalAudioLen 音频数据总大小
     * @param sampleRate    采样率
     * @param byteRate      位元（组）率(每秒的数据量 单位 字节/秒)   采样率(44100之类的) * 通道数(1,或者2)*每次采样得到的样本位数(16或者8) / 8;
     * @param nChannels     声道数量
     * @param weikuan       位宽
     */
    private static byte[] getWavHeader(int totalAudioLen, int sampleRate, int byteRate, int nChannels, int weikuan) {
        long totalDataLen = totalAudioLen + 36;
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) (nChannels & 0xff);
        header[23] = (byte) ((nChannels >> 8) & 0xff);

        header[24] = (byte) (sampleRate & 0xff);//采样率
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);

        header[28] = (byte) (byteRate & 0xff);//取八位
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);

        int b = weikuan * nChannels / 8;//每次采样的大小
        header[32] = (byte) (b & 0xff); // block align
        header[33] = (byte) ((b >> 8) & 0xff);

        header[34] = (byte) (weikuan & 0xff);//位宽
        header[35] = (byte) ((weikuan >> 8) & 0xff);

        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        return header;
    }

}
