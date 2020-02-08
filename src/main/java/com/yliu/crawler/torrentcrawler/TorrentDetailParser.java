package com.yliu.crawler.torrentcrawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yliu.crawler.bean.TorrentDetail;
import com.yliu.crawler.core.bean.Item;
import com.yliu.crawler.core.bean.Page;
import com.yliu.crawler.core.exception.EllegalityUrlCatchtException;
import com.yliu.crawler.core.parser.Parser;
import com.yliu.crawler.utils.DateUtils;

public class TorrentDetailParser implements Parser{

	private String searchKey;
	
	
	public TorrentDetailParser(String searchKey) {
		super();
		this.searchKey = searchKey;
	}


	@Override
	public Page parse(String input, String source) throws EllegalityUrlCatchtException {
		Page page=new Page();
		Document doc = Jsoup.parse(input);
		Element linkE = doc.selectFirst("textarea[class='magnet-link']");
		if(linkE==null){
			page.setItem(null);
			return page;
		}
		String magnetLink = linkE.text();
		
		Element d = doc.selectFirst("table[class='detailSummary']");
		Elements trs = d.getElementsByTag("tr");
		Map<String,Element> groupByName = trs.stream()
				.collect(Collectors.toMap(e->e.getElementsByTag("th").first().text(),
						Function.identity(),(v1,v2)->v2));
		
		String torrentHash = getTdTag("Torrent Hash:",groupByName).text();
		
		Integer fileNums = Integer.parseInt(getTdTag("Number of Files:",groupByName).text());
		
		Double contentSize = convertToGB(getTdTag("Content Size:",groupByName).text());
		
		Date createDate = DateUtils.strToDate(getTdTag("Created On:",groupByName).text(), 
				"yyyy-MM-dd");
		
		Element keys = getTdTag("Keywords:",groupByName);
		
		List<String> keywords = keys.getElementsByTag("h3")
				.stream()
				.map(Element::text).collect(Collectors.toList());
		
		String link = doc.selectFirst("textarea[class='share-link']").text();
		
		Element torrentDetail = doc.getElementById("torrentDetail");
		
		List<TorrentDetail> details = torrentDetail
				.getElementsByTag("tr")
				.stream()
				.skip(1)
				.map(e->{
					String type = e.getElementsByTag("span").first().attr("class");
					String name = e.selectFirst("td[class='name']").text();
					String size = e.selectFirst("td[class='size']").text();
					
					TorrentDetail detail = new TorrentDetail();
					detail.setType(type);
					detail.setName(name);
					detail.setSize(size);
					return detail;
				}).collect(Collectors.toList());
		
		Map<String,Object> items = new HashMap<String, Object>();
		items.put("magnetLink", magnetLink);
		items.put("torrentHash", torrentHash);
		items.put("fileNums", fileNums);
		items.put("contentSize", contentSize);
		items.put("createDate", createDate);
		items.put("keywords", keywords);
		items.put("link", link);
		items.put("details", details);
		items.put("searchKey", searchKey);
		
		Item item = new Item();
		item.setCatchDate(DateUtils.aligningDateToDay(new Date()));
		item.setSource(source);
		item.setItems(items);
		page.setItem(item);
		return page;
	}

	
	private Element getTdTag(String key,Map<String,Element> groupByName){
		return groupByName.get(key)
				.getElementsByTag("td").first();
	}
	
	private Double convertToGB(String text){
		if(StringUtil.isBlank(text)){
			return 0d;
		}
		text = text.toUpperCase();
		
		if(text.contains("GB")){
			text = text.replaceAll("GB", "").trim();
			return Double.parseDouble(text);
		}else if(text.contains("MB")){
			text = text.replaceAll("MB", "").trim();
			return Double.parseDouble(text)/1024d;
		}else if(text.contains("KB")){
			text = text.replaceAll("KB", "").trim();
			return Double.parseDouble(text)/1024d/1024d;
		}
		
		return 0d;
	}
}
