package com.mingrn.itumate.commons.utils.img;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Objects;

/**
 * 图片转化base64后再UrlEncode结果
 *
 * @author MinGR
 */
public class BaseImg64Util {

    private static final String BASE64_PREFIX_GIF = "data:image/gif;base64,";
    private static final String BASE64_PREFIX_PNG = "data:image/png;base64,";
    private static final String BASE64_PREFIX_JPEG = "data:image/jpeg;base64,";
    private static final String BASE64_PREFIX_ICON = "data:image/x-icon;base64,";

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseImg64Util.class);

    /**
     * 将一张本地磁盘图片转化成Base64字符串
     *
     * @param imgPath 本地图片地址
     * @return 图片转化base64后再UrlEncode结果
     */
    public static String getBaseImg64StrFromDisk(String imgPath) {
        Objects.requireNonNull(imgPath, "IMG Path Can't be bull");
        try (InputStream inputStream = new FileInputStream(imgPath)) {
            String suffix = imgPath.substring(imgPath.lastIndexOf(".") + 1);
            return imgIOTransform2Base64(inputStream, suffix);
        } catch (IOException e) {
            LOGGER.error("Image file IO Read Exception", e);
        }
        return null;
    }


    /**
     * 将一张网络图片转化成Base64字符串
     *
     * @param uri 网络图片资源
     * @return 图片转化base64后再UrlEncode结果
     */
    public static String getBaseImg64StrFromNet(String uri) throws IOException {

        Objects.requireNonNull(uri, "Net Img Uri Can't be null");

        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream()) {
            String suffix = uri.substring(uri.lastIndexOf(".") + 1);
            return imgIOTransform2Base64(inputStream, suffix);
        } catch (Exception e) {
            LOGGER.error("Read IMG IO From Net Exception", e);
        }
        return null;
    }

    /**
     * 将一张网络图片转化成Base64字符串
     * <br>推荐使用 {@link #getBaseImg64StrFromNet(String)}, 方法请求次数
     * 越多,效率越高
     *
     * @param uri 网络图片资源
     * @return 图片转化base64后再UrlEncode结果
     * @see #getBaseImg64StrFromNet(String)
     */
    @Deprecated
    public static String getBaseImg64StrFromNetWithHttpClient(String uri) {

        Objects.requireNonNull(uri, "Net Img Uri Can't be null");

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            String suffix = uri.substring(uri.lastIndexOf(".") + 1);
            HttpUriRequest uriRequest = RequestBuilder.get(uri).build();
            InputStream inputStream = httpClient.execute(uriRequest).getEntity().getContent();
            return imgIOTransform2Base64(inputStream, suffix);
        } catch (IOException e) {
            LOGGER.error("read image io from net err >>>>", e);
        }
        return null;
    }

    /**
     * bufferImg 转 Base64 Str
     *
     * @param bufferedImage img
     * @param format        IMG format
     * @return base64Img String
     */
    public static String getBaseImg64StrFromBufferImg(BufferedImage bufferedImage, String format) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, format, out);
            byte[] data = out.toByteArray();
            return byteArr2Base64(data);
        } catch (IOException e) {
            LOGGER.error("bufferedImage transform to base64 img str exception", e);
        }
        return null;
    }

    /**
     * IMG IO 转 Base64 Str(URLEncoder)
     *
     * @param img       IMG inputStream
     * @param imgFormat IMG Format
     * @return base64Img String
     */
    public static String imgIOTransform2Base64(InputStream img, String imgFormat) {
        // 读取图片字节数组
        byte[] data = null;
        try {
            data = new byte[img.available()];
            img.read(data);
            img.close();
        } catch (IOException e) {
            LOGGER.error("Image load stream err", e);
        }
        return getBase64Prefix(imgFormat) + byteArr2Base64(data);
    }

    /**
     * ImgIO 字节转 base64
     *
     * @param data IMG Byte
     * @return base64Img String
     */
    private static String byteArr2Base64(byte[] data) {
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过再URLEncode的字节数组字符串
        try {
            assert data != null;
            return URLEncoder.encode(encoder.encode(data), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("byte[] transform to base64 img str exception", e);
        }
        return null;
    }

    /**
     * Base64 Str 转 OutputStream
     *
     * @param base64       IMG Base64 字符串
     * @param outputStream 输出流
     */
    public static void base642Input(@NotNull String base64, OutputStream outputStream) {
        try (InputStream data = base642Input(base64)) {
            int len;
            byte[] bytes = new byte[1024];
            while ((len = data.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Base64 Str 转 InputStream
     *
     * @param base64 IMG Base64 字符串
     * @return {@link InputStream}
     */
    public static InputStream base642Input(@NotNull String base64) {
        Objects.requireNonNull(base64, "base64 img str can't be null");
        base64 = base64.replaceAll("^data:image\\/[\\w]+;base64,", "");
        return new ByteArrayInputStream(Base64Utils.decodeFromString(base64));
    }

    private static String getBase64Prefix(String format) {
        if ("gif".equalsIgnoreCase(format)) {
            return BASE64_PREFIX_GIF;
        } else if ("png".equalsIgnoreCase(format)) {
            return BASE64_PREFIX_PNG;
        } else if ("icon".equalsIgnoreCase(format)) {
            return BASE64_PREFIX_ICON;
        } else {
            return BASE64_PREFIX_JPEG;
        }
    }
}