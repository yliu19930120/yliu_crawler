package com.yliu.crawler.torrentcrawler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoCollection;
import com.yliu.crawler.commons.RedisQueue;
import com.yliu.crawler.core.crawler.Crawler;
import com.yliu.crawler.core.parser.Parser;
import com.yliu.crawler.core.persistence.Storer;
import com.yliu.crawler.core.queue.BufferQueue;
import com.yliu.utils.MongoUtil;

public class TorrentCrawler {
	
	private final static Logger log = LoggerFactory.getLogger(TorrentCrawler.class);
	
	private static final String P_URL = "https://www.torrentkitty.app";
	
	public void catchTorrent(){
		MongoCollection<Document> vedioCol = MongoUtil.getCollection("crawler", "Vedio");
		List<String> keys = StreamSupport
				.stream(vedioCol.distinct("code", String.class).spliterator(), false)
				.collect(Collectors.toList());
		log.info("得到关键字集合size={}", keys.size());
		keys.forEach(this::catchTorrentByKey);
	}
	
	public void catchTorrentByKey(String key){
		Parser parser = new TorrentListParser();
		Parser targetParser = new TorrentDetailParser(key);
		Storer storer = new TorrentStorer();
		BufferQueue queue = new RedisQueue("Torrent");
		String url = P_URL+"/search/"+key;
		
		Crawler
		.create()
		.setParser(parser)
		.setTargetParser(targetParser)
		.setStorer(storer)
		.setQueue(queue)
		.addUrl(url)
		.run(4);
	}
	public static void main(String[] args) {
		new TorrentCrawler().catchTorrent();
	}
}
