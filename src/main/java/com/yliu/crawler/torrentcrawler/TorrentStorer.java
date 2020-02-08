package com.yliu.crawler.torrentcrawler;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.yliu.crawler.core.bean.ErrorPage;
import com.yliu.crawler.core.bean.Item;
import com.yliu.crawler.core.persistence.Storer;
import com.yliu.utils.MongoUtil;

public class TorrentStorer implements Storer{

	@Override
	public int save(Item item) {
		MongoCollection<Document> col = MongoUtil.getCollection("crawler", "Torrent");
		Document doc = MongoUtil.adaptToDocument(item);
		Bson filter = Filters.eq("items.torrentHash",item.getItems().get("torrentHash"));
		FindOneAndReplaceOptions options = new FindOneAndReplaceOptions();
		options.upsert(true);
		col.findOneAndReplace(filter, doc, options);
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
