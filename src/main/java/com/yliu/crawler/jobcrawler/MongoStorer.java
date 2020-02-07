package com.yliu.crawler.jobcrawler;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.yliu.crawler.core.bean.ErrorPage;
import com.yliu.crawler.core.bean.Item;
import com.yliu.crawler.core.persistence.Storer;
import com.yliu.utils.MongoUtil;

public class MongoStorer implements Storer{

	@Override
	public int save(Item item) {
		MongoCollection<Document> col = MongoUtil.getCollection("crawler", "Job");
		Document doc = MongoUtil.adaptToDocument(item);
		col.insertOne(doc);
		return 1;
	}

	@Override
	public int saveError(ErrorPage errorPage) {
		MongoCollection<Document> col = MongoUtil.getCollection("crawler", "Error");
		Document doc = MongoUtil.adaptToDocument(errorPage);
		col.insertOne(doc);
		return 1;
	}

}
