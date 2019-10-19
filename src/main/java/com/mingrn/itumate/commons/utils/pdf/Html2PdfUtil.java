package com.mingrn.itumate.commons.utils.pdf;

import com.itextpdf.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * HTML 转 PDF
 * 参考: <a href="https://my.oschina.net/u/1778261/blog/810277">使用flying-saucer 实现 html转pdf实现input框select,textarea checkbox等的显示</a>
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/6/5 16:45
 */
public class Html2PdfUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(Html2PdfUtil.class);

    public static void write2PDF(String html, String outputPath) {
        try (OutputStream os = new FileOutputStream(outputPath)) {
            write2PDF(html, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write2PDF(String html, OutputStream os) {
        write2PDF(html.getBytes(StandardCharsets.UTF_8), os);
    }

    public static void write2PDF(InputStream html, OutputStream os) throws IOException {
        write2PDF(inputStream2byte(html), os);
    }

    public static void write2PDF(byte[] html, OutputStream os) {
        try {
            ITextRenderer iTextRenderer = new ITextRenderer();
            ITextFontResolver fontResolver = iTextRenderer.getFontResolver();

            //支持中文
            String osName = System.getProperty("os.name");
            LOGGER.info("-------------os: {} -------------", osName);
            URL url = osName.contains("Window")
                    ? Thread.currentThread().getContextClassLoader().getResource("font/windows/SIMSUN.TTC")
                    : Thread.currentThread().getContextClassLoader().getResource("font/linux/simsun.ttc");

            Objects.requireNonNull(url);

            fontResolver.addFont(url.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            iTextRenderer.setDocument(html);
            iTextRenderer.layout();
            iTextRenderer.createPDF(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] inputStream2byte(InputStream inputStream) throws IOException {
        int rc;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        while ((rc = inputStream.read(buff)) > 0) {
            byteArrayOutputStream.write(buff, 0, rc);
        }
        return byteArrayOutputStream.toByteArray();
    }
}