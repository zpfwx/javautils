package com.myutil;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.*;
import java.util.stream.Collectors;

public class TextUtil {
    public TextUtil() {
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0 || "".equals(text.trim());
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static String delEndStr(String src, String endStr) {
        String tmp = src.trim();
        return tmp.endsWith(endStr) ? tmp.substring(0, tmp.length() - endStr.length()) : tmp;
    }

    public static String camelToUnderline(String param) {
        if (param != null && !"".equals(param.trim())) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for (int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (Character.isUpperCase(c)) {
                    if (i != 0) {
                        sb.append('_');
                    }

                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String underlineToCamel(String param, boolean flag) {
        if (param != null && !"".equals(param.trim())) {
            param = param.toLowerCase();
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for (int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (c == '_') {
                    ++i;
                    if (i < len) {
                        sb.append(Character.toUpperCase(param.charAt(i)));
                    } else {
                        sb.append('_');
                    }
                } else if (i == 0) {
                    if (flag) {
                        sb.append(Character.toUpperCase(c));
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String addZeroForLeft(String str, int length) {
        while (str.length() < length) {
            StringBuffer sb = new StringBuffer();
            sb.append("0").append(str);
            str = sb.toString();
        }

        return str;
    }

    public static String addZeroForRight(String str, int length) {
        while (str.length() < length) {
            StringBuffer sb = new StringBuffer();
            sb.append(str).append("0");
            str = sb.toString();
        }

        return str;
    }

    public static List<String> tansferString2ListBySplit(String str, String split) {
        List<String> list = new ArrayList();
        if (isEmpty(str)) {
            return list;
        } else {
            String[] strs = str.split(split);
            String[] var4 = strs;
            int var5 = strs.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String s = var4[var6];
                if (isNotEmpty(s)) {
                    list.add(s);
                }
            }

            return list;
        }
    }

    public static String[] splitSqlOrderBy(String sql) {
        String[] strs = new String[2];
        if (sql.toUpperCase().indexOf("ORDER BY") != -1) {
            int index = sql.toUpperCase().lastIndexOf("ORDER BY");
            strs[0] = " " + sql.substring(0, index - 1) + " ";
            strs[1] = " " + sql.substring(index, sql.length()) + " ";
            if (strs[1].indexOf(".") != -1) {
                throw new RuntimeException("Order by Not allowed to include '.' !!");
            }
        } else {
            strs[0] = " " + sql + " ";
            strs[1] = null;
        }

        return strs;
    }

    public static String subStr(String str, int num) {
        int max = num;

        try {
            max = trimGBK(str.getBytes("GBK"), num);
        } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
        }

        int sum = 0;
        if (str != null && str.length() > max) {
            StringBuilder sb = new StringBuilder(max);

            for (int i = 0; i < str.length(); ++i) {
                int c = str.charAt(i);
                ++sum;
                if (sum > max) {
                    break;
                }

                sb.append((char) c);
            }

            return sb.append("...").toString();
        } else {
            return str != null ? str : "";
        }
    }

    public static int trimGBK(byte[] buf, int n) {
        int num = 0;
        boolean bChineseFirstHalf = false;
        if (buf.length < n) {
            return buf.length;
        } else {
            for (int i = 0; i < n; ++i) {
                if (buf[i] < 0 && !bChineseFirstHalf) {
                    bChineseFirstHalf = true;
                } else {
                    ++num;
                    bChineseFirstHalf = false;
                }
            }

            return num;
        }
    }

    public static String tansferList2SqlString(Collection<String> list) {
        if (list != null && list.size() != 0) {
            Set<String> set = new HashSet();
            set.addAll(list);
            StringBuilder sb = new StringBuilder();
            Iterator var3 = set.iterator();

            while (var3.hasNext()) {
                String str = (String) var3.next();
                if (isNotEmpty(str)) {
                    sb.append("'").append(str).append("'").append(",");
                }
            }

            sb.deleteCharAt(sb.length() - 1);
            String str = " (" + sb.toString() + ") ";
            return str;
        } else {
            return " ('-1') ";
        }
    }

    public static String blob2String(Blob blob) throws Exception {
        StringBuilder content = new StringBuilder();
        InputStream is = blob.getBinaryStream();
        byte[] tmp = new byte[512];

        int len;
        while ((len = is.read(tmp)) != -1) {
            content.append(new String(tmp, 0, len));
        }

        return content.toString();
    }

    public static String getExceptionDetails(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        ex.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }




    /**
     * 只处理S1***的类型
     * @param product
     * @return
     */
    public static String[] getS1DeviceAndDeviceType(String product){
        String [] result=new String[2];
        if(product.contains("-")){
            product = StringUtils.replace(product,"-Pro","");
        }
        if(StringUtils.isNotBlank(product)){
            if(product.length() == 2){
                result[0]=product;
                result[1]=null;
            }else{
                result[0]=product.substring(0,2);
                result[1]=product.substring(2);
            }
        }
        return result;
    }

    /**
     * 将查询参数product填到paramVo对应的属性上
     * @param product
     * D7-0/1-55/75  第一部分 device为D7，第二部分，是否双屏0 否 1是，第三部分，屏幕尺寸 55/75
     * D7-0-75
     * D7-0-55
     * D7-1-55
     * D7-1-75
     * @return
     */
    public static String [] getD7ParamVo(String product){
        String [] result=new String[3];
        if(StringUtils.isNotEmpty(product)){
            if(product.contains("-")){
                String[] split = product.split("-");
                result[0]=split[0];
                result[1]=split[1];
                if (split.length == 3) {
                    result[2]=split[2];
                } else {
                    result[2]=null;
                }

            }else{
                result[0]=product;
                result[1]=null;
                result[2]=null;
            }
        }
        return result;
    }

    /**
     * 版本号比较
     * @param source 需要比较的版本
     * @param targat 目标版本
     * @return 0代表相等，1代表左边大，-1代表右边大
     * compareVersion("1.0.358","1.0.358")=1
     */
    public static int compareVersion(String source, String targat) {
        if (source.equalsIgnoreCase(targat)) {
            return 0;
        }
        String[] sourceArray = source.split("[.]");
        String[] targatArray = targat.split("[.]");
        int index = 0;
        int minLen = Math.min(sourceArray.length, targatArray.length);
        long diff = 0;

        while (index < minLen
                && (diff = Long.parseLong(sourceArray[index])- Long.parseLong(targatArray[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < sourceArray.length; i++) {
                if (Long.parseLong(sourceArray[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < targatArray.length; i++) {
                if (Long.parseLong(targatArray[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }


    public static void main(String[] args) {
        String source="1.7.10";
        String targat="1.7.3";
        int i = compareVersion(source, targat);
        System.out.println(i);
    }



}
