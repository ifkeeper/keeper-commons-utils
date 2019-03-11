package com.mingrn.itumate.commons.utils.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 * Response 工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 */
public final class ResponseUtils {

	private ResponseUtils() {
	}

	/**
	 * 回写 JSON Object
	 *
	 * @param response {@link HttpServletResponse}
	 * @param object 值
	 */
	public static void writeJsonObject(HttpServletResponse response, Object object) {
		writeJsonObject(response, object, null);
	}

	public static void writeJsonObject(HttpServletResponse response, Object object, String jsonCallback) {
		String jsonStr = "{}";
		if (object != null) {
			jsonStr = JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
		}

		StringBuilder sb = new StringBuilder();
		if (jsonCallback != null) {
			sb.append(jsonCallback).append("(").append(jsonStr).append(")");
		} else {
			sb.append(jsonStr);
		}

		writeJson(response, sb.toString());
	}

	public static void writeJsonArray(HttpServletResponse response, Object object) {
		writeJsonArray(response, object, (String) null);
	}

	public static void writeJsonArray(HttpServletResponse response, Object object, String jsoncallback) {
		String jsonStr = "[]";
		if (object != null) {
			jsonStr = JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
		}

		StringBuilder sb = new StringBuilder();
		if (jsoncallback != null) {
			sb.append(jsoncallback).append("(").append(jsonStr).append(")");
		} else {
			sb.append(jsonStr);
		}

		writeJson(response, sb.toString());
	}

	public static void writeJson(HttpServletResponse response, String jsonStr) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, sid");
		write(response, "application/json; charset=UTF-8", jsonStr);
	}

	public static void writeText(HttpServletResponse response, String text) {
		write(response, "text/plain; charset=UTF-8", text);
	}

	private static void write(HttpServletResponse response, String contentType, String s) {
		response.setContentType(contentType);
		try (PrintWriter out = response.getWriter()) {
			out.write(s);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}