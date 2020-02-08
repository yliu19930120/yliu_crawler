package com.yliu.crawler.commons;

import java.util.List;

import com.yliu.crawler.core.queue.BufferQueue;
import com.yliu.utils.RedisUtils;

import redis.clients.jedis.Jedis;


public class RedisQueue implements BufferQueue{
	
	private String key;
	
	public RedisQueue(String key) {
		this.key = key;
	}
	@Override
	public int push(String value) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.sadd(key, value);
		jedis.close();
		return 1;
	}

	@Override
	public int push(List<String> values) {
		String[] array = new String[values.size()];
		values.toArray(array);
		Jedis jedis = RedisUtils.getJedis();
		jedis.sadd(key, array);
		jedis.close();
		return values.size();
	}

	@Override
	public String poll() {
		Jedis jedis = RedisUtils.getJedis();
		String value = jedis.spop(key);
		jedis.close();
		return value;
	}

	@Override
	public boolean isEmpty() {
		Jedis jedis = RedisUtils.getJedis();
		long size = jedis.scard(key);
		jedis.close();
		if(size==0){
			return true;
		}
		return false;
	}
	@Override
	public void clear() {
		Jedis jedis = RedisUtils.getJedis();
		jedis.del(key);
		jedis.close();
	}

}
