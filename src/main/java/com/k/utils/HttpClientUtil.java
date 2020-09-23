package com.k.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpClientUtil请求工具类
 *
 * @author 王一鸣
 */
public final class HttpClientUtil {
    /**
     * 日志类
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
    /**
     * 请求url
     */
    private String url;
    /**
     * 参数列表
     */
    private Map<String, String> params;
    /**
     * 字符集 默认是:utf-8
     */
    private String charSet;

    public static HttpClientUtil getInstance(String url) {
        return new HttpClientUtil(url, "utf-8");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    private HttpClientUtil() {
    }

    private HttpClientUtil(String url, String charSet) {
        this.charSet = charSet;
        this.url = url;
        this.params = new HashMap<>(4);
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    /**
     * 实体类发送get请求的方法
     *
     * @return String
     */
    public String sendGet() {

        StringBuilder sb = new StringBuilder();
        this.params
                .entrySet()
                .forEach(
                        entry -> {
                            sb.append(entry.getKey());
                            sb.append("=");
                            sb.append(entry.getValue());
                            sb.append("&");
                        });
        LOGGER.info(this.url);
        LOGGER.info(sb.toString());
        return sendGet(this.url, sb.toString(), this.charSet);
    }

    /**
     * 实体类发送get请求的方法
     *
     * @return String
     */
    public String sendPost() {
        StringBuilder sb = new StringBuilder();
        this.params
                .entrySet()
                .forEach(
                        entry -> {
                            sb.append(entry.getKey());
                            sb.append("=");
                            sb.append(entry.getValue());
                            sb.append("&");
                        });
        LOGGER.info(this.url);
        LOGGER.info(sb.toString());
        return sendPost(this.url, sb.toString());
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String charSet) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty(
                    "user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charSet));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            // 使用finally块来关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty(
                    "user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        } finally {
            // 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}

