package com.iflytek.view;

import java.io.*;

public  class  ShellUtils {
    public  File tranPcmToWavFile(File pcmFile) {
         String mSavePath = "./tts_test.wav";

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(pcmFile);
            //String wavfilepath = pcmFile.getAbsolutePath().substring(0, pcmFile.getAbsolutePath().lastIndexOf(".")) + "." + SysConstants.IVR_SPEAK_TYPE_WAV.toLowerCase();
            String wavfilepath = mSavePath;

            File file = new File(wavfilepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(wavfilepath);


            int PCMSize = 0;
            byte[] buf = new byte[1024 * 4];
            int size = fis.read(buf);

            while (size != -1) {
                PCMSize += size;
                size = fis.read(buf);
            }
            fis.close();

            //填入参数，比特率等等。这里用的是16位单声道 8000 hz

            WaveHeader header = new WaveHeader();
            //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
            header.fileLength = PCMSize + (44 - 8);
            header.FmtHdrLeth = 16;
            header.BitsPerSample = 16;
            header.Channels = 2;
            header.FormatTag = 0x0001;
            header.SamplesPerSec = 8000;
            header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
            header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
            header.DataHdrLeth = PCMSize;

            byte[] h = header.getHeader();

            assert h.length == 44; //WAV标准，头部应该是44字节
            //write header
            fos.write(h, 0, h.length);
            //write data stream
            fis = new FileInputStream(pcmFile);
            size = fis.read(buf);
            while (size != -1) {
                fos.write(buf, 0, size);
                size = fis.read(buf);
            }
            fis.close();
            fos.close();
            return new File(wavfilepath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}