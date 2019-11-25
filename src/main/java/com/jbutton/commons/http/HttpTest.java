package main.java.com.jbutton.commons.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author lee
 */
public class HttpTest {
    public static void main(String[] args) {

        Properties properties = new Properties();
        InputStream resourceAsStream = HttpTest.class.getResourceAsStream("url.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String token = args[0];
        if (token == null || "".equals(token)) {
            System.out.println("命令行参数设置token");
            System.exit(0);
        }
        String ACCESS_TOKEN = "Bearer " + token;
        int i = 0;
        ArrayList<String> strings = new ArrayList<>();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            i++;
            strings.add(enumeration.nextElement().toString());
        }

        for (String string : strings) {

            new Thread(() -> {
                String url = properties.get(string).toString();
                System.out.println("开始请求----》" + url);
                String re = doGet(url, ACCESS_TOKEN);
                JSONObject jsonObject = JSONObject.parseObject(re);
                System.out.println("返回结果" + jsonObject);
            }).start();
        }
    }

    public static String doGetReal(String url, Map<String, String> head) throws IOException {
        HttpGet request = new HttpGet(url);
        setHead(request, head);
        return execute(request);
    }

    private static void setHead(HttpRequestBase requestBase, Map<String, String> head) {
        Set<String> keySet = head.keySet();
        for (String key : keySet) {
            requestBase.setHeader(key, head.get(key));
        }
    }

    private static String execute(HttpUriRequest post) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity(), "UTF-8");
                return strResult;
            } else {
                throw new IOException("发送请求异常: " + EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String doGet(String url, String ACCESS_TOKEN) {
        Map<String, String> head = null;
        try {
            if (StringUtils.isBlank(ACCESS_TOKEN)) {
                System.out.println("Token为空");
            }
            head = getHead(ACCESS_TOKEN);
            System.out.println("请求参数url--->" + url);
            System.out.println("请求参数head--->" + head);
            String re = doGetReal(url, head);
            JSONObject jsonObject = JSONObject.parseObject(re);
            String code = jsonObject.getString("code");
            if ("0".equals(code)) {
                return jsonObject.getString("data");
            } else {
                System.out.println("返回结果:" + jsonObject.toJSONString());
                throw new RuntimeException("发送请求异常: " + code);
            }
        } catch (Exception e) {
            System.out.println("请求异常的url-------------》" + url);
            System.out.println("请求异常的header-------------》{}" + JSONObject.toJSONString(head));
            throw new RuntimeException("发送请求异常: " + e.getMessage());
        }
    }

    private static Map<String, String> getHead(String authorization) {
        Map<String, String> head = new HashMap<>();
        head.put("Authorization", authorization);
        head.put("Content-Type", "application/x-www-form-urlencoded");
        return head;
    }


}


