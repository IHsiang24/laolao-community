package com.xiangkai.community.service;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * 接入AI服务
 */
@Service
public class AIService {

    @Value("${com.baidu.yiyan.api-key}")
    private String apiKey;

    @Value("${com.baidu.yiyan.secret-key}")
    private String secretKey;

    private static final String ACCESS_TOKEN_KEY = "access_token";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);
        server.createContext("/eb_stream", new StreamHandler("LvaKu1Fd2wqHRb0Etu7KEwzy", "8C5rJRxingkzZUpY5GwlPisV4LgBm7gl"));
        server.setExecutor(null);
        server.start();
    }

    private static class StreamHandler implements HttpHandler {

        private String apiKey;

        private String secretKey;

        public StreamHandler(String apiKey, String secretKey) {
            this.apiKey = apiKey;
            this.secretKey = secretKey;
        }

        static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

        public String getAccessToken() throws IOException{

            String requestUrl = String.format("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=%s&client_secret=%s", apiKey, secretKey);

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.body() != null) {
                JSONObject object = JSONObject.parseObject(response.body().string());
                return object.getString(ACCESS_TOKEN_KEY);
            }
            return null;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 读取body中的prompt入参
            InputStreamReader bodyReader = new InputStreamReader(exchange.getRequestBody());
            BufferedReader br = new BufferedReader(bodyReader);
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                strBuilder.append(line);
            }
            br.close();
            JSONObject body = JSONObject.parseObject(strBuilder.toString());
            String prompt = body.getString("prompt");

            // 发起流式请求
            OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody ebRequestBody = RequestBody.create(mediaType, "{\"messages\":[{\"role\":\"user\",\"content\":\"" + prompt + "\"}],\"stream\":true}");
            String source = "&sourceVer=0.0.1&source=app_center&appName=streamDemo";
            // 大模型接口URL
            String baseUrl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-4.0-turbo-8k";
            Request request = new Request.Builder()
                    .url(baseUrl + "?access_token=" + getAccessToken() + source)
                    .method("POST", ebRequestBody)
                    .addHeader("Content-Type", "application/json")
                    .build();

            OutputStream outputStream = exchange.getResponseBody();

            // 允许跨域
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, 0);
            // 流式返回
            Response response = HTTP_CLIENT.newCall(request).execute();
            StringBuilder stringBuilder = new StringBuilder();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    InputStream inputStream = responseBody.byteStream();
                    StringBuilder rawData = new StringBuilder();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        String data = new String(buffer, 0, bytesRead);
                        String[] lines = data.split("\n");
                        for (String li : lines) {
                            if (li.startsWith("data: ")) {
                                if (!rawData.toString().equals("")) {
                                    outputStream.write(rawData.substring(6).getBytes());
                                    outputStream.flush();
                                }
                                rawData = new StringBuilder(li);
                            } else {
                                rawData.append(li);
                            }
                        }
                    }
                }
            } else {
                System.out.println("流式请求异常: " + response);
            }
            outputStream.close();
            exchange.close();
        }
    }
}
