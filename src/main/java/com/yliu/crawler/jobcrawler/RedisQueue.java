package com.yliu.crawler.jobcrawler;

import java.util.List;

import com.yliu.crawler.core.queue.BufferQueue;
import com.yliu.utils.RedisUtils;


public class RedisQueue implements BufferQueue{
	
	private String key;
	
	public RedisQueue(String key) {
		this.key = key;
	}
	@Override
	public int push(String value) {
		RedisUtils.getJedis().sadd(key, value);
		return 1;
	}

	@Override
	public int push(List<String> values) {
		String[] array = new String[values.size()];
		values.toArray(array);
		RedisUtils.getJedis().sadd(key, array);
		return values.size();
	}

	@Override
	public String poll() {
		
		return RedisUtils.getJedis().spop(key);
	}

	@Override
	public boolean isEmpty() {
		long size = RedisUtils.getJedis().scard(key);
		if(size==0){
			return true;
		}
		return false;
	}

}
