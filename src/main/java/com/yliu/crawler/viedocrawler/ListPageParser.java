package com.yliu.crawler.viedocrawler;


import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yliu.crawler.core.bean.Page;
import com.yliu.crawler.core.exception.EllegalityUrlCatchtException;
import com.yliu.crawler.core.parser.Parser;

public class ListPageParser implements Parser{
	
	private final static String P_URL = "https://www.newfhk.com";
	@Override
	public Page parse(String input, String source) throws EllegalityUrlCatchtException {
		Page page=new Page();
		Document doc = Jsoup.parse(input);
		Element first = doc.selectFirst("ul[class='clearfix']");
		
		Elements eles = first.getElementsByClass("pos-r wow fadeInUp");
		List<String> targetUrls = eles.stream()
				.map(e->{
					String href = e.select("a[href]").attr("href");
					href = P_URL+href;
					return href;
				}).collect(Collectors.toList());
		
		Element next = doc.getElementsByClass("pageurl").first();
		Elements as = next.getElementsByTag("a");
		Element ae = as.stream().filter(e->e.text().trim().contains("下一页")).findAny().orElse(null);
		String nextUrl = null;
		if(ae!=null){
			nextUrl = P_URL+ae.attr("href");
		}
		page.setTargets(targetUrls);
		page.setNext(nextUrl);
		return page;
	}

}
