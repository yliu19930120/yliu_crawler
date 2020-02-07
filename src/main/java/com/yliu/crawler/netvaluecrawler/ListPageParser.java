package com.yliu.crawler.netvaluecrawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yliu.crawler.core.bean.Item;
import com.yliu.crawler.core.bean.Page;
import com.yliu.crawler.core.exception.EllegalityUrlCatchtException;
import com.yliu.crawler.core.parser.Parser;
import com.yliu.utils.JsonUtil;

public class ListPageParser implements Parser{

	@Override
	public Page parse(String input, String source) throws EllegalityUrlCatchtException {
		Page page = new Page();
		Item item = new Item();
		page.setItem(item);
		item.setCatchDate(new Date());
		item.setSource(source);
		Map<String,Object> items = new HashMap<>();
		item.setItems(items);
		String json = input.replaceAll("var db=", "");
		Map<String,Object> map = JsonUtil.toJava(json, HashMap.class);

		System.out.println(map);
//		List<String> targets = codeEles.stream()
//				.map(ele->{
//					long now = System.currentTimeMillis();
//					long r = (long)(1000000000000000L*Math.random());
//					String url = String.format("http://api.fund.eastmoney.com/f10/lsjz?callback=jQuery1830%s_%s&fundCode=001365&pageIndex=1&pageSize=20&startDate=&endDate=&_=%s", r,now,now+26);
//					return url;
//				})
//				.collect(Collectors.toList());
//		page.setTargets(targets);
		return page;
	}

}
