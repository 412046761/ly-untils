package common.until;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片颜色转换
 */
public class ImageColorUtil {
    public static void converColor(String filePath) throws IOException {
        String fileName = filePath.substring(0,filePath.length() - 4);
        String fileSuffix = filePath.substring(filePath.length() - 3);
        //指定的图片路径
        FileInputStream dir = new FileInputStream(filePath);

        //通过ImageIO.read()方法来返回一个BufferedImage对象，可以对图片像素点进行修改
        BufferedImage bImage = ImageIO.read(dir);

        //获取图片的长宽高
        int width = bImage.getWidth();
        int height = bImage.getHeight();
        int minx = bImage.getMinTileX();
        int miny = bImage.getMinTileY();

        //遍历图片的所有像素点，并对各个像素点进行判断，是否修改
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixelnum = bImage.getRGB(i, j);
                //获取图片的rgb
                int red = (pixelnum >> 16)&255;
                int green = (pixelnum >> 8)&255;
                int blue = (pixelnum >> 0)&255;
                int gray = (red+green+blue)/3;

                int newred = red+150;
                if(newred>255){
                    newred = 255;
                }

                int newgreen = red+80;
                if(newgreen>255){
                    newgreen = 255;
                }

                int newblue = red+50;
                if(newblue>255) {
                    newblue = 255;
                }

                int grayPixel = 255<<24|newred<<16|newgreen<<8|newblue<<0;

                bImage.setRGB(i, j, grayPixel);

                WebImage.webImage();
                //进行判断，如果色素点在指定范围内，则进行下一步修改
//                if (rgb[0] == 255 && rgb[1] == 0 && rgb[2]== 0){
//                    //修改像素点
//                    bImage.setRGB(i, j, 0xBBFFFF);
//                }
            }
        }

        //输出照片保存在本地
        FileOutputStream ops;
        try {
            ops = new FileOutputStream(new File(fileName + "1." + fileSuffix));
            ImageIO.write(bImage,fileSuffix, ops);
            ops.flush();
            ops.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
