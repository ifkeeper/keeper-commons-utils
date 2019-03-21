package com.mingrn.itumate.commons.utils.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图形重绘
 * <br>
 * 宽度与高度绘制时不会使用自定义宽高,会按原比例进行缩放.
 * 如:原图宽大于高,在实际缩放时会按照新宽比例进行计算新高:<code>int newHeight = (newWidth * height) / width</code>.
 * 同样,若原图高大于宽,在实际缩放时同样会按照新高比例进行计算:<code>int newWidth = (newHeight * width) / height</code>.
 * 自定义宽高仅作为参考值
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/3/12 15:42
 */
public interface Redraw {

	/**
	 * 重绘图片
	 * <br>
	 * 缩放图片(压缩图片质量,改变图片尺寸)
	 * 宽默认值 {@link GraphicsRedraw#DEFAULT_WIDTH}
	 * 高默认值 {@link GraphicsRedraw#DEFAULT_HEIGHT}
	 * 图片质量 如0.7f 相当于70%质量,如何值为 0,默认值 {@link GraphicsRedraw#DEFAULT_QUALITY}
	 *
	 * @param file {@link java.io.File}
	 * @return {@link java.io.InputStream}
	 * @author MinGRn <br > MinGRn97@gmail.com
	 */
	InputStream redraw(File file) throws IOException;

	/**
	 * 重绘图片
	 * <br>
	 * 缩放图片(压缩图片质量,改变图片尺寸)
	 * 宽默认值 {@link GraphicsRedraw#DEFAULT_WIDTH}
	 * 高默认值 {@link GraphicsRedraw#DEFAULT_HEIGHT}
	 * 图片质量 如0.7f 相当于70%质量,如何值为 0,默认值 {@link GraphicsRedraw#DEFAULT_QUALITY}
	 *
	 * @param inputStream {@link java.io.InputStream}
	 * @return {@link java.io.InputStream}
	 * @author MinGRn <br > MinGRn97@gmail.com
	 */
	InputStream redraw(InputStream inputStream) throws IOException;

	/**
	 * 重绘图片
	 * <br>
	 * 缩放图片(压缩图片质量,改变图片尺寸)
	 * 宽默认值 {@link GraphicsRedraw#DEFAULT_WIDTH}
	 * 高默认值 {@link GraphicsRedraw#DEFAULT_HEIGHT}
	 * 图片质量 如0.7f 相当于70%质量,如何值为 0,默认值 {@link GraphicsRedraw#DEFAULT_QUALITY}
	 *
	 * @param bufferedImage {@link java.awt.image.BufferedImage}
	 * @return {@link java.io.InputStream}
	 * @author MinGRn <br > MinGRn97@gmail.com
	 */
	InputStream redraw(BufferedImage bufferedImage);

	/**
	 * 重绘图片
	 * <br>
	 * 缩放图片(压缩图片质量,改变图片尺寸)
	 *
	 * @param file {@link java.io.File}
	 * @param maxWidth 图片最大宽度,如何值为 0,则设置默认值 {@link GraphicsRedraw#DEFAULT_WIDTH}
	 * @param maxHeight 图片最大高度,如何值为 0,则设置默认值 {@link GraphicsRedraw#DEFAULT_HEIGHT}
	 * @param quality 图片质量,如0.7f 相当于70%质量,如何值为 0,则设置默认值 {@link GraphicsRedraw#DEFAULT_QUALITY}
	 * @return {@link java.io.InputStream}
	 * @author MinGRn <br > MinGRn97@gmail.com
	 */
	InputStream redraw(File file, int maxWidth, int maxHeight, float quality) throws IOException;

	/**
	 * 重绘图片
	 * <br>
	 * 缩放图片(压缩图片质量,改变图片尺寸)
	 *
	 * @param inputStream {@link java.io.InputStream}
	 * @param maxWidth 图片最大宽度,如何值为 0,则设置默认值 {@link GraphicsRedraw#DEFAULT_WIDTH}
	 * @param maxHeight 图片最大高度,如何值为 0,则设置默认值 {@link GraphicsRedraw#DEFAULT_HEIGHT}
	 * @param quality 图片质量,如0.7f 相当于70%质量,如何值为 0,则设置默认值 {@link GraphicsRedraw#DEFAULT_QUALITY}
	 * @return {@link java.io.InputStream}
	 * @author MinGRn <br > MinGRn97@gmail.com
	 */
	InputStream redraw(InputStream inputStream, int maxWidth, int maxHeight, float quality) throws IOException;

	/**
	 * 重绘图片
	 * <br>
	 * 缩放图片(压缩图片质量,改变图片尺寸)
	 *
	 * @param bufferedImage {@link java.awt.image.BufferedImage}
	 * @param maxWidth 图片最大宽度,如何值为 0,则设置默认值 {@link GraphicsRedraw#DEFAULT_WIDTH}
	 * @param maxHeight 图片最大高度,如何值为 0,则设置默认值 {@link GraphicsRedraw#DEFAULT_HEIGHT}
	 * @param quality 图片质量,如0.7f 相当于70%质量,如何值为 0,则设置默认值 {@link GraphicsRedraw#DEFAULT_QUALITY}
	 * @return {@link java.io.InputStream}
	 * @author MinGRn <br > MinGRn97@gmail.com
	 */
	InputStream redraw(BufferedImage bufferedImage, int maxWidth, int maxHeight, float quality);
}