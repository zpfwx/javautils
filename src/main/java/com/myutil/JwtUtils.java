package com.myutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.geom.transform.Identity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Base64;

/**
 * @author pfzhao
 * @title: JwtUtils
 * @projectName myUtil
 * @description: TODO
 * @date 2023/5/30 15:27
 */
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
    private String secret;

    private static final String oldSecret = "abcdefg1234567";

    public JwtUtils() {
    }

    public static <T> String createToken(Map<String,Object> identity, String secret, Long expire) {
        return createToken(secret, expire, (String)null, identity);
    }

    public static <T> String createTokenWithFlag(Map<String,Object> identity, String flagValue, String secret, Long expire) {
        return createToken(secret, expire, flagValue, identity);
    }

    public static <T> String createToken(String secret, Long expire, String flagValue, Map<String,Object> identities) {
        JwtBuilder jwtBuilder = getJwtBuilder(secret, expire, flagValue, identities);
        String token = jwtBuilder.compact();
        return token;
    }

    public static <T> String createToken(String secret, Long expire, String flagValue, Map<String, Object> claims,  Map<String,Object>  identities) {
        JwtBuilder jwtBuilder = getJwtBuilder(secret, expire, flagValue, identities);
        if (claims != null) {
            claims.forEach((key, value) -> {
                jwtBuilder.claim(key, value);
            });
        }

        String token = jwtBuilder.compact();
        return token;
    }

    private static JwtBuilder getJwtBuilder(String secret, Long expire, String flagValue, Map<String,Object> paramMap) {
        Map<String, Object> subMap =  new HashMap<>();
        for(Map.Entry entry: paramMap.entrySet()) {
            subMap.put(entry.getKey().toString(), entry.getValue());
        }

        String subject = JSON.toJSONString(subMap);
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000L);
        JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(subject).setIssuedAt(nowDate).setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes(Charsets.UTF_8)));
        if (flagValue != null) {
            jwtBuilder.claim("TOKEN-FLAG", flagValue);
        }

        log.info("create a token with subject <{}> , will expire in {} seconds.", subject, expire);
        return jwtBuilder;
    }

    public static String createJwt(String secret, Map<String, Object> payload) {
        return Jwts.builder().setHeaderParam("typ", "JWT").setClaims(payload).signWith(SignatureAlgorithm.HS256, secret.getBytes(Charsets.UTF_8)).compact();
    }

    public static Claims validToken(String secret, String token) {
        return (Claims)Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes(Charsets.UTF_8))).parseClaimsJws(token).getBody();
    }


    /**
     * 验证token是否过期失效
     * @param token
     * @return
     */
    public boolean isTokenExpired (String token) {
        boolean isExpired = false;
        try{
            isExpired = getExpirationDateFromToken(token).after(new Date());
        }catch (Exception e){
            log.error("Token expire time check failed",e);
        }
        return isExpired;
    }

    /**
     * 获取token中注册信息
     * @param token
     * @return
     */
    public Claims getTokenClaim (String token) {
        try{
            return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8))).parseClaimsJws(token).getBody();
        } catch (Exception e){
            return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(oldSecret.getBytes(StandardCharsets.UTF_8))).parseClaimsJws(token).getBody();
        }
    }

    /**
     * 获取token失效时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getTokenClaim(token).getExpiration();
    }

    /**
     * 从token中解析userId
     * @param token
     * @return
     */
    public String getUserIdFromToken(String token) throws Exception{
        Claims claims = getTokenClaim(token);
        //String subject = StringUtils.contains(claims.getSubject(), ":") ? claims.getSubject() : StringUtils.replace(claims.getSubject(), "=", ":");
        String subject ="";
        return JSONObject.parseObject(subject).getString("userId");
    }


}
