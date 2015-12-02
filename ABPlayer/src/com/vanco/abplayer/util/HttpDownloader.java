package com.vanco.abplayer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.util.EncodingUtils;

import android.util.Log;
import android.util.Xml.Encoding;

public class HttpDownloader {
	private URL url = null;

	/**
	 * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容 
	 * 1.创建一个URL对象
	 * 2.通过URL对象，创建一个HttpURLConnection对象 
	 * 3.得到InputStram 
	 * 4.从InputStream当中读取数据
	 * 
	 * @param urlStr
	 * @return
	 */
	public String download(String urlstr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		String xmlString = "";  
		try {
			// 创建一个URL对象
			url = new URL(urlstr);
			// 创建一个Http连接
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			// 使用IO流读取数据
			buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"utf-8")); // 防止中文出现乱码  gb2312 utf-8
			
            for(String temp = buffer.readLine(); temp != null;xmlString += temp ,temp = buffer.readLine());  
            // 去除字符串中的换行符，制表符，回车符。  
            xmlString = xmlString.replaceAll("/n|/t|/r", "");  
//			while ((line = buffer.readLine()) != null) {
//				sb.append(line);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return xmlString;
	}

	/**
	 * 可以下载字节流文件到SD卡中
	 * 
	 * @param urlstr  要下载文件的URI地址
	 * @param Path  在SD卡上文件夹的路径
	 * @param FileName  在SD卡上文件的名称
	 * @return 该函数返回整型：-1代表下载失败，0代表下载成功，1代表文件已经存在
	 */
	public int download(String urlstr, String Path, String FileName) {
		InputStream inputstream = null;
		BufferedReader buffer = null;
		try {
			FileUitl fileUitls = new FileUitl();
			System.out.println(Path + FileName);
			if (fileUitls.isFileExist(Path + FileName)) {
				return 1;
			} else {
				// 获取URI中的字节流
				inputstream = getInputStreamFromUrl(urlstr);
				// 把字节流转换成字符流
				buffer = new BufferedReader(new InputStreamReader(inputstream,
						"gb2312")); // 防止中文出现乱码   UTF-8
				File resultFile = fileUitls.write2SDFromWrite(Path, FileName,
						buffer);
				if (resultFile == null) {
					return -1;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		} finally {
			try {
				if(buffer != null)
					buffer.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 可以下载字符流和字节流文件到SD卡中
	 * 
	 * @param urlstr
	 * @param Path
	 * @param FileName
	 * @return 该函数返回整型：-1代表下载失败，0代表下载成功，1代表文件已经存在
	 */
	public int downFile(String urlstr, String Path, String FileName) {
		InputStream inputstream = null;
		try {
			FileUitl fileUitls = new FileUitl();
			if (fileUitls.isFileExist(Path + FileName)) {
				return 1;
			} else {
				inputstream = getInputStreamFromUrl(urlstr);
				File resultFile = fileUitls.write2SDFromInput(Path, FileName,
						inputstream);
				if (resultFile == null) {
					return -1;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputstream.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 根据URL得到输入流
	 * 
	 * @param urlstr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private InputStream getInputStreamFromUrl(String urlstr)
			throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		url = new URL(urlstr);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}
	
	// 以下是测试字符编码的
	public static void testCharset(String datastr){
	                try {
	                        String temp = new String(datastr.getBytes(), "GBK");
	                        Log.v("TestCharset","****** getBytes() -> GBK ******/n"+temp);
	                        temp = new String(datastr.getBytes("GBK"), "UTF-8");
	                        Log.v("TestCharset","****** GBK -> UTF-8 *******/n"+temp);
	                        temp = new String(datastr.getBytes("GBK"), "ISO-8859-1");
	                        Log.v("TestCharset","****** GBK -> ISO-8859-1 *******/n"+temp);
	                        temp = new String(datastr.getBytes("ISO-8859-1"), "UTF-8");
	                        Log.v("TestCharset","****** ISO-8859-1 -> UTF-8 *******/n"+temp);
	                        temp = new String(datastr.getBytes("ISO-8859-1"), "GBK");
	                        Log.v("TestCharset","****** ISO-8859-1 -> GBK *******/n"+temp);
	                        temp = new String(datastr.getBytes("UTF-8"), "GBK");
	                        Log.v("TestCharset","****** UTF-8 -> GBK *******/n"+temp);
	                        temp = new String(datastr.getBytes("UTF-8"), "ISO-8859-1");
	                        Log.v("TestCharset","****** UTF-8 -> ISO-8859-1 *******/n"+temp);
	                } catch (UnsupportedEncodingException e) {
	                        e.printStackTrace();
	                }
	        }

}

