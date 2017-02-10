package com.xtr.comm.verify;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by abiao on 2016/6/27.
 */
public class ValidateCodeDiv {
    /**设置宽度**/
    private int width = 150;
    //字体类型
    private static String[] fontNames = new String[] { "Courier", "Arial","Verdana", "Georgia", "Times", "Tahoma" };
    /**设置高度**/
    private int height = 40;
    //字体样式
    private static int[] fontStyle = new int[] { Font.PLAIN, Font.BOLD,
            Font.ITALIC, Font.BOLD | Font.ITALIC };
    /**画板后面的画条**/
    private int disturbLineNum = 8;
    private OutputStream os;
    /**设置字符个数**/
    private int charCnt = 4;
    //要产生的所有字符
    final private char[] chars = "2345678ABCDEFGHJKLMPQRSTUVWXYabcdefhkmnqrstuvwx".toCharArray();
    public ValidateCodeDiv(OutputStream os) {
        this.os = os;
    }
    /**
     * 绘制验证码
     * 	BufferedImage.TYPE_INT_RGB ： 表示一个图像，该图像具有整数像素的 8 位 RGB 颜色
     * @throws IOException
     */
    public String drawCode() throws IOException {
        //设置画板图像的宽高
        BufferedImage bi = new BufferedImage(this.width, this.height,BufferedImage.TYPE_INT_RGB);
        //创建画笔,宽高就是BufferedImage指定的
        Graphics2D gh = bi.createGraphics();
        //设置画笔颜色，把十六进制转换成十进制
        gh.setColor(new Color(245,245,245));
        //用画笔画出一个矩形，从0,0开始，设置宽高
        gh.fillRect(0, 0, this.width, this.height);
        //画板后面的画条
        drawDisturbLine(gh);
        //得到验证码数字
        char[] codes = generateCode();
        //创建图片画板，大小是4个
        BufferedImage[] bis = new BufferedImage[charCnt];
        //循环遍历得到的数字
        for(int i=0;i<charCnt;i++){
            /**
             * 给得到的验证码设置字体样式，类型，大小。等等
             * 在把得到的验证码放在画板图片数组中，用画笔画上去
             */
            bis[i] = generateBuffImg(codes[i]);
            /**
             * bis[i]呈现的图像
             * null:为 null，则此方法不执行任何动作。
             * (int) (this.height * 0.7) * i: 用户空间中呈现该图像左上角位置的 x 坐标
             * this.height * 0.7：图片之间的间距
             * 0,用户空间中呈现该图像左上角位置的 y 坐标  因为是矩形
             */
            gh.drawImage(bis[i], null, (int) (this.height * 0.62) * i, 0);
        }
        gh.dispose();
        /**
         * bi:要写入的图片画板
         * gif:图片格式
         * os:输出
         */
        ImageIO.write(bi, "gif", os);
        //返回一个string对象
        return new String(codes);
    }
    /**
     * 设置画板后面的画条
     * @param graphics
     */
    private void drawDisturbLine(Graphics2D graphics) {
        for (int i = 0; i < disturbLineNum; i++) {
            graphics.setColor(getRandomColor());
            int x = (int) (Math.random() * 10000) % (this.width + 1) + 1;
            int x1 = (int) (Math.random() * 10000) % (this.width + 1) + 1;
            int y = (int) (Math.random() * 10000) % (this.height + 1) + 1;
            int y1 = (int) (Math.random() * 10000) % (this.height + 1) + 1;
            graphics.drawLine(x, y, x1, y1);
        }

    }
    /**
     * 得到4验证码数字
     * @return 验证码数组
     */
    private char[] generateCode() {

        char[] ret = new char[charCnt];
        for(int i=0;i<charCnt;i++){
            /**
             * Math.random() * 10000 = (0-9999)%chars.length的余
             * 得到下标
             */
            int letterPos = (int) (Math.random() * 10000) % (chars.length);
            //得到这个下标的值，赋值
            ret[i] = chars[letterPos];
        }
        return ret;
    }
    /**
     * 设置字体颜色
     *  由于颜色背景是透明的，
     *   (int) (Math.random() * 10000) % 200; 所以余200要小于255的透明度
     *
     * @return 十六进制的颜色
     */
    private Color getRandomColor() {
        int r = (int) (Math.random() * 10000) % 200;
        int g = (int) (Math.random() * 10000) % 200;
        int b = (int) (Math.random() * 10000) % 200;
        return new Color(r, g, b);
    }
    /**
     * 给验证码设置字体样式，类型，大小。等等
     * @param c 穿过来的字符
     * @return 画板图片
     */
    private BufferedImage generateBuffImg(char c) {
        //把传过来的字符转成字符串
        String tmp = Character.toString(c);
        /* 字体颜色 */
        Color forecolor = getRandomColor();
        //背景颜色(透明的)
        Color backcolor = new Color(255, 255, 255, 0);
        //设置字体类型
        String fontName = getRandomFontName();
        //设置字体样式
        int fontStyle = getRandomStyle();
        //设置字体大小
        int fontSize = getRandomSize();
        //设置x，y坐标
        int strX = (this.height - fontSize) / 2;
        int strY = (this.height - fontSize) / 2 + fontSize;
        //设置字体旋转度
        double arch = getRandomArch();
        //创建画板
        BufferedImage ret = new BufferedImage(this.height, this.height,
                BufferedImage.TYPE_INT_ARGB);
        //创建画笔
        Graphics2D g = ret.createGraphics();
        //设置颜色
        g.setColor(backcolor);
        //创建矩形
        g.fillRect(0, 0, this.height, this.height);
        //设置背景颜色
        g.setColor(forecolor);
        //设置字体大小，样式，类型
        g.setFont(new Font(fontName, fontStyle, fontSize));
        /**
         * 为了不让字体旋转，转出画板,
         * arch - 用弧度测量的旋转角度
         * this.height / 2 - 旋转锚点的 X 坐标
         * this.height / 2 - 旋转锚点的 Y 坐标
         */
        g.rotate(arch, this.height / 2, this.height / 2);
        //用画笔画在画板上
        g.drawString(tmp, strX, strY);
        //释放资源
        g.dispose();
        //返回你画好的画板图片
        return ret;
    }
    /**
     * 设置字体类型
     * @return 字体类型
     */
    private String getRandomFontName() {
        int pos = (int) (Math.random() * 10000) % (fontNames.length);
        return fontNames[pos];
    }
    /**
     * 设置字体大小
     * @return 字体的大小程度
     */
    private int getRandomSize() {
        int max = (int) (this.height * 0.98);
        int min = (int) (this.height * 0.75);
        return (int) (Math.random() * 10000) % (max - min + 1) + min;
    }
    /**
     * 设置字体旋转度
     * @return
     */
    private double getRandomArch() {
        return ((int) (Math.random() * 1000) % 2 == 0 ? -1 : 1) * Math.random()/2+0.2;
    }
    /**
     * 设置字体样式
     * @return
     */
    private int getRandomStyle() {
        int pos = (int) (Math.random() * 10000) % (fontStyle.length);
        return fontStyle[pos];
    }
}
