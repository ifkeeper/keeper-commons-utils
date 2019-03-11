package com.mingrn.itumate.commons.utils.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie 工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 */
public final class CookieUtils {

	private CookieUtils() {
	}

	/**
	 * 获取 Cookie
	 *
	 * @param request {@link HttpServletRequest}
	 * @param name Cookie Key
	 * @return {@link Cookie}
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}

		return null;
	}

	/**
	 * 增加 Cookie
	 *
	 * @param response {@link HttpServletResponse}
	 * @param domain 服务器地址,如 127.0.0.1:8080
	 * @param path 路径
	 * @param name Cookie 名称
	 * @param value 值
	 */
	public static void addCookie(HttpServletResponse response, String domain, String path, String name, String value) {
		addCookie(response, domain, path, name, value, -1);
	}

	/**
	 * @param response {@link HttpServletResponse}
	 * @param domain 服务器地址,如 127.0.0.1:8080
	 * @param path 路径
	 * @param name key
	 * @param value 值
	 * @param maxAge 存活时长
	 */
	public static void addCookie(HttpServletResponse response, String domain, String path, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		if (domain.contains(":")) {
			domain = domain.split(":")[0];
		}

		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	/**
	 * 删除 Cookie
	 *
	 * @param response {@link HttpServletResponse}
	 * @param domain 服务器地址,如 127.0.0.1:8080
	 * @param path 路径
	 * @param name Cookie 名称
	 */
	public static void removeCookie(HttpServletResponse response, String domain, String path, String name) {
		addCookie(response, domain, path, name, null, 0);
	}
}