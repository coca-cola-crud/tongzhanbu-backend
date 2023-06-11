package com.shu.tongzhanbu.component.util;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shu.tongzhanbu.admindb.entity.Member;
import com.shu.tongzhanbu.component.util.constance.Constants;
import com.shu.tongzhanbu.security.config.SecurityProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

/**
*@描述 同步人事处数据
*@创建人 dyj
*@创建时间 2022/11/3
**/

public class TongbuUtil {
    public static String createJwt(){
        String appId = "21wL392i20II0zYD2f03jD223fiL7L68";// 申请到的APPID f1Pm1ekcxfDWd3T6r7dluV-mfEi_Hh7f
        String secret = "8O3lcVSj2WUbxZRJcUzQaP7LbD6QRxzA";// 申请到的appSecret
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512; // 加密算法
        Key signingKey = new SecretKeySpec(secret.getBytes(), signatureAlgorithm.getJcaName());

        Date date = new Date();
        long time = date.getTime();
        long expMillis = time + 10 * 60 * 1000; //超时时间
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(date)
                .setIssuer(appId)
                .setNotBefore(date)
                .setExpiration(new Date(expMillis))
                .signWith(signatureAlgorithm, signingKey);
        builder.setHeaderParam("typ", "JWT"); // 在io.jsonwebtoken工具包中，需要手动设置头为JWT
        String jwt = builder.compact(); // 生成JWT
        return jwt;
    }

    public static JSONObject getName(String gonghao,String jwt){
        if(jwt.equals("")){
            jwt  = createJwt();
        }
        RestTemplate r = new RestTemplate();
        String url = "https://dataset.shu.edu.cn/rest/v1/userinfo/basic/"+gonghao;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",jwt);
        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = r.exchange(url, HttpMethod.GET,httpEntity,String.class);
//        System.out.println(responseEntity.getBody());
        String respond = responseEntity.getBody();
        JSONObject resObj = JSONObject.parseObject(respond);
        return resObj;
    }

    public static String getImage(String gonghao){
        RestTemplate r = new RestTemplate();
        String url = "http://10.10.0.84/api/commonApi/getPicture?number="+gonghao;
        String result = r.getForObject(url,String.class);
//        System.out.println(result);
        return result;

    }

    public static JSONObject getMoreInfo(String gonghao,String jwt){
        if(jwt.equals("")){
            jwt  = createJwt();
        }
        RestTemplate r = new RestTemplate();
        String url = "https://dataset.shu.edu.cn/rest/v1/identity/user/info/"+gonghao;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",jwt);
        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = r.exchange(url, HttpMethod.GET,httpEntity,String.class);
//        System.out.println(responseEntity.getBody());
        String respond = responseEntity.getBody();
        JSONObject resObj = JSONObject.parseObject(respond).getJSONObject("Data");
//        System.out.println(resObj);
        return resObj;
    }

    public static HashMap<String, String> getInfofromRsc(String gonghao){
        try{
            String jwt  = createJwt();
//            System.out.println(jwt);
            RestTemplate r = new RestTemplate();
            String url = "https://dataset.shu.edu.cn/rest/v1/identity/user/roles/"+gonghao;
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization",jwt);
            HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(null, headers);
            ResponseEntity<String> responseEntity = r.exchange(url, HttpMethod.GET,httpEntity,String.class);
            String respond = responseEntity.getBody();
            JSONObject resObj = JSONObject.parseObject(respond);
            HashMap<String,String> map = new HashMap<>();
            JSONArray infoArray = resObj.getJSONArray("Data");
            for(int i = 0;i<infoArray.size();i++){
                if(Constants.rscValuemap.get(infoArray.getJSONObject(i).getString("type"))!=null){
                    String key = Constants.rscValuemap.get(infoArray.getJSONObject(i).getString("type"));
                    if(map.get(key)!=null){
                        map.put(key,map.get(key)+","+infoArray.getJSONObject(i).getString("name"));
                    }else {
                        map.put(key,infoArray.getJSONObject(i).getString("name"));
                    }

                }

            }
            JSONObject moreInfo = getMoreInfo(gonghao,jwt);
            map.put("gonghao",moreInfo.getString("Username"));
            map.put("xingming",moreInfo.getString("RealName"));
            map.put("gender",moreInfo.getString("Sex"));
            map.put("yuangongzu",moreInfo.getString("Status"));
            if(!moreInfo.getString("WorkStart").equals("")){
                map.put("joinworkyearmonth",String.format("%s.%s",moreInfo.getString("WorkStart").substring(0,4),moreInfo.getString("WorkStart").substring(4,6)));
            }
            if(!moreInfo.getString("BirthDate").equals("")){
                map.put("birthday",String.format("%s.%s",moreInfo.getString("BirthDate").substring(0,4),moreInfo.getString("BirthDate").substring(4,6)));
            }

            map.put("picture",getImage(gonghao));
//            map.put("email",moreInfo.getString("Email"));
//            map.put("card",moreInfo.getString("IdentityNO"));
//            map.put("hujiaddress",moreInfo.getString("ResidentPlace"));
//            map.put("juzhuaddress",moreInfo.getString("LivingPlace"));
            return map;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static void main(String[] args){
        getInfofromRsc("90000105");
    }
}
