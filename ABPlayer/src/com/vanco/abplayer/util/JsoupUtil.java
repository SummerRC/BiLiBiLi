package com.vanco.abplayer.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.vanco.abplayer.model.VideoItem;


/**
 * 
 * @author wwj_748
 * @date 2014/8/10
 */
public class JsoupUtil {
	public static boolean contentFirstPage = true; // 第一页
	public static boolean contentLastPage = true; // 最后一页
	public static boolean multiPages = false; // 多页
	private static final String BILIBILI_HOMEPAGE_URL = "http://www.bilibili.com/mobile/index.html"; // bilibili首页地址
	public static void resetPages() {
		contentFirstPage = true;
		contentLastPage = true;
		multiPages = false;
	}
		
}
