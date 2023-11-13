package com.wbu.train.getway.utile;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JwtUtil.class);
    private static final String key="zzb12306";
    public static String createToken(Long id, String mobile){
        DateTime now = DateTime.now();
        DateTime expTime = now.offsetNew(DateField.HOUR, 24);
        Map<String, Object> payload = new HashMap<>();
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT, expTime);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        // 内容
        payload.put("id", id);
        payload.put("mobile", mobile);
        String token = JWTUtil.createToken(payload, key.getBytes());
        LOG.info("生成JWT token：{}", token);
        return token;
    }
    public static boolean validate(String token) {
        try{
            JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
            // validate包含了verify
            boolean validate = jwt.validate(0);
            LOG.info("JWT token校验结果：{}", validate);
            return validate;
        }catch (Exception e){
            LOG.warn("token校验异常{}",e);
            return false;
        }
    }
    public static JSONObject getJSONObject(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(key.getBytes());
        JSONObject payloads = jwt.getPayloads();
        payloads.remove(JWTPayload.ISSUED_AT);
        payloads.remove(JWTPayload.EXPIRES_AT);
        payloads.remove(JWTPayload.NOT_BEFORE);
        LOG.info("根据token获取原始内容：{}", payloads);
        return payloads;
    }
    public static void main(String[] args) {
        createToken(1L, "123");

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE2OTk4NTE2NDcsIm1vYmlsZSI6IjEyMyIsImlkIjoxLCJleHAiOjE2OTk5MzgwNDcsImlhdCI6MTY5OTg1MTY0N30.sziGVr510eo3btKFmPJ8yLOFpTLdzdiR7loAvGzk53c";
        validate(token);
        validate("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE2OTk4NTQ0MzYsIm1vYmlsZSI6IjE2NjA3MjExNTAzIiwiaWQiOjIzLCJleHAiOjE2OTk5NDA4MzYsImlhdCI6MTY5OTg1NDQzNn0.dacVeY6mYPbA47cDMhBQpv0N_DKUxtZPzV7NOONebI8");
        getJSONObject("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE2OTk4NTU0MDQsIm1vYmlsZSI6IjE2NjA3MjExNTAzIiwiaWQiOjE3MjI4Njc1MDgzNzkyNDI0OTcsImV4cCI6MTY5OTk0MTgwNCwiaWF0IjoxNjk5ODU1NDA0fQ.9CG1FxUEP8dDRNf_j6MpvbS_47ndrIl-eV2FtjI5mzU");
        getJSONObject(token);
    }
}
