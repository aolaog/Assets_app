package com.cdhxqh.polling_mobile.utils;

/**
 * 工具类
 * 
 */
public class DataTransfer
{
	/**
	 * 把一个字节串转换成一个字符串，用以界面显示
	 */
	public static String xGetString(byte[] bs)
	{
		if (bs != null)
		{
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < bs.length; i++)
			{
				sBuffer.append(String.format("%02x ", bs[i]));
			}
			return sBuffer.toString();
		}
		return "null";
	}

	/**
	 * 把一个字符串转换成一个字节串，用以处理界面中的数据，以供实现使用
	 */
	public static byte[] getBytesByHexString(	String string)
	{
		string = string.replaceAll(" ", "");// 去掉空格
		int len = string.length();
		if (len % 2 == 1)
		{
			return null;
		}
		byte[] ret = new byte[len / 2];
		for (int i = 0; i < ret.length; i++)
		{
			ret[i] = (byte) (Integer.valueOf(string.substring((i * 2), (i * 2 + 2)), 16) & 0xff);
		}
		return ret;
	}

}
