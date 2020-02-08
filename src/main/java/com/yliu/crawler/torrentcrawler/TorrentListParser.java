package com.yliu.crawler.torrentcrawler;

import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yliu.crawler.core.bean.Page;
import com.yliu.crawler.core.exception.EllegalityUrlCatchtException;
import com.yliu.crawler.core.parser.Parser;

public class TorrentListParser implements Parser{

//	private static final String P_URL = "https://www.torrentkitty.app/search/";
	private static final String P_URL = "https://www.torrentkitty.app";
	@Override
	public Page parse(String input, String source) throws EllegalityUrlCatchtException {
		Page page=new Page();
		Document doc = Jsoup.parse(input);
		Element root = doc.getElementById("archiveResult");
		Elements results = root.select("a[rel='information']");
		List<String> targets = results.stream()
				.map(e->P_URL+e.attr("href")).collect(Collectors.toList());
		
		String nextUrl = null;
		Element paging = doc.getElementsByClass("pagination").first();
		if(paging!=null){
			Element current = paging.getElementsByClass("current").first();
			if(current!=null){
			    Elements nexts = paging.getElementsByTag("a");
			    if(!nexts.isEmpty()){
			    	nextUrl = source+nexts.first().attr("href");
			    }
			}
		}
		
		page.setNext(nextUrl);
		page.setTargets(targets);
		return page;
	}

}
