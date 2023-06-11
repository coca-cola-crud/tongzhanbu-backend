//package com.shu.tongzhanbu;
//
//
//import com.alibaba.fastjson.JSONArray;
//import io.jsonwebtoken.JwtBuilder;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.crypto.spec.SecretKeySpec;
//import java.io.*;
//import java.security.Key;
//import java.util.Calendar;
//import java.util.Date;
//
//@SpringBootTest
//class TongzhanbuApplicationTests {
//    @Test
//    void contextLoads() {
//
//        try {
//            String appId = "21wL392i20II0zYD2f03jD223fiL7L68";// 申请到的APPID f1Pm1ekcxfDWd3T6r7dluV-mfEi_Hh7f
//            String secret = "8O3lcVSj2WUbxZRJcUzQaP7LbD6QRxzA";// 申请到的appSecret
//            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512; // 加密算法
//
//            Key signingKey = new SecretKeySpec(secret.getBytes(), signatureAlgorithm.getJcaName());
//            System.out.println(signingKey);
//            Date date = new Date();
//            long time = date.getTime();
//            long expMillis = time + 10 * 60 * 1000; //超时时间
//            JwtBuilder builder = Jwts.builder()
//                    .setIssuedAt(date)
//                    .setIssuer(appId)
//                    .setNotBefore(date)
//                    .setExpiration(new Date(expMillis))
//                    .signWith(signatureAlgorithm, signingKey);
//            builder.setHeaderParam("typ", "JWT"); // 在io.jsonwebtoken工具包中，需要手动设置头为JWT
//            String jwt = builder.compact(); // 生成JWT
//            System.out.println(jwt);
//            String url = "https://dataset.shu.edu.cn/rest/v1/identity/user/roles/10008948";
//            if(!doGet(url,jwt).equals("")) {
//
//                System.out.println( JSONArray.parseArray(doGet(url,jwt)).get(0));
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static String doGet(String url,String jwt) {
//        CloseableHttpClient httpClient = null;
//        CloseableHttpResponse response = null;
//        String result = "";
//        String department="";
//        try {
//            // 通过址默认配置创建一个httpClient实例
//            httpClient = HttpClients.createDefault();
//            // 创建httpGet远程连接实例
//            HttpGet httpGet = new HttpGet(url);
//            // 设置请求头信息，鉴权
//            httpGet.setHeader("Authorization",jwt);
//            // 设置配置请求参数
//            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
//                    .setConnectionRequestTimeout(35000)// 请求超时时间
//                    .setSocketTimeout(60000)// 数据读取超时时间
//                    .build();
//            // 为httpGet实例设置配置
//            httpGet.setConfig(requestConfig);
//            // 执行get请求得到返回对象
//            response = httpClient.execute(httpGet);
//            System.out.println(response);
//            // 通过返回对象获取返回数据
//            HttpEntity entity = response.getEntity();
//
//            // 通过EntityUtils中的toString方法将结果转换为字符串
//            result = EntityUtils.toString(entity);
//            System.out.println( result);
//
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭资源
//            if (null != response) {
//                try {
//                    response.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (null != httpClient) {
//                try {
//                    httpClient.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return department;
//    }
//
//}
