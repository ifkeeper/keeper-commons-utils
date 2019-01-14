package com.mingrn.keeper.utils.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Request 工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 */
public final class RequestUtils {

	private RequestUtils() {
	}

	private static final String UNKNOWN_IP = "unknown";

	/**
	 * 请求头代理
	 */
	private static final String[] PROXY_IP_HEADER_NAMES = new String[]{"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP"};


	/**
	 * 获取请求路径
	 *
	 * @param request {@link HttpServletRequest}
	 */
	public static String getRequestURIWithoutContextPath(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (requestUri.startsWith(contextPath)) {
			requestUri = requestUri.substring(contextPath.length());
		}

		return requestUri;
	}


	public static String getRequestURIWithQueryString(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder(getRequestURIWithoutContextPath(request));
		String queryString = request.getQueryString();
		if (StringUtils.isNotEmpty(queryString)) {
			sb.append("?").append(queryString);
		}

		return sb.toString();
	}


	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 * 如:X-Forwarded-For:192.168.1.110, 192.168.1.120, 192.168.1.130,
	 * 192.168.1.100
	 * 用户真实IP为: 192.168.1.110
	 *
	 * @param request {@link HttpServletRequest}
	 */
	public static String getClientIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (String strIp : ips) {
				if (!UNKNOWN_IP.equalsIgnoreCase(strIp)) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}


	/**
	 * 获取请求信息
	 *
	 * @param request {@link HttpServletRequest}
	 */
	public static String getRequestInfo(HttpServletRequest request) {
		return getRequestInfo(request, null);
	}


	public static String getRequestInfo(HttpServletRequest request, String[] hiddenParams) {
		StringBuilder sb = new StringBuilder();
		sb.append(request.getMethod()).append(" ");
		sb.append(request.getRequestURI());
		if (StringUtils.isNotEmpty(request.getQueryString())) {
			sb.append("?").append(request.getQueryString());
		}

		sb.append("\n");
		String forwardUri = (String) request.getAttribute("javax.servlet.forward.request_uri");
		String includeUri, referer;
		if (StringUtils.isNotEmpty(forwardUri)) {
			sb.append("Forward From: ").append(forwardUri);
			includeUri = (String) request.getAttribute("javax.servlet.forward.query_string");
			if (StringUtils.isNotEmpty(includeUri)) {
				sb.append("?").append(includeUri);
			}

			referer = (String) request.getAttribute("javax.servlet.forward.path_info");
			if (StringUtils.isNotEmpty(referer)) {
				sb.append(", Path Info: ").append(referer);
			}

			sb.append("\n");
		}

		includeUri = (String) request.getAttribute("javax.servlet.include.request_uri");
		if (StringUtils.isNotEmpty(includeUri)) {
			sb.append("Include From: ").append(includeUri);
			referer = (String) request.getAttribute("javax.servlet.include.query_string");
			if (StringUtils.isNotEmpty(referer)) {
				sb.append("?").append(referer);
			}

			String includePathInfo = (String) request.getAttribute("javax.servlet.include.path_info");
			if (StringUtils.isNotEmpty(includePathInfo)) {
				sb.append(", Path Info: ").append(includePathInfo);
			}

			sb.append("\n");
		}

		sb.append("Remote IP Address: ").append(getClientIpAddress(request)).append("\n");
		sb.append("User-Agent: ").append(request.getHeader("User-Agent")).append("\n");
		referer = request.getHeader("Referer");
		if (StringUtils.isNotEmpty(referer)) {
			sb.append("Referer: ").append(referer).append("\n");
		}

		sb.append("Parameters: ");
		boolean first = true;
		for (Object object : request.getParameterMap().keySet()) {
			String key = object.toString();
			if (!first) {
				sb.append("&");
			} else {
				first = false;
			}

			sb.append(key).append("=");
			boolean hidden = false;
			if (hiddenParams != null) {
				for (String hiddenParam : hiddenParams) {
					if (key.equals(hiddenParam)) {
						hidden = true;
						break;
					}
				}
			}

			if (!hidden) {
				sb.append(StringUtils.join(request.getParameterValues(key), ","));
			} else {
				sb.append("******");
			}
		}

		return sb.toString();
	}


	/**
	 * 替换参数值
	 *
	 * @param paramName 参数名称
	 * @param paramValue 参数值
	 */
	public static String replaceParamValue(String url, String paramName, String paramValue) {
		StringBuilder sb = new StringBuilder();
		String str = paramName + "=";
		if (url.contains("?")) {
			int idx = url.indexOf(str);
			if (idx != -1) {
				int ampIdx = url.indexOf("&", idx);
				if (ampIdx != -1) {
					sb.append(url.substring(0, idx));
					if (paramValue != null) {
						sb.append(str).append(paramValue).append(url.substring(ampIdx));
					} else {
						sb.append(url.substring(ampIdx + 1));
					}
				} else if (paramValue != null) {
					sb.append(url.substring(0, idx)).append(str).append(paramValue);
				} else {
					sb.append(url.substring(0, idx - 1));
				}
			} else {
				sb.append(url);
				if (paramValue != null) {
					sb.append("&").append(str).append(paramValue);
				}
			}
		} else {
			sb.append(url);
			if (paramValue != null) {
				sb.append("?").append(str).append(paramValue);
			}
		}

		return sb.toString();
	}
}