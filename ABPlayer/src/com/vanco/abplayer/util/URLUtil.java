package com.vanco.abplayer.util;

/**
 * 
 * @author wwj_748
 * @date 2014/8/10
 */
public class URLUtil {
	// BILIBILI_DATA_URL
	public static String URL_DONG_HUA = "http://www.bilibili.com/index/ding/1.json";
	public static String URL_MAD_MAV = "http://www.bilibili.com/index/ding/24.json";
	public static String URL_MAD_3D = "http://www.bilibili.com/index/ding/25.json";
	public static String URL_DONG_HUA_DUAN_PIAN = "http://www.bilibili.com/index/ding/47.json";
	public static String URL_DONG_HUA_ZONG_HE = "http://www.bilibili.com/index/ding/27.json";
	
	public static String URL_LIAN_ZAI_DONG_HUA = "http://www.bilibili.com/index/ding/33.json";
	public static String URL_WAN_JIE_DONG_HUA = "http://www.bilibili.com/index/ding/32.json";
	public static String URL_ZHI_XUN = "http://www.bilibili.com/index/ding/51.json";
	public static String URL_GUAN_FANG_YAN_SHEN = "http://www.bilibili.com/index/ding/152.json";
	public static String URL_GUO_CHAN_DONG_HUA = "http://www.bilibili.com/index/ding/153.json";
	
	public static String URL_YIN_YUE = "http://www.bilibili.com/index/ding/3.json";
	public static String URL_FAN_CHANG = "http://www.bilibili.com/index/ding/31.json";
	public static String URL_VOCALOID_UTAU = "http://www.bilibili.com/index/ding/30.json";
	public static String URL_YAN_ZOU= "http://www.bilibili.com/index/ding/59.json";
	public static String URL_YIN_YUE_XUAN_JI = "http://www.bilibili.com/index/ding/130.json";
	
	public static String URL_KE_JI = "http://www.bilibili.com/index/ding/36.json";
	public static String URL_JI_LU_PIAN = "http://www.bilibili.com/index/ding/37.json";
	public static String URL_KE_PU_REN_WEN = "http://www.bilibili.com/index/ding/124.json";
	public static String URL_YE_SHENG_JI_SHU= "http://www.bilibili.com/index/ding/122.json";
	public static String URL_YAN_JIANG = "http://www.bilibili.com/index/ding/39.json";
	public static String URL_JUN_SHI = "http://www.bilibili.com/index/ding/96.json";
	public static String URL_SHU_MA = "http://www.bilibili.com/index/ding/95.json";
	
	public static String URL_YU_LE = "http://www.bilibili.com/index/ding/5.json";
	public static String URL_GAO_XIAO = "http://www.bilibili.com/index/ding/138.json";
	public static String URL_SHENG_HUO = "http://www.bilibili.com/index/ding/21.json";
	public static String URL_ZONG_YI = "http://www.bilibili.com/index/ding/71.json";
	
	public static String URL_DIAN_YIN = "http://www.bilibili.com/index/ding/23.json";
	public static String URL_OU_MEI_DIAN_YIN = "http://www.bilibili.com/index/ding/145.json";
	public static String URL_RI_BEN_DIAN_YIN = "http://www.bilibili.com/index/ding/146.json";
	public static String URL_GUO_CHAN_DIAN_YIN = "http://www.bilibili.com/index/ding/147.json";
	public static String URL_DIAN_YIN_XIANG_GUAN = "http://www.bilibili.com/index/ding/82.json";
	
	public static String URL_YOU_XI = "http://www.bilibili.com/index/ding/4.json";
	public static String URL_DIAN_JI = "http://www.bilibili.com/index/ding/17.json";
	public static String URL_WANG_LUO_DIAN_YIN = "http://www.bilibili.com/index/ding/65.json";
	public static String URL_DIAN_ZI_JING_JI = "http://www.bilibili.com/index/ding/60.json";
	
	public static String URL_RANK_QUAN_QU = "http://www.bilibili.com/index/rank/all-7-0.json";//全区
	public static String URL_RANK_XIN_FAN = "http://www.bilibili.com/index/rank/all-7-33.json";//新番
	public static String URL_RANK_DONG_HUA = "http://www.bilibili.com/index/rank/all-7-1.json";//动画
	public static String URL_RANK_YIN_YUE = "http://www.bilibili.com/index/rank/all-7-3.json";//音乐
	public static String URL_RANK_YOU_XI = "http://www.bilibili.com/index/rank/all-7-4.json";//游戏
	public static String URL_RANK_KE_JI = "http://www.bilibili.com/index/rank/all-7-36.json";//科技
	public static String URL_RANK_YU_LE = "http://www.bilibili.com/index/rank/all-7-5.json";//娱乐
	public static String URL_RANK_DIAN_YING = "http://www.bilibili.com/index/rank/all-7-23.json";//电影
	
	
	/**
	 * 获取博客列表的URL
	 * 
	 * @param blogType
	 *            博客类型
	 * @param page
	 *            页数
	 * @return
	 */
	public static String getVideoListURL(int videoType) {
		String url = "";
		switch (videoType) {
		case Constants.DEF_VIDEO_TYPE.DONG_HUA:
			url = URL_DONG_HUA;
			break;
		case Constants.DEF_VIDEO_TYPE.MAD_AMV:
			url = URL_MAD_MAV;
			break;
		case Constants.DEF_VIDEO_TYPE.MMD_3D:
			url = URL_MAD_3D;
			break;
		case Constants.DEF_VIDEO_TYPE.DONG_HUA_DUAN_PIAN:
			url = URL_DONG_HUA_DUAN_PIAN;
			break;
		case Constants.DEF_VIDEO_TYPE.DONG_HUA_ZONG_HE:
			url = URL_DONG_HUA_ZONG_HE;
			break;
		case Constants.DEF_VIDEO_TYPE.LIAN_ZAI_DONG_HUA:
			url = URL_LIAN_ZAI_DONG_HUA;
			break;
		case Constants.DEF_VIDEO_TYPE.WAN_JIE_DONG_HUA:
			url = URL_WAN_JIE_DONG_HUA;
			break;
		case Constants.DEF_VIDEO_TYPE.ZHI_XUN:
			url = URL_ZHI_XUN;
			break;
		case Constants.DEF_VIDEO_TYPE.GUAN_FANG_YAN_SHEN:
			url = URL_GUAN_FANG_YAN_SHEN;
			break;
		case Constants.DEF_VIDEO_TYPE.GUO_CHAN_DONG_HUA:
			url = URL_GUO_CHAN_DONG_HUA;
			break;
		//<------------音乐区------------->	
		case Constants.DEF_VIDEO_TYPE.YIN_YUE:
			url = URL_YIN_YUE;
			break;
		case Constants.DEF_VIDEO_TYPE.FAN_CHANG:
			url = URL_FAN_CHANG;
			break;
		case Constants.DEF_VIDEO_TYPE.VOCALOID_UTAU:
			url = URL_VOCALOID_UTAU;
			break;
		case Constants.DEF_VIDEO_TYPE.YAN_ZOU:
			url = URL_YAN_ZOU;
			break;
		case Constants.DEF_VIDEO_TYPE.YIN_YUE_XUAN_JI:
			url = URL_YIN_YUE_XUAN_JI;
			break;
			//<------------科技区------------->	
		case Constants.DEF_VIDEO_TYPE.KE_JI:
			url = URL_KE_JI;
			break;
		case Constants.DEF_VIDEO_TYPE.JI_LU_PIAN:
			url = URL_JI_LU_PIAN;
			break;
		case Constants.DEF_VIDEO_TYPE.KE_PU_REN_WEN:
			url = URL_KE_PU_REN_WEN;
			break;
		case Constants.DEF_VIDEO_TYPE.YE_SHENG_JI_SHU:
			url = URL_YE_SHENG_JI_SHU;
			break;
		case Constants.DEF_VIDEO_TYPE.YAN_JIANG:
			url = URL_YAN_JIANG;
			break;
		case Constants.DEF_VIDEO_TYPE.JUN_SHI:
			url = URL_JUN_SHI;
			break;
		case Constants.DEF_VIDEO_TYPE.SHU_MA:
			url = URL_SHU_MA;
			break;
			//<------------娱乐区------------->	
		case Constants.DEF_VIDEO_TYPE.YU_LE:
			url = URL_YU_LE;
			break;
		case Constants.DEF_VIDEO_TYPE.GAO_XIAO:
			url = URL_GAO_XIAO;
			break;
		case Constants.DEF_VIDEO_TYPE.SHENG_HUO:
			url = URL_SHENG_HUO;
			break;
		case Constants.DEF_VIDEO_TYPE.ZONG_YI:
			url = URL_ZONG_YI;
			break;
			//<------------电影区------------->	
		case Constants.DEF_VIDEO_TYPE.DIAN_YIN:
			url = URL_DIAN_YIN;
			break;
		case Constants.DEF_VIDEO_TYPE.OU_MEI_DIAN_YIN:
			url = URL_OU_MEI_DIAN_YIN;
			break;
		case Constants.DEF_VIDEO_TYPE.RI_BEN_DIAN_YIN:
			url = URL_RI_BEN_DIAN_YIN;
			break;
		case Constants.DEF_VIDEO_TYPE.GUO_CHAN_DIAN_YIN:
			url = URL_GUO_CHAN_DIAN_YIN;
			break;
		case Constants.DEF_VIDEO_TYPE.DIAN_YIN_XIANG_GUAN:
			url = URL_DIAN_YIN_XIANG_GUAN;
			break;
			//<------------游戏区------------->	
		case Constants.DEF_VIDEO_TYPE.YOU_XI:
			url = URL_YOU_XI;
			break;
		case Constants.DEF_VIDEO_TYPE.DIAN_JI:
			url = URL_DIAN_JI;
			break;
		case Constants.DEF_VIDEO_TYPE.WANG_LUO_DIAN_YIN:
			url = URL_WANG_LUO_DIAN_YIN;
			break;
		case Constants.DEF_VIDEO_TYPE.DIAN_ZI_JING_JI:
			url = URL_DIAN_ZI_JING_JI;
			break;
			//<------------排行榜------------->	
		case 10070:
			url = URL_RANK_QUAN_QU;
			break;
		case 100733:
			url = URL_RANK_XIN_FAN;
			break;
		case 10071:
			url = URL_RANK_DONG_HUA;
			break;
		case 10073:
			url = URL_RANK_YIN_YUE;
			break;
		case 10074:
			url = URL_RANK_YOU_XI;
			break;
		case 100736:
			url = URL_RANK_KE_JI;
			break;
		case 10075:
			url = URL_RANK_YU_LE;
			break;
		case 100723:
			url = URL_RANK_DIAN_YING;
			break;
		default:
			break;
		}
		return url;
	}

	/**
	 * 获取刷新博客的URL
	 * 
	 * @param blogType
	 *            博客类型
	 * @return
	 */
	public static String getRefreshBlogListURL(int blogType) {
		String url = "";
		switch (blogType) {
		case Constants.DEF_VIDEO_TYPE.DONG_HUA:
			url = URL_DONG_HUA;
			break;
		case Constants.DEF_VIDEO_TYPE.MAD_AMV:
			url = URL_MAD_MAV;
			break;
		case Constants.DEF_VIDEO_TYPE.MMD_3D:
			url = URL_MAD_3D;
			break;
		case Constants.DEF_VIDEO_TYPE.DONG_HUA_DUAN_PIAN:
			url = URL_DONG_HUA_DUAN_PIAN;
			break;
		case Constants.DEF_VIDEO_TYPE.DONG_HUA_ZONG_HE:
			url = URL_DONG_HUA_ZONG_HE;
			break;
		case Constants.DEF_VIDEO_TYPE.LIAN_ZAI_DONG_HUA:
			url = URL_LIAN_ZAI_DONG_HUA;
			break;
		case Constants.DEF_VIDEO_TYPE.WAN_JIE_DONG_HUA:
			url = URL_WAN_JIE_DONG_HUA;
			break;
		case Constants.DEF_VIDEO_TYPE.ZHI_XUN:
			url = URL_ZHI_XUN;
			break;
		case Constants.DEF_VIDEO_TYPE.GUAN_FANG_YAN_SHEN:
			url = URL_GUAN_FANG_YAN_SHEN;
			break;
		case Constants.DEF_VIDEO_TYPE.GUO_CHAN_DONG_HUA:
			url = URL_GUO_CHAN_DONG_HUA;
			break;
			//<------------音乐区------------->	
			case Constants.DEF_VIDEO_TYPE.YIN_YUE:
				url = URL_YIN_YUE;
				break;
			case Constants.DEF_VIDEO_TYPE.FAN_CHANG:
				url = URL_FAN_CHANG;
				break;
			case Constants.DEF_VIDEO_TYPE.VOCALOID_UTAU:
				url = URL_VOCALOID_UTAU;
				break;
			case Constants.DEF_VIDEO_TYPE.YAN_ZOU:
				url = URL_YAN_ZOU;
				break;
			case Constants.DEF_VIDEO_TYPE.YIN_YUE_XUAN_JI:
				url = URL_YIN_YUE_XUAN_JI;
				break;
				//<------------科技区------------->	
			case Constants.DEF_VIDEO_TYPE.KE_JI:
				url = URL_KE_JI;
				break;
			case Constants.DEF_VIDEO_TYPE.JI_LU_PIAN:
				url = URL_JI_LU_PIAN;
				break;
			case Constants.DEF_VIDEO_TYPE.KE_PU_REN_WEN:
				url = URL_KE_PU_REN_WEN;
				break;
			case Constants.DEF_VIDEO_TYPE.YE_SHENG_JI_SHU:
				url = URL_YE_SHENG_JI_SHU;
				break;
			case Constants.DEF_VIDEO_TYPE.YAN_JIANG:
				url = URL_YAN_JIANG;
				break;
			case Constants.DEF_VIDEO_TYPE.JUN_SHI:
				url = URL_JUN_SHI;
				break;
			case Constants.DEF_VIDEO_TYPE.SHU_MA:
				url = URL_SHU_MA;
				break;
				//<------------娱乐区------------->	
			case Constants.DEF_VIDEO_TYPE.YU_LE:
				url = URL_YU_LE;
				break;
			case Constants.DEF_VIDEO_TYPE.GAO_XIAO:
				url = URL_GAO_XIAO;
				break;
			case Constants.DEF_VIDEO_TYPE.SHENG_HUO:
				url = URL_SHENG_HUO;
				break;
			case Constants.DEF_VIDEO_TYPE.ZONG_YI:
				url = URL_ZONG_YI;
				break;
				//<------------电影区------------->	
			case Constants.DEF_VIDEO_TYPE.DIAN_YIN:
				url = URL_DIAN_YIN;
				break;
			case Constants.DEF_VIDEO_TYPE.OU_MEI_DIAN_YIN:
				url = URL_OU_MEI_DIAN_YIN;
				break;
			case Constants.DEF_VIDEO_TYPE.RI_BEN_DIAN_YIN:
				url = URL_RI_BEN_DIAN_YIN;
				break;
			case Constants.DEF_VIDEO_TYPE.GUO_CHAN_DIAN_YIN:
				url = URL_GUO_CHAN_DIAN_YIN;
				break;
			case Constants.DEF_VIDEO_TYPE.DIAN_YIN_XIANG_GUAN:
				url = URL_DIAN_YIN_XIANG_GUAN;
				break;
				//<------------游戏区------------->	
			case Constants.DEF_VIDEO_TYPE.YOU_XI:
				url = URL_YOU_XI;
				break;
			case Constants.DEF_VIDEO_TYPE.DIAN_JI:
				url = URL_DIAN_JI;
				break;
			case Constants.DEF_VIDEO_TYPE.WANG_LUO_DIAN_YIN:
				url = URL_WANG_LUO_DIAN_YIN;
				break;
			case Constants.DEF_VIDEO_TYPE.DIAN_ZI_JING_JI:
				url = URL_DIAN_ZI_JING_JI;
				break;
				//<------------排行榜------------->	
			case 10070:
				url = URL_RANK_QUAN_QU;
				break;
			case 100733:
				url = URL_RANK_XIN_FAN;
				break;
			case 10071:
				url = URL_RANK_DONG_HUA;
				break;
			case 10073:
				url = URL_RANK_YIN_YUE;
				break;
			case 10074:
				url = URL_RANK_YOU_XI;
				break;
			case 100736:
				url = URL_RANK_KE_JI;
				break;
			case 10075:
				url = URL_RANK_YU_LE;
				break;
			case 100723:
				url = URL_RANK_DIAN_YING;
				break;
			default:
				break;			
		}
		return url;
	}

	/**
	 * 返回博文评论列表链接
	 * 
	 * @param filename
	 *            文件名
	 * @param pageIndex
	 *            页数
	 * @return
	 */
	public static String getCommentListURL(String filename, String pageIndex) {
		return "http://blog.csdn.net/wwj_748/comment/list/" + filename
				+ "?page=" + pageIndex;
	}

}
