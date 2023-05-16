package com.myutil.deencrypt;

import com.myutil.Base64;
import com.myutil.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: AES加密工具类
 * @author: hyzhang
 * @create: 2020-05-30 11:19
 **/
@Slf4j
public class AESUtil {

    String REGEDIT_AND_ACTIVATION_MAIL_SUBJECT="Verify Your DTEN Account";
    String RESET_PASSWORD_SUBJECT="Reset Password";

    String QR_ACTIVATE_KEY="QRActivate:";

    /**
     * symbol char type
     */
    String STRING_UTF8 = "utf-8";

    /**
     * AES 加密参数 key
     */
    public static  String AES_KEY = System.getenv("ENCRYPT_KEY");

    /**
     * AES 加密参数 vector
     */
    public static String AES_VECTOR = System.getenv("ENCRYPT_VECTOR");

    /**
     * AES 加密
     *
     * @param data 需要加密的数据
     * @param key 加密密钥
     * @param vector 加密向量
     * @return String
     * @throws Exception
     */
    public static String encrypt(String data, String key,String vector) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(vector.getBytes());
            byte[] byteContent = data.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"),ips);

            byte[] result = cipher.doFinal(byteContent);

            return new String(Base64.encodeBase64(result));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * AES 解密
     *
     * @param data 需要解密数据
     * @param key 加密密钥
     * @param vector 加密向量
     * @return 字符串
     * @throws Exception
     */
    public static String decrypt(String data, String key,String vector) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(vector.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"),ips);
            byte[] result = cipher.doFinal(Base64.decodeBase64(data.getBytes()));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
//        String device = encrypt("50DE19E000CD",OrbitManagerConstant.AES_KEY,OrbitManagerConstant.AES_VECTOR);
//        String command = encrypt("1",OrbitManagerConstant.AES_KEY,OrbitManagerConstant.AES_VECTOR);
//        String commandVal = encrypt("1,2,3,4,5,6,7,8,9,10",OrbitManagerConstant.AES_KEY,OrbitManagerConstant.AES_VECTOR);
//        String empty = encrypt("",OrbitManagerConstant.AES_KEY,OrbitManagerConstant.AES_VECTOR);
//
//        String deDevice = decrypt(device,OrbitManagerConstant.AES_KEY,OrbitManagerConstant.AES_VECTOR);
//        String deCommand = decrypt(command,OrbitManagerConstant.AES_KEY,OrbitManagerConstant.AES_VECTOR);
//        String deCommandVal = decrypt(commandVal,OrbitManagerConstant.AES_KEY,OrbitManagerConstant.AES_VECTOR);
//        String deEmpty = decrypt(empty,OrbitManagerConstant.AES_KEY,OrbitManagerConstant.AES_VECTOR);
//
//        String url = "http://localhost/survey-service/record/updateOpenFeedbackTime?type=bluetooth&userId=2021060702415290594";
//        String encryUrl = encryptUrl(url);
//        log.info(encryUrl);
//        String urlParms = getUrlParam(encryUrl);
//        int index = urlParms.indexOf("=");
//        urlParms = urlParms.substring(index + 1);
//        log.info("urlParms:" + urlParms);
//        Map<String,String> decryptUrlParam = decryptUrlParam(urlParms);
//        log.info("The urlParam:" + JSON.toJSONString(decryptUrlParam));
//        String encryMail = encrypt("wyu@cn.dten.com");
//        log.info("The encryKey:{}", encryMail);
        return;
    }

    /**
     * 对url后的参数进行加密，将问号后面的参数加密，作为新url中key的拼接参数，如：
     * 加密前   http://localhost/survey-service/record/updateOpenFeedbackTime?type=bluetooth&userId=2021060702415290594
     * 加密后   http://localhost/survey-service/record/updateOpenFeedbackTime?key=rWGiZo36iaRdm%2FJLkBsGWmRg0CbGexKP%2FQQYx79DP3sXzB3RJ9%2B6egFkCTusBKkD
     * @param url
     * @return
     */
    public static String encryptUrl(String url) {
        String newUrl=null;
        String param=null;
        if(url.contains("?")){
            newUrl=getFrontUrl(url)+"key=";
            param=getUrlParam(url);
            param=encrypt(param);
            try {
                param= URLEncoder.encode(param,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return newUrl+param;
        }else{
            return url;
        }
    }

    /**
     * 对url进行解密，将加密后的url还原为没加密的。
     * @return
     */
    public static Map<String,String> decryptUrlParam(String urlParam) {
        String param=null;
        try {
            param= URLDecoder.decode(urlParam,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        param=decrypt(param);
        Map<String,String> map= convertUrlParam2Map(param);
        return map;
    }

    /**
     * 将url中的参数转换为map
     * @param urlParam  emailType=bluet_invite&userServiceId=115&expiresTime=1623147304358
     * @return
     */
    public static Map<String,String> convertUrlParam2Map(String urlParam){
        Map<String,String> info=new HashMap<>();
        String param[]=null;
        String singleParam[]=null;
        if(!StringUtils.isEmpty(urlParam)){
            if(urlParam.contains("&")){
                param=urlParam.split("&");
            }else{
                param=new String[]{urlParam};
            }
            for (String str : param) {
                singleParam = str.split("=");
                info.put(singleParam[0],singleParam[1]);
            }
        }
        return info;
    }

    /**
     * 获取url参数部分。
     * @param url
     * @return
     */
    private static String getFrontUrl(String url){
        if(url.contains("?")){
            return url.substring(0,url.indexOf("?")+1);
        }else{
            return url;
        }
    }

    private static String getUrlParam(String url){
        if(url.contains("?")){
            return url.substring(url.indexOf("?")+1);
        }else{
            return null;
        }
    }



    /**
     * AES 加密
     * @param data 需要加密的数据
     * @return String
     * @throws Exception
     */
    public static String encrypt(String data) {
        if(StringUtils.isEmpty(data)){
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(AES_VECTOR.getBytes());
            byte[] byteContent = data.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(AES_KEY.getBytes(), "AES"),ips);
            byte[] result = cipher.doFinal(byteContent);
            return new String( Base64.encodeBase64(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES 解密
     * @param data 需要解密数据
     * @return 字符串
     */
    public static String decrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(AES_VECTOR.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(AES_KEY.getBytes(), "AES"),ips);
            byte[] result = cipher.doFinal(Base64.decodeBase64(data.getBytes()));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
