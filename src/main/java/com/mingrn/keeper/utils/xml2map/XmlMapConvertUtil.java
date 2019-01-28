package com.mingrn.keeper.utils.xml2map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * XML Map 转换工具
 *
 * @author MinGRn
 */
public final class XmlMapConvertUtil {

	private XmlMapConvertUtil() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(XmlMapConvertUtil.class);

	/**
	 * 解析 HttpServletRequest 请求 XML 流数据,封装成Map
	 * 从 HttpServletRequest 中读取 InputStream(该输入流是一个XML对象)
	 * 通过使用 org.dom4j.SAXReader.read() api 读取输入流,封装成 org.dom4j.Document
	 * 文档对象.通过获取文档对象的根节点,获取该根节点的全部子节点。进行遍历,封装进入Map集合.
	 * 注:需要引入 org.dom4j.dom4j API
	 *
	 * @param request javax.servlet.http.HttpServletRequest
	 * @return java.util.Map
	 * @since 1.7
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml2Map(HttpServletRequest request) {

		Map<String, String> params = new LinkedHashMap<>(8);
		try (InputStream inputStream = request.getInputStream()) {
			Document document = new SAXReader().read(inputStream);
			Element rootElement = document.getRootElement();
			List<Element> elementList = rootElement.elements();
			for (Element element : elementList) {
				params.put(element.getName(), element.getText());
			}
		} catch (IOException e) {
			LOGGER.error("MessageUtil.parseXml IOException >>", e);
		} catch (DocumentException e) {
			LOGGER.error("MessageUtil.parseXml DocumentException >>", e);
		}

		return params;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseXml2Map(String xml) {
		try {
			Document document = new SAXReader().read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			Element rootElement = document.getRootElement();
			List<Element> elementList = rootElement.elements();
			return getElements(elementList);
		} catch (Exception e) {
			LOGGER.error("MessageUtil.parseXml IOException >>", e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> getElements(List<Element> elementList) {
		Map<String, Object> map = new LinkedHashMap<>(8);
		for (Element element : elementList) {
			List<Element> list = element.elements();
			if (list.size() > 0) {
				Map<String, Object> eMap = getElements(list);
				map.put(element.getName(), eMap);
			} else {
				map.put(element.getName(), element.getText());
			}
		}
		return map;
	}
}