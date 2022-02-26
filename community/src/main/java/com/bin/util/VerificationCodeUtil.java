package com.bin.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VerificationCodeUtil {

    //获取随机的大小写字母和数字组成的验证码
    public static String getRandomCode(int length) {
        int i = 0;
        StringBuilder randomCode = new StringBuilder(16);
        while (i < length) {
            Random random = new Random();
            int t = random.nextInt(123);
            if ((t >= 97 || (t >= 65 && t <= 90) || (t >= 48 && t <= 57))) {
                randomCode.append((char) t);
                i++;
            }
        }
        return String.valueOf(randomCode);
    }

    //绘制图片
    private static BufferedImage drawCodeImage(String code, int interLineNums, int width, int height, Color backColor) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        //绘制背景
        g.setColor(backColor);
        g.fillRect(0, 0, width, height);

        //绘制干扰线 interLineNums为干扰线数量
        //随机操作对象
        Random r = new Random();
        if (interLineNums > 0) {
            int x = r.nextInt(4), y = 0;
            int x1 = width - r.nextInt(4), y1 = 0;
            for (int i = 0; i < interLineNums; i++) {
                g.setColor(Color.GRAY);
                y = r.nextInt(height - r.nextInt(4));
                y1 = r.nextInt(height - r.nextInt(4));
                g.drawLine(x, y, x1, y1);
            }
        }

        //写验证码
        int fontSize = (int) (height * 0.8);//字体大小为图片高度的80%
        int fx = 2;
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, fontSize));

        //写字符
        for (int i = 0; i < code.length(); i++) {
            g.setColor(Color.BLACK);
            g.drawString(code.charAt(i) + "", fx, fontSize);
            fx += (width / code.length()) * (Math.random() * 0.3 + 0.8); //依据宽度浮动
        }

        //扭曲图片
        shearX(g, width, height, backColor);
        shearY(g, width, height, backColor);

        //添加噪点
        float yawpRate = 0.05f;// 噪声率
        int area = (int) (yawpRate * width * height);//噪点数量
        for (int i = 0; i < area; i++) {
            int xxx = r.nextInt(width);
            int yyy = r.nextInt(height);
            image.setRGB(xxx, yyy, getRandColorCode().getRGB());
        }
        g.dispose();
        return image;
    }

    //x方向扭曲
    private static void shearX(Graphics g, int w1, int h1, Color color) {
        Random random = new Random();
        int period = 4;
        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period
                    + (2.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            g.setColor(color);
            g.drawLine((int) d, i, 0, i);
            g.drawLine((int) d + w1, i, w1, i);
        }
    }

    //y方向扭曲
    private static void shearY(Graphics g, int w1, int h1, Color color) {
        Random random = new Random();
        int period = random.nextInt(10) + 10; // 25;
        int frames = 20;
        int phase = random.nextInt(2);
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (2.2831853071795862D * (double) phase) / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            g.setColor(color);
            g.drawLine(i, (int) d, i, 0);
            g.drawLine(i, (int) d + h1, i, h1);
        }
    }

    //获取随机的RGB颜色
    private static Color getRandColorCode() {
        int r, g, b;
        Random random = new Random();
        r = random.nextInt(256);
        g = random.nextInt(256);
        b = random.nextInt(256);
        return new Color(r, g, b);
    }

/*    //保存生成验证码图片
    public static void saveVerificationCodeImg() {
        String randomCode = VerificationCodeUtil.getRandomCode(4);
        System.out.println(randomCode);
        BufferedImage imageFromCode = VerificationCodeUtil.drawCodeImage(randomCode, 3, 100, 40, Color.WHITE);
        try {
            //获取项目的绝对路径
            String projectPath = new File(".").getCanonicalPath();
            //拼接验证码的绝对路径
            File file = new File(projectPath + "/src/main/resources/static/img/cptcha.png");
            ImageIO.write(imageFromCode, "png", file);
            System.out.println("成功保存到：" + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("保存失败");
            e.printStackTrace();
        }
    }*/

    public static BufferedImage getCodeImg(String randomCode) {
        return VerificationCodeUtil.drawCodeImage(randomCode, 3, 100, 40, Color.WHITE);
    }
}