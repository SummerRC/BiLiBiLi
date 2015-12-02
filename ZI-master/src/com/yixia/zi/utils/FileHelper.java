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

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import android.os.Environment;

public class FileHelper {

	public static void deleteDir(File f) {
		if (f.exists() && f.isDirectory()) {
			for (File file : f.listFiles()) {
				if (file.isDirectory())
					deleteDir(file);
				file.delete();
			}
			f.delete();
		}
	}

	public static void deleteDirAllMedias(File f) {
		if (f != null && f.exists() && f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.exists() && !file.isDirectory() && file.canRead() && Media.isVideoOrAudio(file))
						file.delete();
				}
			}
			files = f.listFiles();
			if (files == null || files.length == 0)
				f.delete();
		}
	}

	public static String getCanonical(File f) {
		if (f == null)
			return null;

		try {
			return f.getCanonicalPath();
		} catch (IOException e) {
			return f.getAbsolutePath();
		}
	}

	public static File[] listFilesAccordingPref(File f, final boolean hiddenShown) {
		return f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename == null)
					return false;
				File f = new File(dir, filename);
				if (!f.canRead() || !hiddenShown && f.isHidden())
					return false;
				return true;
			}
		});
	}

	public static File[] listSubtrackFilesAccordingPref(File f, final boolean hiddenShown) {
		return f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename == null)
					return false;
				File f = new File(dir, filename);
				if (!f.canRead() || !hiddenShown && f.isHidden() || !Media.isSubTrack(f) && !f.isDirectory())
					return false;
				return true;
			}
		});
	}

	public static String[] listAllMedias(File f) {
		File[] medias = f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename == null)
					return false;
				File f = new File(dir, filename);
				if (f.exists() && f.canRead() && Media.isVideoOrAudio(f))
					return true;
				else
					return false;
			}
		});

		if (medias == null)
			return null;

		sortFilesByName(medias);

		int len = medias.length;
		String[] mediaNames = new String[len];
		for (int i = 0; i < len; i++)
			mediaNames[i] = FileHelper.getCanonical(medias[i]);

		return mediaNames;
	}

	public static void sortFilesBySize(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				if (f1.isDirectory() && f2.isDirectory() || f1.isFile() && f2.isFile())
					return Long.valueOf(f2.length()).compareTo(f1.length());
				else if (f1.isDirectory() && f2.isFile())
					return -1;
				else
					return 1;
			}
		});
	}

	public static void sortFilesByName(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				if (f1.isDirectory() && f2.isDirectory() || f1.isFile() && f2.isFile())
					return f1.getName().trim().compareToIgnoreCase(f2.getName().trim());
				else if (f1.isDirectory() && f2.isFile())
					return -1;
				else
					return 1;
			}
		});
	}

	public static String getUrlFileName(String url) {
		int slashIndex = url.lastIndexOf('/');
		if (slashIndex > -1)
			return url.substring(slashIndex + 1);
		else
			return url;
	}

	public static String getUrlFileNameNoEx(String url) {
		int slashIndex = url.lastIndexOf('/');
		int dotIndex = url.lastIndexOf('.');
		String filenameWithoutExtension;
		if (dotIndex == -1) {
			filenameWithoutExtension = url.substring(slashIndex + 1);
		} else {
			filenameWithoutExtension = url.substring(slashIndex + 1, dotIndex);
		}
		return filenameWithoutExtension;
	}

	public static String getUrlExtension(String url) {
		if (!StringUtils.isBlank(url)) {
			int slashIndex = url.lastIndexOf('/');
			if (slashIndex > -1) {
				String fileName = url.substring(slashIndex + 1);
				int dotIndex = fileName.indexOf('.');
				int paramIndex = fileName.indexOf('?');
				if (dotIndex != -1) {
					if (paramIndex == -1) {
						paramIndex = fileName.indexOf('&');
						if (paramIndex == -1)
							return fileName.substring(dotIndex + 1).toLowerCase();
						else
							return fileName.substring(dotIndex + 1, paramIndex).toLowerCase();
					} else if (paramIndex <= (fileName.length() - 1)) {
						return fileName.substring(dotIndex + 1, paramIndex).toLowerCase();
					}
				}
			}

		}
		return "";
	}

	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	public static boolean sdAvailable() {
		return Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	public static String getFileNameForTitle(String title) {
		int lastDot = title.lastIndexOf('.');
		return lastDot > 0 ? title.substring(0, lastDot) : title;
	}

	public static File getExternalStoragePublicDirectory(String type) {
		return new File(Environment.getExternalStorageDirectory(), type);
	}

	public static int getFileAvailable(String file) {
		return getFileAvailable(new File(file));
	}

	public static int getFileAvailable(File file) {
		int ds = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ds = fis.available();
		} catch (Exception e) {
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e) {

			}
		}
		return ds;
	}
}