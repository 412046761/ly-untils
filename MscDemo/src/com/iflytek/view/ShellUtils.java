//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.iflytek.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShellUtils {
    public ShellUtils() {
    }

    public File tranPcmToWavFile(File pcmFile) {
        String mSavePath = "./tts_test.wav";
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(pcmFile);
            File file = new File(mSavePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(mSavePath);
            int PCMSize = 0;
            byte[] buf = new byte[4096];

            int size;
            for(size = fis.read(buf); size != -1; size = fis.read(buf)) {
                PCMSize += size;
            }

            fis.close();
            WaveHeader header = new WaveHeader();
            header.fileLength = PCMSize + 36;
            header.FmtHdrLeth = 16;
            header.BitsPerSample = 16;
            header.Channels = 2;
            header.FormatTag = 1;
            header.SamplesPerSec = 8000;
            header.BlockAlign = (short)(header.Channels * header.BitsPerSample / 8);
            header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
            header.DataHdrLeth = PCMSize;
            byte[] h = header.getHeader();

            assert h.length == 44;

            fos.write(h, 0, h.length);
            fis = new FileInputStream(pcmFile);

            for(size = fis.read(buf); size != -1; size = fis.read(buf)) {
                fos.write(buf, 0, size);
            }

            fis.close();
            fos.close();
            File var12 = new File(mSavePath);
            return var12;
        } catch (Exception var26) {
            var26.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException var25) {
                    var25.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var24) {
                    var24.printStackTrace();
                }
            }

        }

        return null;
    }
}
