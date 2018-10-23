package com.mentor.pte.file.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mentor.pte.tools.HttpClientTool;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    private static final String UPLOADED_FOLDER = "D:/files";

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,String text) {

        if (file.isEmpty()) {
            return "error";
        }
        try {
            // Get the file and save it somewhere
            String filePath = UPLOADED_FOLDER + "/" + UUID.randomUUID().toString() + ".wav";
            Path path = Paths.get(filePath);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            if (StringUtils.isEmpty(text)) {
                text = "Where are you form ?";
            }
            String result = sendToXF(filePath,text);
            System.out.println("返回结果："+result);
            return dealWithResponse(result);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ok";
    }

    private String sendToXF(String filePath, String text) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("aue", "raw");
        //jsonObject.addProperty("speex_size", "70");
        jsonObject.addProperty("result_level", "entirety");
        jsonObject.addProperty("language", "en_us");
        jsonObject.addProperty("category", "read_chapter");
        jsonObject.addProperty("extra_ability","multi_dimension");
        String json = jsonObject.toString();
        String param64 =new String(Base64.encodeBase64(json.getBytes("UTF-8")));
        String url = "http://api.xfyun.cn/v1/service/v1/ise";
        //将音频文件转换成Base64
        InputStream is = new FileInputStream(filePath);
        byte[] bytes = IOUtils.toByteArray(is);
        String audio64=Base64.encodeBase64String(bytes);
        Map<String, String> map = new HashMap<>();
        map.put("audio", audio64);
        map.put("text", text);
        String body = HttpClientTool.sendXFPost(url, param64, map);
       // System.out.println("响应结果：" + body);
        return body;
    }

    private String dealWithResponse(String result) {
        JSONObject json = JSONObject.parseObject(result);
        if (!"0".equals(json.get("code"))) {
            return json.get("desc").toString();
        }
        JSONObject data = (JSONObject) json.get("data");
        JSONObject read_sentence = (JSONObject) data.get("read_chapter");
        JSONObject rec_paper = (JSONObject) read_sentence.get("rec_paper");
        JSONObject read_chapter = (JSONObject) rec_paper.get("read_chapter");
        String score= read_chapter.getString("total_score");
        String accuracy="；准确度："+ read_chapter.getString("accuracy_score");
        String fluency="；流利度："+ read_chapter.getString("fluency_score");
        String integrity="；完整度："+ read_chapter.getString("integrity_score");
        return score.substring(0, score.indexOf("."))+accuracy+fluency+integrity;
    }
}
