package com.yliu.crawler.utils;
/**
 * 系统工具类
 * @author YLiu
 * @Email yliu19930120@163.com
 * 2018年12月16日 下午2:24:20
 */
public class SystemUtils {

	/**
	 * 获取系统CPU核数
	 * @return
	 */
	public static int getNumsOfCPU(){
		return  Runtime.getRuntime().availableProcessors();
	}
}
