package com.mingrn.itumate.commons.utils.img;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.util.Iterator;

/**
 * 图片重绘实现类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/3/12 15:44
 */
public class ImgRedrawImpl implements ImgRedraw {

	private ImgRedrawImpl() {
	}

	/**
	 * 默认图片宽度(像素)
	 */
	private static final int DEFAULT_WIDTH = 1920;
	/**
	 * 默认图片高度(像素)
	 */
	private static final int DEFAULT_HEIGHT = 1080;
	/**
	 * 默认图片质量
	 * 0 < DEFAULT_QUALITY ≤ 1
	 */
	private static final float DEFAULT_QUALITY = 0.8f;

	/**
	 * return this.class Instance
	 */
	public static final ImgRedraw INSTANCE = new ImgRedrawImpl();


	@Override
	public InputStream redraw(File file, int maxWidth, int maxHeight, float quality) throws IOException {
		return redraw(new FileInputStream(file), maxWidth, maxHeight, quality);
	}

	@Override
	public InputStream redraw(InputStream inputStream, int maxWidth, int maxHeight, float quality) throws IOException {
		if (null == inputStream || inputStream.available() <= 0) {
			throw new IOException("the input stream can not be null or no available stream");
		}
		return redraw(ImageIO.read(inputStream), maxWidth, maxHeight, quality);
	}

	@Override
	public InputStream redraw(BufferedImage bufferedImage, int maxWidth, int maxHeight, float quality) {
		if (maxWidth == 0) {
			maxWidth = DEFAULT_WIDTH;
		}
		if (maxHeight == 0) {
			maxHeight = DEFAULT_HEIGHT;
		}

		Image resizedImage = null;
		Image img = new ImageIcon(bufferedImage).getImage();

		// 原始图片宽高
		int width = img.getWidth(null), height = img.getHeight(null);

		// 重绘图片宽高
		int newWidth = width < maxWidth ? width : maxWidth,
				newHeight = height < maxHeight ? height : maxHeight;

		// 调整新宽高
		if (width >= height) {
			resizedImage = img.getScaledInstance(newWidth,
					(newWidth * height) / width, Image.SCALE_SMOOTH);
		}

		if (resizedImage == null && height >= width) {
			resizedImage = img.getScaledInstance((newHeight * width) / height,
					newHeight, Image.SCALE_SMOOTH);
		}

		if (null == resizedImage) {
			resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		}

		BufferedImage drawImg = redrawGraphics(resizedImage);

		return bufferedImage2InputStream(drawImg, quality);
	}


	/**
	 * 重新绘制图形
	 *
	 * @param resizedImage 调整宽高后的图形 {@link java.awt.Image}
	 * @return {@link java.awt.image.BufferedImage}
	 */
	private static BufferedImage redrawGraphics(Image resizedImage) {
		// Ensures that all the pixels in the image are loaded.
		Image temp = new ImageIcon(resizedImage).getImage();

		int drawWidth = temp.getWidth(null), drawHeight = temp.getHeight(null);

		// Create the buffered image.
		BufferedImage drawImage = new BufferedImage(drawWidth, drawHeight, BufferedImage.TYPE_INT_RGB);

		// Copy image to buffered image.
		Graphics g = drawImage.createGraphics();


		// Clear background and paint the image.
		/// g.setColor(Color.white);
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
		g.drawImage(temp, 0, 0, null);
		g.dispose();

		// Soften.
		float softenFactor = 0.05f;
		float[] softenArray = {0, softenFactor, 0, softenFactor,
				1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
		Kernel kernel = new Kernel(3, 3, softenArray);
		ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

		return cOp.filter(drawImage, null);
	}


	/**
	 * {@link java.awt.image.BufferedImage} 转 {@link java.io.InputStream}
	 *
	 * @param img {@link java.awt.image.BufferedImage}
	 * @param quality 绘制图片质量
	 * @return {@link java.io.InputStream}
	 */
	private static InputStream bufferedImage2InputStream(BufferedImage img, float quality) {

		if (quality == 0) {
			quality = DEFAULT_QUALITY;
		}

		if (quality > 1) {
			quality = 1;
		}

		ImageWriter writer = null;
		ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromRenderedImage(img);

		Iterator<ImageWriter> iterator = ImageIO.getImageWriters(typeSpecifier, "jpg");
		while (iterator.hasNext()) {
			writer = iterator.next();
		}

		if (null == writer) {
			return null;
		}

		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);
		ColorModel colorModel = ColorModel.getRGBdefault();
		param.setDestinationType(new ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));

		// Write the jpeg to a file.
		InputStream inputStream = null;
		IIOImage iioImage = new IIOImage(img, null, null);
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			ImageOutputStream imgOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);

			writer.setOutput(imgOutputStream);
			writer.write(null, iioImage, param);

			inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// The following code implementation is not recommended, Just as a reference
		/*try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			// Encodes image as a JPEG data stream
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);

			// set draw quality
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img);
			param.setQuality(quality, true);

			encoder.setJPEGEncodeParam(param);
			encoder.encode(img);

			inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return inputStream;
	}
}