package com.myutil.file;

//import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileTxtUtil {
	
	public static String readFileContent(String fileName) {
	    File file = new File(fileName);
	    BufferedReader reader = null;
	    StringBuffer sbf = new StringBuffer();
	    try {
	        reader = new BufferedReader(new FileReader(file));
	        String tempStr;
	        while ((tempStr = reader.readLine()) != null) {
	            sbf.append(tempStr);
	        }
	        reader.close();
	        return sbf.toString();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
	        }
	    }
	    return sbf.toString();
	}

	public static String readFileContent(InputStream inputStream) {
		StringBuffer sbf = new StringBuffer();
		BufferedReader reader = null;
		InputStreamReader inputReader;
		try {
			inputReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			reader = new BufferedReader(inputReader);
			String tempStr;
			while ((tempStr = reader.readLine()) != null) {
				sbf.append(tempStr);
			}
			reader.close();
			return sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(reader != null){
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sbf.toString();
	}
	
	 
	public static String replaceTxtWithParams(String fileName,  Map<String, String> params){
		String readFileContent = readFileContent(fileName);
		return replaceWithParams(readFileContent, params);
	}

	public static String replaceTxtWithParams(InputStream is,  Map<String, String> params){
		String readFileContent = readFileContent(is);
		return replaceWithParams(readFileContent, params);
	}

	public static String replaceActivateCodeTxtWithParams(InputStream is,  Map<String, String> params){
		String readFileContent = readFileContent(is);
		return replaceActiveCodeWithParams(readFileContent, params);
	}

	public static String replaceTxtWithSupportParams(InputStream is,  Map<String, Object> params){
		String readFileContent = readFileContent(is);
		return replaceWithObjectParams(readFileContent, params);
	}


	public static String replaceWithParams(String readFileContent, Map<String, String> params) {
		Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher m = p.matcher(readFileContent);
		StringBuffer sb2 = new StringBuffer();		
		while (m.find()) {
				String group = m.group(1);
				String springMessage = params.get(group);
				if(springMessage == null){
					springMessage = "";
				}
			//m.appendReplacement(sb2, HtmlUtils.htmlEscape(springMessage.replaceAll("\\$", "\\\\\\$")));
			}
		m.appendTail(sb2);// 将匹配到的最后一个字符串之后的数据拼接上
		return sb2.toString();
	}
	
	/**
	 * @Description  激活码邮件文本专用处理
	 * @Method replaceActiveCodeWithParams
	 * @Author hyzhang 
	 * @param readFileContent
	 * @param params
	 * @return {@link String}
	 * @Exception 
	 * @Date 2020/10/15 14:28
	 */
	private static String replaceActiveCodeWithParams(String readFileContent, Map<String, String> params) {
		Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher m = p.matcher(readFileContent);
		StringBuffer sb2 = new StringBuffer();
		while (m.find()) {
			String group = m.group(1);
			String springMessage = params.get(group);
			if(springMessage == null){
				springMessage = "";
			}
			m.appendReplacement(sb2, springMessage);
		}
		m.appendTail(sb2);// 将匹配到的最后一个字符串之后的数据拼接上
		return sb2.toString();
	}

	public static String replaceWithObjectParams(String readFileContent, Map<String, Object> params) {
		Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher m = p.matcher(readFileContent);
		StringBuffer sb2 = new StringBuffer();
		while (m.find()) {
			String group = m.group(1);
			Object springMessage = params.get(group);
			if(springMessage == null){
				springMessage = "";
			}
			//m.appendReplacement(sb2,  HtmlUtils.htmlEscape(springMessage.toString().replaceAll("\\$", "\\\\\\$")));
		}
		m.appendTail(sb2);// 将匹配到的最后一个字符串之后的数据拼接上
		return sb2.toString();
	}

}
