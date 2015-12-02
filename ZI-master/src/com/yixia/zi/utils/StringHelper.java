/*
 * Copyright (C) 2012 YIXIA.COM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yixia.zi.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StringHelper {
	public static final SimpleDateFormat DATE_FORMAT_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_PART = new SimpleDateFormat("HH:mm");

	public static String currentTimeString() {
		return DATE_FORMAT_PART.format(Calendar.getInstance().getTime());
	}

	public static String md5(String plaintext) {
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(("abitno&hellojane" + plaintext + " vplayer" + 7777777).getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String hashtext = bigInt.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String generateFileSize(long size) {
		String fileSize;
		if (size < KB)
			fileSize = size + "B";
		else if (size < MB)
			fileSize = String.format("%.1f", size / KB) + "KB";
		else if (size < GB)
			fileSize = String.format("%.1f", size / MB) + "MB";
		else
			fileSize = String.format("%.1f", size / GB) + "GB";

		return fileSize;
	}

	public static String generateTime(long time) {
		int totalSeconds = (int) (time / 1000);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
	}

	public static String highlight(String key, String title, String query) {
		try {
			query = " " + query;
			int start = key.indexOf(query), end = start + query.length() - 1;
			if (start == end) {
				return title;
			}
			StringBuffer result = new StringBuffer(title.substring(0, start));
			result.append("<font color='#669900'>" + title.substring(start, end) + "</font>");
			result.append(title.substring(end));
			return result.toString();
		} catch (Exception e) {
			return title;
		}
	}

	public static boolean isBlank(String seq) {
		return (null == seq || seq.equals("") || seq.replaceAll(" ", "").equals(""));
	}

	public static boolean isDigit(String obj) {
		return isBlank(obj) ? false : obj.matches("\\d*");
	}

	public static int toDigit(String obj, int defaltValue) {
		int result = defaltValue;
		try {
			result = Integer.parseInt(obj);
		} catch (NumberFormatException ex) {
			
		}
		return result;
	}

	private static final double KB = 1024.0;
	private static final double MB = 1048576.0;
	private static final double GB = 1073741824.0;
}
