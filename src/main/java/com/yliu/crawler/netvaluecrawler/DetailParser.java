package com.yliu.crawler.netvaluecrawler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.yliu.crawler.core.bean.Item;
import com.yliu.crawler.core.bean.Page;
import com.yliu.crawler.core.exception.EllegalityUrlCatchtException;
import com.yliu.crawler.core.parser.Parser;

public class DetailParser implements Parser{

	@Override
	public Page parse(String input, String source) throws EllegalityUrlCatchtException {
		Page page = new Page();
		Item item = new Item();
		page.setItem(item);
		item.setCatchDate(new Date());
		item.setSource(source);
		Map<String,Object> items = new HashMap<>();
		item.setItems(items);
		Document doc = Jsoup.parse(input);
		doc.getElementById("tableDiv");
		return page;
	}

}
