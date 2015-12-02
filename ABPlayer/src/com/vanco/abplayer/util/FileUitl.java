package com.vanco.abplayer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

/**
 * 文件管理类    在以后的开发中也可以使用这个工具类，提高代码的利用性
 * 只要是对SD卡的操作
 * 1、获取SD卡路径   getSDPATH
 * 2、在SD卡上根据传入的目录名创建目录  createSDDir
 * 3、在创建上目录后可以在该目录上创建文件    createSDFile
 * 4、检测文件是否存在  isFileExist
 * 5、将一个InputStream写入到SD卡中   write2SDFromInput
 * 6、将一个字符流写入到SD卡 write2SDFromWrite
 * 注：如果要写入SD卡，只要调用write2SDFromInput函数即可
 * @author Administrator
 *
 */
public class FileUitl {
	private static String SDPATH;
	
	public FileUitl(){
		//得到当前设备外部存储设备的目录
		SDPATH = Environment.getExternalStorageDirectory() + File.separator;
	}
	/**
	 * 获取当前SD卡的根目录
	 * @return
	 */
	public String getSDPATH(){
		return SDPATH;
	}
	/**
	 * SD卡上创建目录
	 */
	public File createSDDir(String dirName){
		File dir = new File(SDPATH + dirName);
		System.out.println("createSDDir " + SDPATH + dirName);
		dir.mkdir();
		return dir;
	}
	/**
	 * SD卡上创建文件
	 */
	public File createSDFile(String fileName)throws IOException{
		File file = new File(SDPATH + fileName);
		System.out.println("createSDFile " + SDPATH + fileName);
		file.createNewFile();
		return file;
	}
	
	
	/**
	 * 判断SD卡上的文件是否存在
	 */
	public boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}
	/**
	 * 将一个InputStream字节流写入到SD卡中
	 */
	public File write2SDFromInput(String Path, String FileName, InputStream input){
		File file = null;
		OutputStream output = null;   //创建一个写入字节流对象
		try{
			createSDDir(Path);    //根据传入的路径创建目录
			file = createSDFile(Path + FileName); //根据传入的文件名创建
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];   //每次读取4K
			int num = 0;      //需要根据读取的字节大小写入文件
			while((num = (input.read(buffer))) != -1){
				output.write(buffer, 0, num);
			}
			output.flush();  //清空缓存
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			try{
				output.close();
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**
	 * 把传入的字符流写入到SD卡中
	 * @param Path
	 * @param FileName
	 * @param input
	 * @return
	 */
	public File write2SDFromWrite(String Path, String FileName, BufferedReader input){
		File file = null;
		FileWriter output = null;   //创建一个写入字符流对象
		BufferedWriter bufw = null;
		try{
			createSDDir(Path);    //根据传入的路径创建目录
			file = createSDFile(Path + FileName); //根据传入的文件名创建
			output = new FileWriter(file);
			bufw = new BufferedWriter(output);
			String line = null;
			while((line = (input.readLine())) != null){
				System.out.println("line = " + line);
				bufw.write(line);
				bufw.newLine();
			}
			bufw.flush();  //清空缓存
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			try{
				bufw.close();
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return file;
	}
}

