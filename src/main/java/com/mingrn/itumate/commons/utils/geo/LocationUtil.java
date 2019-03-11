package com.mingrn.itumate.commons.utils.geo;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

/**
 * 明文位置获取经纬度点信息{@link #distanceBetweenLocation(Double, Double, Double, Double)}
 * </p>
 * 发送 GET 请求借助 httpclient,需要在 POM 中引入:
 * <a href="http://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient"></a>
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 01/10/2018 15:17
 */
public class LocationUtil {

	private LocationUtil() {
	}

	/**
	 * 百度获取位置经纬度 URL
	 */
	private static final String LNG_LAT_POINT_URL = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=23LgQFlojlfahUpIxZG5Qvif&address=";

	/**
	 * 百度Map距离求取 URL
	 */
	private static final String WAY_POINTS_DISTANCE_URL = "http://api.map.baidu.com/telematics/v3/distance?output=json&ak=RguGdBfvanKG10lrLHtUAtka&waypoints=";


	/**
	 * 测试用例
	 */
	public static void main(String[] args) {
		getLngAndLat("上海市徐汇区零陵小区");
		distanceBetweenLocation(121.455244, 31.234076, 121.488301, 31.237534);
	}


	/**
	 * 获取具体位置的经纬度
	 * </p>
	 * 如: location = 上海市徐汇区零陵小区
	 * result:{"status":0,"result":{"location":{"lng":121.45078363819293,"lat":31.193489388753848},"precise":1,"confidence":70,"comprehension":100,"level":"地产小区"}}
	 *
	 * @param location 地理位置
	 */
	public static JSONObject getLngAndLat(String location) {
		location = location.length() > 35 ? location.substring(0, 35) : location;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpUriRequest uriRequest = RequestBuilder.get(LNG_LAT_POINT_URL + URLEncoder.encode(location, "UTF-8")).build();
			return JSONObject.parseObject(result(httpClient, uriRequest));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * 计算两个坐标点的距离
	 * </p>
	 * 仅供百度API调用参考,具体使用 {@link DistanceUtil}
	 *
	 * @param startLng 起点经度
	 * @param startLat 起点纬度
	 * @param endLng 终点经度
	 * @param endLat 终点纬度
	 * @return 距离（米）
	 */
	private static void distanceBetweenLocation(Double startLng, Double startLat, Double endLng, Double endLat) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			StringBuilder builderPoint = new StringBuilder(String.valueOf(startLng))
					.append(",").append(startLat).append(";").append(endLng).append(",").append(endLat);
			HttpUriRequest uriRequest = RequestBuilder.get(WAY_POINTS_DISTANCE_URL + builderPoint).build();
			result(httpClient, uriRequest);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String result(CloseableHttpClient httpClient, HttpUriRequest uriRequest) {
		StringBuilder stringBuilder = new StringBuilder();
		try (InputStream inputStream = httpClient.execute(uriRequest).getEntity().getContent()) {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line);
			}
			return stringBuilder.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}