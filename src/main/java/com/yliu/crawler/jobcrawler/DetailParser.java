package com.yliu.crawler.jobcrawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.yliu.crawler.core.bean.Item;
import com.yliu.crawler.core.bean.Page;
import com.yliu.crawler.core.exception.EllegalityUrlCatchtException;
import com.yliu.crawler.core.parser.Parser;
import com.yliu.crawler.utils.DateUtils;

public class DetailParser implements Parser{
	
	
	private static final List<String> EDU_BACK = Arrays.asList("大专","本科","硕士 ","博士");
	
	private static final Map<String,Double> UNITS= new HashMap<>();
	
	static{
		UNITS.put("千/月", 1000.00);
		UNITS.put("万/月", 10000.00);
		UNITS.put("千/年", 83.33);
		UNITS.put("万/年", 833.33);
	}
	
	@Override
	public Page parse(String input, String source) throws EllegalityUrlCatchtException {
		Page page = new Page();
		Item item = new Item();
		page.setItem(item);
		Map<String, Object> items = new HashMap<>();
		Document doc = Jsoup.parse(input);
		if(doc.getElementById("hidJobID")==null){
			throw new EllegalityUrlCatchtException(source);
		}
		Long jobId = Long.parseLong(doc.getElementById("hidJobID").val());
		String jobName = doc.getElementsByTag("h1").attr("title");

		String salary = doc.select("div[class='cn']").select("strong").text();
		String bases = doc.select("p[class='msg ltype']").attr("title");
		String companyName = doc.select("a[class='catn']").attr("title");
		List<String> tags = doc.select("span[class='sp4']")
				.stream().map(e->e.text()).collect(Collectors.toList());
		Element detail = doc.selectFirst("div[class='bmsg job_msg inbox']");
		List<String> requires = detail.select("p").not("[class='fp']")
				.stream().map(e->e.text()).collect(Collectors.toList());
		String companyDes = doc.select("div[class='tmsg inbox']").text();
		String address = null;
		Element ele = doc.selectFirst("div[class='bmsg inbox']");
		if(ele!=null){
			address = ele.selectFirst("p[class='fp']").text().split("上班地址：")[1];
		}
		
		List<String> keyWords = detail.select("a[href]").not("a[onclick]")
				.stream().map(e->e.text()).collect(Collectors.toList());

		String city = getCity(bases);
		String region = getRegion(bases);
		Double[] salraies = getSalary(salary);
		Integer[] exps = getExp(bases);
		String background =getBackground(bases);
		Integer reqnum = getRecruiting(bases);
		Date issueDate = getIssueDate(bases);

		items.put("jobId", jobId);
		items.put("jobName", jobName);
		items.put("companyName", companyName);
		items.put("tags", tags);
		items.put("companyDes", companyDes);
		items.put("address", address);
		items.put("requires", requires);
		items.put("keyWords", keyWords);
		items.put("city", city);
		items.put("region", region);
		items.put("recruitingNum", reqnum);
		items.put("experienceLowerLimit", exps[0]);
		items.put("experienceUpperLimit", exps[1]);
		items.put("educationBackground", background);
		items.put("issueDate", issueDate);
		items.put("salaryLowerLimit", salraies[0]);
		items.put("salaryUpperLimit", salraies[1]);
		item.setItems(items);
		item.setSource(source);
		item.setCatchDate(DateUtils.aligningDateToDay(new Date()));
		return page;

	}
	

	private static Double[] getSalary(String salary){
		Double[] salaries = new Double[2];
		Double unit = 0.00;
		for(String k:UNITS.keySet()){
			if(salary.contains(k)){
				unit = UNITS.get(k);
				String reg = "\\d.*-\\d.*"+k;
				String salaryStr = getRex(salary, reg);
				if(salaryStr!=null){
					salaryStr = salaryStr.replace(k, "");
					salaries[0] = Double.parseDouble(salaryStr.split("-")[0])*unit;
					salaries[1] = Double.parseDouble(salaryStr.split("-")[1])*unit;
				}

			}
		}

		return salaries;
	}
	private static Integer[] getExp(String bases){
		Integer[] exps = new Integer[2];
		exps[0]=0;
		exps[1]=99;
		if(bases.contains("以上")){
			exps[0]=10;
		}
		String rgex = "\\d*-\\d*年经验";
		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
		List<String> group = new ArrayList<>();
		Matcher m = pattern.matcher(bases);
		int i = 0;
		while(m.find()){
			group.add(m.group(i++));
		}
		if(i>0){
			String expe = group.get(0).replace("年经验", "");
			if(expe.contains("-")){
				exps[0] = Integer.parseInt(expe.split("-")[0]);
				exps[1] = Integer.parseInt(expe.split("-")[1]);
			}
		}
		return exps;
	}
	
	private static String getBackground(String bases){
		String back = null;
		for(String e:EDU_BACK){
			if(bases.contains(e)){
				back = e;
			}
		}
		return back;
	}
	
	private static Integer getRecruiting(String bases){
		Integer reg = 99;
		String req = getRex(bases,"\\d*人");
		if(req!=null&&req.contains("人")&&!bases.contains("若干")){
			reg = Integer.parseInt(req.replace("人", ""));
		}
		return reg;
	}
	
	private static Date getIssueDate(String bases){
		String issueStr = getRex(bases,"\\d*-\\d*发布");
		if(issueStr!=null){
			String year = DateUtils.dateToStr(new Date(), "yyyy");
			String str = String.format("%s-%s", year,issueStr.replace("发布", ""));
			Date issueDate = DateUtils.strToDate(str, "yyyy-MM-dd");
			return issueDate;
		}
		return null;
	}
	
	private static String getRex(String input,String reg){
		Pattern pattern = Pattern.compile(reg);// 匹配的模式
		List<String> group = new ArrayList<>();
		Matcher m = pattern.matcher(input);
		int i = 0;
		while(m.find()){
			group.add(m.group(i++));
		}
		if(group.size()==0){
			return null;
		}
		return group.get(0);
	}
	private static String getCity(String bases){
		String cityAndRegion = bases.split("  |  ")[0];
		String city = null;
		if(cityAndRegion.contains("-")){
			city = cityAndRegion.split("-")[0];
		}
		return city;
	}
	
	private static String getRegion(String bases){
		String cityAndRegion = bases.split("  |  ")[0];
		String region = null;
		if(cityAndRegion.contains("-")){
			region = cityAndRegion.split("-")[1];
		}
		return region;
	}
}
