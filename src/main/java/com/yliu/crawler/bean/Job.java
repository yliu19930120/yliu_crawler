package com.yliu.crawler.bean;

import java.util.Date;
import java.util.List;

public class Job {
	/**
	 * 工作id	
	 */
	private Long jobId;
	/**
	 * 工作名称
	 */
	private String jobName;
	/**
	 * 所在城市
	 */
	private String city;
	/**
	 * 所在城区
	 */
	private String region;
	/**
	 * 经验要求下限
	 */
	private Integer experienceLowerLimit;
	/**
	 * 经验要求上限
	 */
	private Integer	experienceUpperLimit;
	/**
	 * 教育背景
	 */
	private String educationBackground;
	/**
	 * 招聘人数
	 */
	private Integer recruitingNum;
	/**
	 * 发布日期
	 */
	private Date issueDate;
	/**
	 * 月薪下限
	 */
	private Double salaryLowerLimit;
	/**
	 * 月薪上限
	 */
	private Double salaryUpperLimit;
	/**
	 * 专业要求
	 */
	private String majorRequirement;
	/**
	 * 公司地址
	 */
	private String address;
	/**
	 * 关键词
	 */
	private List<String> keyWords;
	/**
	 * 福利标签
	 */
	private List<String> tags;
	/**
	 * 工作要求
	 */
	private List<String> requires;
	/**
	 * 公司名称
	 */
	private String companyName;
	/**
	 * 公司简介
	 */
	private String companyDes;
	/**
	 * 抓取日期
	 */
	private Date catchDate;
	/**
	 * 抓取地址
	 */
	private String source;
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Integer getExperienceLowerLimit() {
		return experienceLowerLimit;
	}
	public void setExperienceLowerLimit(Integer experienceLowerLimit) {
		this.experienceLowerLimit = experienceLowerLimit;
	}
	public Integer getExperienceUpperLimit() {
		return experienceUpperLimit;
	}
	public void setExperienceUpperLimit(Integer experienceUpperLimit) {
		this.experienceUpperLimit = experienceUpperLimit;
	}
	public String getEducationBackground() {
		return educationBackground;
	}
	public void setEducationBackground(String educationBackground) {
		this.educationBackground = educationBackground;
	}
	public Integer getRecruitingNum() {
		return recruitingNum;
	}
	public void setRecruitingNum(Integer recruitingNum) {
		this.recruitingNum = recruitingNum;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public Double getSalaryLowerLimit() {
		return salaryLowerLimit;
	}
	public void setSalaryLowerLimit(Double salaryLowerLimit) {
		this.salaryLowerLimit = salaryLowerLimit;
	}
	public Double getSalaryUpperLimit() {
		return salaryUpperLimit;
	}
	public void setSalaryUpperLimit(Double salaryUpperLimit) {
		this.salaryUpperLimit = salaryUpperLimit;
	}
	public String getMajorRequirement() {
		return majorRequirement;
	}
	public void setMajorRequirement(String majorRequirement) {
		this.majorRequirement = majorRequirement;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<String> getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public List<String> getRequires() {
		return requires;
	}
	public void setRequires(List<String> requires) {
		this.requires = requires;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyDes() {
		return companyDes;
	}
	public void setCompanyDes(String companyDes) {
		this.companyDes = companyDes;
	}
	public Date getCatchDate() {
		return catchDate;
	}
	public void setCatchDate(Date catchDate) {
		this.catchDate = catchDate;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	
}
