package com.mingrn.itumate.commons.utils.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 中文算数图形验证码
 * <br>
 * <code>
 *     VerifyCodeUtil verify = new VerifyCodeUtil();
 *     Map<String, Object> data = verify.drawVerificationCodeImage(?);
 *     // result =>String result = data.get("result");
 *     // Img => BufferedImage img = (BufferedImage) data.get("img");
 *
 *     // HttpServletRequest request;
 *     // HttpServletResponse response;
 *     request.setCharacterEncoding("UTF-8");
 *     response.setContentType("text/html;charset=utf-8");
 *
 *     // Clear Cache
 *     response.setHeader("Cache-Control", "no-cache");
 *     response.setHeader("Pragma", "No-cache");
 *     response.setDateHeader("Expires", 0);
 *     response.setContentType("image/jpeg");
 *
 *     ImageIO.write(img, "JPEG", response.getOutputStream());
 * </code>
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019-04-05 23:11
 */
public final class VerifyCodeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCodeUtil.class);

    private static final int DEFAULT_IMG_WIDTH = 150;

    /**
     * 宽度比例值
     */
    private static final int DEFAULT_IMG_WIDTH_SCALE = 16;

    /**
     * 高度比例值
     */
    private static final int DEFAULT_IMG_HEIGHT_SCALE = 5;

    /**
     * 随机数
     */
    private static final Random RANDOM = new Random();

    /**
     * 中文数字
     */
    /// private static final String [] CNUMBERS = "零,一,二,三,四,五,六,七,八,九,十".split(",");

    /**
     * 零一二三四五六七八九十乘除加减等于？
     * Unicode code
     */
    private static final String CVC_NUMBERS = "\u96F6\u4E00\u4E8C\u4E09\u56DB\u4E94\u516D\u4E03\u516B\u4E5D\u5341\u4E58\u9664\u52A0\u51CF\u7B49\u4E8E\uFF1F";

    /**
     * 字体
     */
    private static final String[] FONT_FAMILY = new String[]{
            "Hanzipen SC", "Wawati SC", "STZhongsong", "Hannotate SC", "Songti SC", "Yuanti SC", "YouYuan"
    };

    /**
     * 运算符号
     */
    private static final Map<String, Integer> OPERATION_SYMBOL = new HashMap<>(8);

    static {
        OPERATION_SYMBOL.put("*", 11);
        OPERATION_SYMBOL.put("/", 12);
        OPERATION_SYMBOL.put("+", 13);
        OPERATION_SYMBOL.put("-", 14);
        OPERATION_SYMBOL.put("等", 15);
        OPERATION_SYMBOL.put("于", 16);
        OPERATION_SYMBOL.put("？", 17);
    }

    /**
     * 生成图形验证码
     *
     * @param imgWidth 图片宽度,为0时默认设置 <code>DEFAULT_IMG_WIDTH</code>
     */
    public Map<String, Object> drawVerificationCodeImage(int imgWidth) throws ScriptException {

        final int newImgWidth = imgWidth <= 50 ? DEFAULT_IMG_WIDTH : imgWidth;
        final int newImgHeight = (newImgWidth * DEFAULT_IMG_HEIGHT_SCALE) / DEFAULT_IMG_WIDTH_SCALE;

        BufferedImage image = new BufferedImage(newImgWidth, newImgHeight, BufferedImage.TYPE_INT_RGB);

        //画布
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);

        //填充背景
        g.fillRect(0, 0, newImgWidth, newImgHeight);
        //Set the brush color
        g.setColor(getRandomColor(200, 250));
        //image border
        g.drawRect(0, 0, newImgWidth - 2, newImgHeight - 2);

        //随机干扰线,干扰线数量为 IMG 高度
        g.setColor(getRandomColor(110, 133));
        for (int i = 0; i < newImgHeight; i++) {
            drawDisturbLine(g, newImgWidth, newImgHeight, true);
            drawDisturbLine(g, newImgWidth, newImgHeight, false);
        }

        // 随机算法
        StringBuilder randomAlgorithm = genRandomMathStr();

        // 计算结果
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object result = engine.eval(randomAlgorithm + "");

        randomAlgorithm.append("\u7B49\u4E8E\uFF1F");

        LOGGER.info("验证码 : " + randomAlgorithm);

        int len = randomAlgorithm.length();
        StringBuilder buffer = new StringBuilder();
        for (int index = 0; index < len; index++) {

            int charIndex = (index % 2 == 1) || (index > len - 3) ?
                    OPERATION_SYMBOL.get(String.valueOf(randomAlgorithm.charAt(index))) : Integer.parseInt(String.valueOf(randomAlgorithm.charAt(index)));

            String cn = String.valueOf(CVC_NUMBERS.charAt(charIndex));
            buffer.append(cn);
            drawRandomStr((Graphics2D) g, cn, index, len, newImgWidth, newImgHeight);
        }

        LOGGER.info("汉字验证码 : " + buffer);

        g.dispose();

        Map<String, Object> data = new HashMap<>(2);
        data.put("result", result);
        data.put("img", image);

        return data;
    }

    /**
     * 随机加减乘表达式
     */
    private StringBuilder genRandomMathStr() {

        StringBuilder cn = new StringBuilder().append(RANDOM.nextInt(10));

        for (int i = 0; i < 2; i++) {
            int operands = (int) Math.round(Math.random() * 2);
            symbol(operands, cn);
            cn.append(RANDOM.nextInt(10));
        }
        return cn;
    }

    /**
     * 随机加减乘符号
     * <br>
     * 不使用除法
     */
    private void symbol(int operands, StringBuilder cn) {
        if (operands == 0) {
            cn.append("*");
        } else if (operands == 1) {
            cn.append("+");
        } else if (operands == 2) {
            cn.append("-");
        } else {
            cn.append("+");
        }
    }

    /**
     * 绘制文字
     *
     * @param g         Graphics
     * @param cn        文字
     * @param index     随机数字字符下标
     * @param len       字符长度
     * @param imgWidth  <code>g</code> 宽度
     * @param imgHeight <code>g</code> 高度
     */
    private void drawRandomStr(Graphics2D g, String cn, int index, final int len, final int imgWidth, final int imgHeight) {

        // 设置文字样式
        g.setFont(new Font(FONT_FAMILY[RANDOM.nextInt(FONT_FAMILY.length)], Font.BOLD, imgHeight >> 1));

        // 设置文字颜色
        int rc = RANDOM.nextInt(255);
        int gc = RANDOM.nextInt(255);
        int bc = RANDOM.nextInt(255);
        g.setColor(new Color(rc, gc, bc));

        // 设置文字x, y坐标随机偏移值
        int x = RANDOM.nextInt(imgHeight >> 4);
        int y = RANDOM.nextInt(imgHeight >> 4);
        g.translate(x, y);

        // 文字旋转角度
        int degree = RANDOM.nextInt() % 15;
        g.rotate(degree * Math.PI / 180, (double) (imgWidth / (len + 2)) * index, imgHeight >> 1);

        // 文字位置
        g.drawString(cn, (imgWidth / (len + 2) + 2) * index, imgHeight / 2);

        g.rotate(-degree * Math.PI / 180, (double) (imgWidth / (len + 2)) * index, imgHeight >> 1);
    }

    /**
     * 干扰线
     *
     * @param g         Graphics
     * @param imgWidth  <code>g</code> 宽度
     * @param imgHeight <code>g</code> 高度
     */
    private void drawDisturbLine(Graphics g, final int imgWidth, final int imgHeight, boolean isPlus) {
        int x1 = RANDOM.nextInt(imgWidth);
        int y1 = RANDOM.nextInt(imgHeight);
        int x2 = RANDOM.nextInt(13);
        int y2 = RANDOM.nextInt(15);
        g.drawLine(x1, y1, isPlus ? x1 + x2 : x1 - x2, isPlus ? y1 + y2 : y1 - y2);
    }


    /**
     * 随机颜色值
     *
     * @param fc fc
     * @param bc bc
     */
    private Color getRandomColor(int fc, int bc) {
        int colorUpLimit = 255;
        if (fc > colorUpLimit) {
            fc = colorUpLimit;
        }
        if (bc > colorUpLimit) {
            bc = colorUpLimit;
        }
        // 生成 RGB 三色值
        int r = fc + RANDOM.nextInt(bc - fc - 16);
        int g = fc + RANDOM.nextInt(bc - fc - 14);
        int b = fc + RANDOM.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }
}