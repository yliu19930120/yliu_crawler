package com.yliu.crawler.viedocrawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yliu.crawler.bean.Actor;
import com.yliu.crawler.bean.Vedio;
import com.yliu.crawler.core.bean.Item;
import com.yliu.crawler.core.bean.Page;
import com.yliu.crawler.core.exception.EllegalityUrlCatchtException;
import com.yliu.crawler.core.parser.Parser;
import com.yliu.crawler.utils.DateUtils;

public class DetailParser implements Parser{
	
	private final static String P_URL = "https://www.newfhk.com";
	@Override
	public Page parse(String input, String source) throws EllegalityUrlCatchtException {
		
		Date catchDate = DateUtils.aligningDateToDay(new Date());
		
		Page page = new Page();
		
		String actorId = new ObjectId().toString();
		Document doc = Jsoup.parse(input);
		Element actEle = doc.selectFirst("div[class='infosay fr pos-r']");
		String actName = actEle.getElementsByTag("h1").first().text();
		String recommend  = actEle.getElementsByTag("p").first().text();
		String urlImg = doc.selectFirst("div[class='infopic fl']")
				.getElementsByTag("img").first().attr("src")
				.replaceAll("//", "");
		List<String> actTags = actEle.getElementsByTag("li")
				.stream()
				.map(Element::text)
				.collect(Collectors.toList());
		
		Actor actor = new Actor();
		actor.setName(actName);
		actor.setPhoto(urlImg);
		actor.setTags(actTags);
		actor.setRecommend(recommend);
		actor.setCatchDate(catchDate);
		actor.setId(actorId);
		
		Element first = doc.selectFirst("ul[class='clearfix']");
		Elements eles = first.getElementsByClass("wow fadeInUp");
		
		List<Vedio> viedos = eles.stream()
				.map(e->{
					String detailUrl = P_URL+e.select("a[href]").attr("href");
					String code = e.getElementsByTag("h3").first().text();
					String imgUrl = e.getElementsByTag("img").first().attr("src").replaceAll("//", "");
					List<String> tags = e.getElementsByTag("p").stream()
							.map(Element::text).collect(Collectors.toList());
					
					String runningTimeStr = tags.stream()
							.filter(s->s.contains("时长"))
							.findAny().orElse(null);
					
					String dateStr = tags.stream()
							.filter(s->s.contains("发行日期"))
							.findAny().orElse(null);
					
					runningTimeStr = StringUtil.isBlank(runningTimeStr)?null:runningTimeStr.replaceAll("时长：", "")
							.replaceAll("分钟", "").trim();
					
					Long runningTime = StringUtil.isBlank(runningTimeStr)?null:
						Long.parseLong(runningTimeStr);
					
					dateStr = StringUtil.isBlank(dateStr)?null:dateStr.replaceAll("发行日期：", "").trim();
					
					Date issueDate = StringUtil.isBlank(dateStr)?null:DateUtils.strToDate(dateStr, "yyyy-MM-dd");
					
					Vedio viedo = new Vedio();
					viedo.setActorName(actName);
					viedo.setCode(code);
					viedo.setDetailUrl(detailUrl);
					viedo.setImgUrl(imgUrl);
					viedo.setIssueDate(issueDate);
					viedo.setRunningTime(runningTime);
					viedo.setSource(source);
					viedo.setCatchDate(catchDate);
					viedo.setActorId(actorId);
					
					return viedo;
				}).collect(Collectors.toList());
		
		Item item = new Item();
		item.setCatchDate(catchDate);
		item.setSource(source);
		Map<String,Object> items = new HashMap<>();
		items.put("actor", actor);
		items.put("vedios", viedos);
		item.setItems(items);
		
		page.setItem(item);
		return page;
	}

}
