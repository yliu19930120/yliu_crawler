package com.yliu.crawler.jobcrawler;

/**抓取关键字的枚举
 * 
 * @author YLiu
 * @Email yliu19930120@163.com
 * 2019年3月30日 上午9:41:59
 */
public enum JobKeyWordEnum {
	
	JAVA("Java",1,"Java语言的开发者"),
	PYTHON("Python",2,"Python语言的开发者"),
	FRONT_END ("前端",3,"前端开发者"),
	GO("Go",4,"Go语言的开发者"),
	KOTLIN("Kotlin",5,"Kotlin语言的开发者"),
	IOS("IOS",6,"IOS的开发者"),
	ANDRIOD("ANDRIOD",7,"ANDRIOD的开发者"),
	;
	
	private JobKeyWordEnum(String key, Integer keyType, String description) {
		this.key = key;
		this.keyType = keyType;
		this.description = description;
	}
	
	private String key;
	private Integer keyType;
	private String description;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Integer getKeyType() {
		return keyType;
	}
	public void setKeyType(Integer keyType) {
		this.keyType = keyType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
