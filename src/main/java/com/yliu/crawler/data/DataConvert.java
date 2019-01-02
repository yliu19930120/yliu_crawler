package com.yliu.crawler.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.yliu.crawler.bean.Job;
import com.yliu.crawler.utils.DateUtils;
import com.yliu.utils.JsonUtil;
import com.yliu.utils.MongoUtil;

public class DataConvert {
	
	private static final List<String> EDU_BACK = Arrays.asList("大专","本科","硕士 ","博士");
	
	private static final Map<String,Double> UNITS= new HashMap<>();
	
	static{
		UNITS.put("千/月", 1000.00);
		UNITS.put("万/月", 10000.00);
		UNITS.put("千/年", 83.33);
		UNITS.put("万/年", 833.33);
	}
	
	public static void main(String[] args) {
		new DataConvert().run();
	} 
	private final static Logger log = LoggerFactory.getLogger(DataConvert.class);

	private static Job convert(Document doc) {
		Job job = new Job();
		Document items = doc.get("items", Document.class);
		String bases = items.getString("bases");
		String salary = items.getString("salary");
		String city = getCity(bases);
		String region = getRegion(bases);
		Double[] salraies = getSalary(salary);
		Integer[] exps = getExp(bases);
		String background =getBackground(bases);
		Integer reqnum = getRecruiting(bases);
		Date issueDate = getIssueDate(bases);
		job.setCity(city);
		job.setRegion(region);
		job.setRecruitingNum(reqnum);
		job.setExperienceLowerLimit(exps[0]);
		job.setExperienceUpperLimit(exps[1]);
		job.setEducationBackground(background);
		job.setIssueDate(issueDate);
		job.setSalaryLowerLimit(salraies[0]);
		job.setSalaryUpperLimit(salraies[1]);
		log.info(JsonUtil.toJson(job));
		return job;
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
	private void run(){
		MongoCollection<Document> coll = MongoUtil.getCollection("crawler", "JobSource");
		Block<Document> block = new ConvertBlock();
		Bson filter = Filters.eq("items.jobId", 108818427);
		coll.find(filter).limit(1).forEach(block);
	}
	private class ConvertBlock implements Block<Document> {

		@Override
		public void apply(Document t) {
			try {
				
				DataConvert.convert(t);

			} catch (Exception e) {
				log.info("doc|{}",t.toJson());
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
}
