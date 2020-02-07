package com.yliu.crawler.viedocrawler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.yliu.crawler.bean.Actor;
import com.yliu.crawler.bean.Vedio;
import com.yliu.crawler.core.bean.ErrorPage;
import com.yliu.crawler.core.bean.Item;
import com.yliu.crawler.core.persistence.Storer;
import com.yliu.utils.MongoUtil;

public class VedioStorer implements Storer{

	@Override
	public int save(Item item) {
		Map<String,Object> items = item.getItems();
		Actor actor = (Actor) items.get("actor");
		List<Vedio> vedios = (List<Vedio>) items.get("vedios");
		MongoCollection<Document> actorCol = MongoUtil.getCollection("crawler", "Actor");
		MongoCollection<Document> vedioCol = MongoUtil.getCollection("crawler", "Vedio");
		actorCol.insertOne(MongoUtil.adaptToDocument(actor));
		List<Document> docs = vedios.stream()
				.map(vedio->MongoUtil.adaptToDocument(vedio))
				.collect(Collectors.toList());
		
		if(!docs.isEmpty()){
			vedioCol.insertMany(docs);
		}
		
		return docs.size()+1;
	}

	@Override
	public int saveError(ErrorPage errorPage) {
		MongoCollection<Document> col = MongoUtil.getCollection("crawler", "Error");
		Document doc = MongoUtil.adaptToDocument(errorPage);
		col.insertOne(doc);
		return 1;
	}

}
