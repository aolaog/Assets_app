package com.cdhxqh.polling_mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Configuration
{
	private final SharedPreferences	sharedPreferences;

	Configuration(Context context, String fileName) {
		this(context, fileName, Context.MODE_PRIVATE);
	}

	public Configuration(Context context, String fileName, int mode) {
		if (null == context || fileName == null || fileName.length() == 0)
		{
			throw new IllegalArgumentException();
		}

		sharedPreferences = context.getSharedPreferences(fileName, mode);
	}

	public boolean setBoolean(	String key,boolean value)
	{
		return sharedPreferences.edit().putBoolean(key, value).commit();
	}

	public boolean setInt(	String key,int value)
	{
		return sharedPreferences.edit().putInt(key, value).commit();
	}

	public boolean setFloat(String key,float value)
	{
		return sharedPreferences.edit().putFloat(key, value).commit();
	}

	public boolean setString(	String key,String value)
	{
		return sharedPreferences.edit().putString(key, value).commit();
	}

	public boolean getBoolean(String key)
	{
		return sharedPreferences.getBoolean(key, false);
	}

	public boolean getBoolean(	String key,boolean defaultBoolean)
	{
		return sharedPreferences.getBoolean(key, defaultBoolean);
	}

	public int getInt(	String key,int defaultInt)
	{
		return sharedPreferences.getInt(key, defaultInt);
	}

	public float getFloat(	String key,float defaultFloat)
	{
		return sharedPreferences.getFloat(key, defaultFloat);
	}

	public String getString(String key,String defaultString)
	{
		return sharedPreferences.getString(key, defaultString);
	}
}
