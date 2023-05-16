package com.myutil.encode;

import java.util.ArrayList;
import java.util.List;

public class EncoderUtil {
	public static byte FLAG = Byte.parseByte("7e", 16);
	public static final String  KEY = "F5yHuwmtonDpcJfX";
	public static final String VECTOR = "BwE1xuhVLqPrZlyQ";
	
	public static byte[] encode(byte[] bytes) {
		//编码 126 转为 125 + 1， 125 转为125 + 0
		List<Byte> blist = new ArrayList<Byte>();
		blist.add(FLAG);
		for(int i=1;i<bytes.length-1;i++){
			if(bytes[i] == 126){
				blist.add((byte)125);
				blist.add((byte)1);
			}else if(bytes[i] == 125){
				blist.add((byte)125);
				blist.add((byte)0);
			}else{
				blist.add(bytes[i]);
			}
		}
		blist.add(FLAG);
		
		byte[] r = new byte[blist.size()];
		int m = 0;
		for (byte b : blist) {
			r[m++] = b;
		}
		return r;
	}

	public static byte[] encodeWithNoFlags(byte[] bytes) {
		//编码 126 转为 125 + 1， 125 转为125 + 0
		List<Byte> blist = new ArrayList<Byte>();
		for(int i=0;i<bytes.length;i++){
			if(bytes[i] == 126){
				blist.add((byte)125);
				blist.add((byte)1);
			}else if(bytes[i] == 125){
				blist.add((byte)125);
				blist.add((byte)0);
			}else{
				blist.add(bytes[i]);
			}
		}
		byte[] r = new byte[blist.size()];
		int m = 0;
		for (byte b : blist) {
			r[m++] = b;
		}
		return r;
	}
	
	public static byte[] decode(byte[] bytes) {
		//编码 126 转为 125 + 1， 125 转为125 + 0
		List<Byte> blist = new ArrayList<Byte>();
		int i=0;
		for(;i<bytes.length-1;i++){
			if(bytes[i] == 125 && bytes[i+1] == 1){
				blist.add((byte)126);
				i++;
			}else if(bytes[i] == 125 && bytes[i+1] == 0){
				blist.add((byte)125);
				i++;
			}else{
				blist.add(bytes[i]);
			}
		}
		if(i == bytes.length-1){
			blist.add(bytes[i]);
		}

		byte[] r = new byte[blist.size()];
		int m = 0;
		for (byte b : blist) {
			r[m++] = b;
		}
		return r;
	}

	public static String fillBlack(String src, int filLength){
		int srcLength = src.length();
		if(srcLength < filLength){
			int remainLength = filLength - srcLength;
			for(int i=0;i<remainLength;i++){
				src += blankStr;
			}
		}
		return src;
		
	}
	
	static int blank = 32; //' '的编码
	static String blankStr = " ";
	
	 
}
