package com.yliu.crawler.viedocrawler;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.yliu.crawler.commons.RedisQueue;
import com.yliu.crawler.core.crawler.Crawler;
import com.yliu.crawler.core.parser.Parser;
import com.yliu.crawler.core.persistence.Storer;
import com.yliu.crawler.core.queue.BufferQueue;
import com.yliu.utils.MongoUtil;

public class ViedoCrawler {
	
	private final static String url = "https://www.newfhk.com/nvyouku/";
	
	public void catchVedio(){
		Parser parser = new ListPageParser();
		Parser targetParser = new DetailParser();
		Storer storer = new VedioStorer();
		BufferQueue queue = new RedisQueue("vedio");
		
		MongoCollection<Document> actorCol = MongoUtil.getCollection("crawler", "Actor");
		MongoCollection<Document> vedioCol = MongoUtil.getCollection("crawler", "Vedio");
		//先全删掉
		actorCol.deleteMany(new Document());
		vedioCol.deleteMany(new Document());
		
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
		new ViedoCrawler().catchVedio();
	}
}
