package com.myutil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BitOperator {

	/**
	 * 把一个整形该为byte
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static byte integerTo1Byte(int value) {
		return (byte) (value & 0xFF);
	}

	/**
	 * 把一个整形该为1位的byte数组
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static byte[] integerTo1Bytes(int value) {
		byte[] result = new byte[1];
		result[0] = (byte) (value & 0xFF);
		return result;
	}

	/**
	 * 把一个整形改为2位的byte数组
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static byte[] integerTo2Bytes(int value) {
		byte[] result = new byte[2];
		result[0] = (byte) ((value >>> 8) & 0xFF);
		result[1] = (byte) (value & 0xFF);
		return result;
	}

	public static void main(String[] args) {
		/*int a = -800;
		BitOperator bitOperator = new BitOperator();
		byte[] integerTo1Byte = bitOperator.integerTo2Bytes(a);
		System.out.println(Arrays.toString(integerTo1Byte));
		
		byte b = -4;
		System.out.println(b & 0xFF);
		System.out.println(252<<8);*/
//		System.out.println((0xff & b) << 8);
//		System.out.println(-800>>8);
//		System.out.println(-4<<8);
//		System.out.println(-32 & 0xff);
//		System.out.println(0xff);
//		System.out.println(Integer.toBinaryString(-24));
		String str = "~device00~}00Id001~}~~";
		System.out.println(new String(decode(encode(str))));
	}

	private static byte[] encode(String str) {
		//编码 126 转为 125 + 1， 125 转为125 + 0
		byte[] bytes = str.getBytes();
		List<Byte> blist = new ArrayList<Byte>();
		byte header = bytes[0];
		blist.add(header);
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
		byte tail = bytes[bytes.length-1];
		blist.add(tail);
		
		byte[] r = new byte[blist.size()];
		int m = 0;
		for (byte b : blist) {
			r[m++] = b;
		}
		return r;
	}
	
	private static byte[] decode(byte[] bytes) {
		//编码 126 转为 125 + 1， 125 转为125 + 0
		List<Byte> blist = new ArrayList<Byte>();
		byte header = bytes[0];
		blist.add(header);
		int i=1;
		for(;i<bytes.length-2;i++){
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
		if(i == bytes.length-2){
			blist.add(bytes[i]);
		}
		byte tail = bytes[bytes.length-1];
		blist.add(tail);
		
		byte[] r = new byte[blist.size()];
		int m = 0;
		for (byte b : blist) {
			r[m++] = b;
		}
		return r;
	}
	
	/**
	 * 把一个整形改为3位的byte数组
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static byte[] integerTo3Bytes(int value) {
		byte[] result = new byte[3];
		result[0] = (byte) ((value >>> 16) & 0xFF);
		result[1] = (byte) ((value >>> 8) & 0xFF);
		result[2] = (byte) (value & 0xFF);
		return result;
	}

	/**
	 * 把一个整形改为4位的byte数组
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static byte[] integerTo4Bytes(int value){
		byte[] result = new byte[4];
		result[0] = (byte) ((value >>> 24) & 0xFF);
		result[1] = (byte) ((value >>> 16) & 0xFF);
		result[2] = (byte) ((value >>> 8) & 0xFF);
		result[3] = (byte) (value & 0xFF);
		return result;
	}

	/**
	 * 把byte[]转化位整形,通常为指令用
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static int byteToInteger(byte[] value) {
		int result;
		if (value.length == 1) {
			result = oneByteToInteger(value[0]);
		} else if (value.length == 2) {
			result = twoBytesToInteger(value);
		} else if (value.length == 3) {
			result = threeBytesToInteger(value);
		} else if (value.length == 4) {
			result = fourBytesToInteger(value);
		} else {
			result = fourBytesToInteger(value);
		}
		return result;
	}

	/**
	 * 把一个byte转化位整形,通常为指令用
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static int oneByteToInteger(byte value) {
		return (int) value & 0xFF;
	}

	/**
	 * 把一个2位的数组转化位整形
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static int twoBytesToInteger(byte[] value) {
		// if (value.length < 2) {
		// throw new Exception("Byte array too short!");
		// }
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		return ((temp0 << 8) + temp1);
	}

	/**
	 * 把一个3位的数组转化位整形
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static int threeBytesToInteger(byte[] value) {
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		int temp2 = value[2] & 0xFF;
		return ((temp0 << 16) + (temp1 << 8) + temp2);
	}

	/**
	 * 把一个4位的数组转化位整形,通常为指令用
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static int fourBytesToInteger(byte[] value) {
		// if (value.length < 4) {
		// throw new Exception("Byte array too short!");
		// }
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		int temp2 = value[2] & 0xFF;
		int temp3 = value[3] & 0xFF;
		return ((temp0 << 24) + (temp1 << 16) + (temp2 << 8) + temp3);
	}

	/**
	 * 把一个4位的数组转化位整形
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static long fourBytesToLong(byte[] value) throws Exception {
		// if (value.length < 4) {
		// throw new Exception("Byte array too short!");
		// }
		int temp0 = value[0] & 0xFF;
		int temp1 = value[1] & 0xFF;
		int temp2 = value[2] & 0xFF;
		int temp3 = value[3] & 0xFF;
		return (((long) temp0 << 24) + (temp1 << 16) + (temp2 << 8) + temp3);
	}

	/**
	 * 把一个数组转化长整形
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static long bytes2Long(byte[] value) {
		long result = 0;
		int len = value.length;
		int temp;
		for (int i = 0; i < len; i++) {
			temp = (len - 1 - i) * 8;
			if (temp == 0) {
				result += (value[i] & 0x0ff);
			} else {
				result += (value[i] & 0x0ff) << temp;
			}
		}
		return result;
	}

	/**
	 * 把一个长整形改为byte数组
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static byte[] longToBytes(long value){
		return longToBytes(value, 8);
	}

	/**
	 * 把一个长整形改为byte数组
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static byte[] longToBytes(long value, int len) {
		byte[] result = new byte[len];
		int temp;
		for (int i = 0; i < len; i++) {
			temp = (len - 1 - i) * 8;
			if (temp == 0) {
				result[i] += (value & 0x0ff);
			} else {
				result[i] += (value >>> temp) & 0x0ff;
			}
		}
		return result;
	}

	/**
	 * 得到一个消息ID
	 * 
	 * @return
	 * @throws Exception
	 */
	public static byte[] generateTransactionID() throws Exception {
		byte[] id = new byte[16];
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 0, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 2, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 4, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 6, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 8, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 10, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 12, 2);
		System.arraycopy(integerTo2Bytes((int) (Math.random() * 65536)), 0, id, 14, 2);
		return id;
	}

	/**
	 * 把IP拆分位int数组
	 * 
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public int[] getIntIPValue(String ip) throws Exception {
		String[] sip = ip.split("[.]");
		// if (sip.length != 4) {
		// throw new Exception("error IPAddress");
		// }
		int[] intIP = { Integer.parseInt(sip[0]), Integer.parseInt(sip[1]), Integer.parseInt(sip[2]),
				Integer.parseInt(sip[3]) };
		return intIP;
	}

	/**
	 * 把byte类型IP地址转化位字符串
	 * 
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public String getStringIPValue(byte[] address) throws Exception {
		int first = this.oneByteToInteger(address[0]);
		int second = this.oneByteToInteger(address[1]);
		int third = this.oneByteToInteger(address[2]);
		int fourth = this.oneByteToInteger(address[3]);

		return first + "." + second + "." + third + "." + fourth;
	}

	/**
	 * 合并字节数组
	 * 
	 * @param first
	 * @param rest
	 * @return
	 */
	public byte[] concatAll(byte[] first, byte[]... rest) {
		int totalLength = first.length;
		for (byte[] array : rest) {
			if (array != null) {
				totalLength += array.length;
			}
		}
		byte[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (byte[] array : rest) {
			if (array != null) {
				System.arraycopy(array, 0, result, offset, array.length);
				offset += array.length;
			}
		}
		return result;
	}

	/**
	 * 合并字节数组
	 * 
	 * @param rest
	 * @return
	 */
	public byte[] concatAll(List<byte[]> rest) {
		int totalLength = 0;
		for (byte[] array : rest) {
			if (array != null) {
				totalLength += array.length;
			}
		}
		byte[] result = new byte[totalLength];
		int offset = 0;
		for (byte[] array : rest) {
			if (array != null) {
				System.arraycopy(array, 0, result, offset, array.length);
				offset += array.length;
			}
		}
		return result;
	}

	public float byte2Float(byte[] bs) {
		return Float.intBitsToFloat(
				(((bs[3] & 0xFF) << 24) + ((bs[2] & 0xFF) << 16) + ((bs[1] & 0xFF) << 8) + (bs[0] & 0xFF)));
	}

	public float byteBE2Float(byte[] bytes) {
		int l;
		l = bytes[0];
		l &= 0xff;
		l |= ((long) bytes[1] << 8);
		l &= 0xffff;
		l |= ((long) bytes[2] << 16);
		l &= 0xffffff;
		l |= ((long) bytes[3] << 24);
		return Float.intBitsToFloat(l);
	}

	public int getCheckSum4JT808(byte[] bs, int start, int end) {
		if (start < 0 || end > bs.length)
			throw new ArrayIndexOutOfBoundsException("getCheckSum4JT808 error : index out of bounds(start=" + start
					+ ",end=" + end + ",bytes length=" + bs.length + ")");
//		int cs = 0;
		int cs = bs[0];
		for (int i = 1; i < end; i++) {
			cs ^= bs[i];
		}
//		System.out.println( "校验码："+bs[0]+"   " + cs);
		
//		int a = 0;
//		for (int i = 0; i < end; i++) {
//			a ^= bs[i];
//		}
//		System.out.println("校验码2：" + a);
		
		return cs;
	}

	public int getBitRange(int number, int start, int end) {
		if (start < 0)
			throw new IndexOutOfBoundsException("min index is 0,but start = " + start);
		if (end >= Integer.SIZE)
			throw new IndexOutOfBoundsException("max index is " + (Integer.SIZE - 1) + ",but end = " + end);

		return (number << Integer.SIZE - (end + 1)) >>> Integer.SIZE - (end - start + 1);
	}

	public int getBitAt(int number, int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException("min index is 0,but " + index);
		if (index >= Integer.SIZE)
			throw new IndexOutOfBoundsException("max index is " + (Integer.SIZE - 1) + ",but " + index);

		return ((1 << index) & number) >> index;
	}

	public int getBitAtS(int number, int index) {
		String s = Integer.toBinaryString(number);
		return Integer.parseInt(s.charAt(index) + "");
	}

	@Deprecated
	public int getBitRangeS(int number, int start, int end) {
		String s = Integer.toBinaryString(number);
		StringBuilder sb = new StringBuilder(s);
		while (sb.length() < Integer.SIZE) {
			sb.insert(0, "0");
		}
		String tmp = sb.reverse().substring(start, end + 1);
		sb = new StringBuilder(tmp);
		return Integer.parseInt(sb.reverse().toString(), 2);
	}
	
	public static String byteToString (byte[] bytes){
		try {
			return new String(bytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static byte[] stringToByte (String str){
		try {
			return str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
