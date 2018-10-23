package com.mentor.pte.tools;

import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientTool {

    private static String APP_ID = "5bb5574f";
    private static String APP_KEY = "a2e58a4520ab7e0991e558efca6f4f76";

    /**
     * post请求传输map数据
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String sendXFPost(String url, String base64Param, Map<String, String> map) throws ClientProtocolException, IOException {
        String result = "";

        // 创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        // 设置header信息
        // 指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
        String curTime = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Shanghai")).toEpochSecond(ZoneOffset.of("+8")));
//        System.out.println(System.currentTimeMillis());
//        String curTime =System.currentTimeMillis()/ 1000L + "";
        String checkSum = DigestUtils.md5Hex((APP_KEY + curTime + base64Param).getBytes());
        httpPost.setHeader("X-Appid", APP_ID);
        httpPost.setHeader("X-CurTime", curTime);
        httpPost.setHeader("X-Param", base64Param);
        httpPost.setHeader("X-CheckSum", checkSum);

        // 装填参数
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        // 设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));


        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // 获取结果实体
        // 判断网络连接状态码是否正常(0--200都数正常)
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        }
        // 释放链接
        response.close();

        return result;
    }


    public static void main(String[] args) throws IOException {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("aue", "raw");
        //jsonObject.addProperty("speex_size", "70");
        jsonObject.addProperty("result_level", "entirety");
        jsonObject.addProperty("language", "en_us");
        jsonObject.addProperty("category", "read_chapter");
        jsonObject.addProperty("extra_ability", "multi_dimension");
        String json = jsonObject.toString();
        System.out.println(json);

        String param64 = new String(Base64.encodeBase64(json.getBytes("UTF-8")));
        System.out.println(param64);
        String url = "http://api.xfyun.cn/v1/service/v1/ise";

        //将音频文件转换成Base64
        String filePath = "D:/output03.wav";
        InputStream is = new FileInputStream(filePath);
        byte[] bytes = IOUtils.toByteArray(is);
        String audio64 = Base64.encodeBase64String(bytes);
        String text64 = "Domestication is an evolutionary, rather than a political development. They were more likely to survive and prosper in an alliance with humans than on their own. Humans provided the animals with food and protection, in exchange for which the animals provided the humans their milk and eggs and -- yes--their flesh.";


        Map<String, String> map = new HashMap<>();
        map.put("audio", audio64);
        map.put("text", text64);


        String body = sendXFPost(url, param64, map);
        System.out.println("响应结果：" + body);
    }


}
