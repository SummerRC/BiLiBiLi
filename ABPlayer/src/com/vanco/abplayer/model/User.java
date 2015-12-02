package com.vanco.abplayer.model;

public class User {
	private String mid;
	private String name;
	private Boolean isApprove = false; //是否是认证账号
	private String spaceName;
	private String sex;
	private String rank;
	private String avatar;
	private String follow ;            //关注好友数目
	private String fans;               //粉丝数目
	private String article;            //投稿数
	private String place;              //所在地
	private String description;        //认证用户为认证信息 普通用户为交友宣言
	private String followlist;         //关注的好友列表
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsApprove() {
		return isApprove;
	}
	public void setIsApprove(Boolean isApprove) {
		this.isApprove = isApprove;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public String getFans() {
		return fans;
	}
	public void setFans(String fans) {
		this.fans = fans;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFollowlist() {
		return followlist;
	}
	public void setFollowlist(String followlist) {
		this.followlist = followlist;
	}

}
