package com.mingrn.itumate.commons.utils.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;
import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.lang3.StringUtils;

/**
 * PDF文件关键字所在坐标位置获取工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/7/5 16:57
 */
public class PdfKeyWordUtil {

    private static List getKeyWordsPosition(String filePath, final String keyword) {
        try {
            PdfReader pdfReader = new PdfReader(filePath);
            return parsingPdfReader(pdfReader, keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    private static List getKeyWordsPosition(final byte[] pdfByte, final String keyword) {
        try {
            PdfReader pdfReader = new PdfReader(pdfByte);
            return parsingPdfReader(pdfReader, keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    private static List getKeyWordsPosition(final InputStream in, final String keyword) {
        try {
            PdfReader pdfReader = new PdfReader(in);
            return parsingPdfReader(pdfReader, keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    private static List getKeyWordsPosition(final URL url, final String keyword) {
        try {
            PdfReader pdfReader = new PdfReader(url);
            return parsingPdfReader(pdfReader, keyword);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    private static List parsingPdfReader(PdfReader pdfReader, final String keyword) {
        // 获取总页数
        int pageNum = pdfReader.getNumberOfPages();

        // 内容解析
        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);

        List info = new TreeList();
        for (int page = 1; page < pageNum; page++) {
            Map<String, java.lang.Float> positionMap = getKeyWordsPositionByPage(page, keyword, pdfReaderContentParser);
            java.lang.Float[] position = {positionMap.get("x"), positionMap.get("y")};
            info.add(position);
        }
        return info;
    }


    public static Map<String, java.lang.Float> getKeyWordsPositionByPage(int pageNum, final String keyword, PdfReaderContentParser pdfReaderContentParser) {

        if (pageNum < 1) {
            throw new IllegalArgumentException("Page Number Is negative Or Zero");
        }

        Map<String, java.lang.Float> position = new HashMap<>(2);

        try {
            pdfReaderContentParser.processContent(pageNum, new RenderListener() {
                @Override
                public void beginTextBlock() {
                }

                @Override
                public void renderText(TextRenderInfo renderInfo) {
                    // 渲染 PDF 文本内容
                    // 获取本页文本
                    String text = renderInfo.getText();

                    // 获取关键字位置
                    if (StringUtils.isNotBlank(text) && text.contains(keyword)) {
                        // 关键字文本所在行信息
                        LineSegment lineSegment = renderInfo.getBaseline();
                        // 文本行边界信息
                        Rectangle2D.Float boundingRectange = lineSegment.getBoundingRectange();
                        // 关键字位置信息
                        position.put("x", boundingRectange.x);
                        position.put("y", boundingRectange.y);
                    }
                }

                @Override
                public void endTextBlock() {
                }

                @Override
                public void renderImage(ImageRenderInfo renderInfo) {
                    // 渲染 PDF 图片
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return position;
    }

}