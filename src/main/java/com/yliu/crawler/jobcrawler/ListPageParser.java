package com.yliu.crawler.jobcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yliu.crawler.core.bean.Page;
import com.yliu.crawler.core.parser.Parser;

public class ListPageParser implements Parser{

	@Override
	public Page parse(String input, String source) {
		
		Page page=new Page();
		Document doc = Jsoup.parse(input);
		List<String> targets = new ArrayList<String>();
		List<Element> li = doc.getElementsByClass("bk");
		String nextPage = li.get(li.size()-1).select("a[href]").attr("href"); 
        Elements detaiList=doc.select("a[onmousedown]");
        if(detaiList!=null&&!detaiList.isEmpty()){
        	targets = detaiList.stream().map(e->e.attr("href")).collect(Collectors.toList());
        }
        if(nextPage!=null&&!"".equals(nextPage)){
        	page.setNext(nextPage);
        }
        page.setTargets(targets);
		return page;
	}

}
