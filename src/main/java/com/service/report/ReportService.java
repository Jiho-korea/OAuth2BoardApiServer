package com.service.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mapper.report.ReportMapper;

@Service
public class ReportService {

	private static final Logger log = LogManager.getLogger(ReportService.class);
	
	@Autowired
	private ReportMapper mapper;
		
    public Map<String,Object> dailyGraph(Map<String, Object> param) {
    	Map<String,Object> data = new HashMap<String,Object>();    	
		try{
			List<Map<String, String>> result = mapper.dailyGraph(param);
			data.put("data", result);

		}catch (Exception e){
			data.put("error", e.toString());
		}	  
        return data;       
    }

    public Map<String,Object> totalGraph(Map<String, Object> param) {
    	Map<String,Object> data = new HashMap<String,Object>();    	
		try{
			String type = param.get("type")+"";
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Calendar cal = Calendar.getInstance();

			Date today = new Date();
			Date dateFrom = dateformat.parse(param.get("date_from")+"");
			Date dateTo   = dateformat.parse(param.get("date_to")+"");		
			if(type.equals("month")) {
				cal.setTime(dateFrom);
				cal.set(Calendar.DAY_OF_MONTH,1);
				dateFrom = cal.getTime();
			} else if(type.equals("week")) {
				cal.setTime(dateFrom);
				cal.add(Calendar.DATE, 2 - cal.get(Calendar.DAY_OF_WEEK));
				dateFrom = cal.getTime();
			}
			Date selectDateFrom = new Date();
			Date selectDateTo = new Date();			
			List<Map> resultList = new ArrayList();
			while(dateFrom.compareTo(dateTo) < 0) {
				if(type.equals("month")) {
					selectDateFrom = dateFrom;
					param.put("date_from", dateformat.format(selectDateFrom));					
					cal.setTime(selectDateFrom);
					cal.add(Calendar.MONTH, 1);
					cal.add(Calendar.DATE, -1);
					selectDateTo = cal.getTime();					
					param.put("date_to", dateformat.format(selectDateTo));					
					List<Map<String, String>> list = mapper.totalGraph(param);
					if(list != null) {
						if(list.size() > 0) {
							for(int i = 0; i < list.size(); i++) {
								Map map = (Map)list.get(i);

								Date date = dateformat.parse(map.get("date")+"");

								if(today.before(date)) {
									map.put("quality_qty","");
									map.put("quality_time","");
									map.put("total_qty","");
									map.put("total_time","");
								}

								resultList.add(map);
								if(i == list.size()-1){
									Map blank = new HashMap();
									blank.put("date","");
									blank.put("plan_qty","");
									blank.put("plan_time","");
									blank.put("quality_qty","");
									blank.put("quality_time","");
									blank.put("total_qty","");
									blank.put("total_time","");
									resultList.add(blank);
								}
							}
						}
					}					
					cal.setTime(selectDateTo);
					cal.add(Calendar.DATE, 1);
					dateFrom = cal.getTime();
				} else if(type.equals("week")) {
					selectDateFrom = dateFrom;
					param.put("date_from", dateformat.format(selectDateFrom));
					cal.setTime(selectDateFrom);
					cal.add(Calendar.DATE, 6);
					selectDateTo = cal.getTime();
					param.put("date_to", dateformat.format(selectDateTo));					
					List<Map<String, String>> list = mapper.totalGraph(param);
					if(list != null) {
						if(list.size() > 0) {
							for(int i = 0; i < list.size(); i++) {
								Map map = (Map)list.get(i);

								Date date = dateformat.parse(map.get("date")+"");

								if(today.before(date)) {
									map.put("quality_qty","");
									map.put("quality_time","");
									map.put("total_qty","");
									map.put("total_time","");
								}

								resultList.add(map);
								if(i == list.size()-1){
									Map blank = new HashMap();
									blank.put("date","");
									blank.put("plan_qty","");
									blank.put("plan_time","");
									blank.put("quality_qty","");
									blank.put("quality_time","");
									blank.put("total_qty","");
									blank.put("total_time","");
									resultList.add(blank);
								}
							}
						}
					}
					cal.setTime(selectDateTo);
					cal.add(Calendar.DATE, 1);
					dateFrom = cal.getTime();
				} else {
					List<Map<String, String>> list = mapper.totalGraph(param);
					if(list != null) {
						if(list.size() > 0) {
							for(int i = 0; i < list.size(); i++) {
								Map map = (Map)list.get(i);
								resultList.add(map);
								
							}
						}
					}
					break;
				}
			}
			data.put("data", resultList);
		}catch (Exception e){
			data.put("error", e.toString());
		}	  
        return data;       
    }

    public Map<String,Object> nonCCR(Map<String, Object> param) {
    	Map<String,Object> data = new HashMap<String,Object>();    	
		try{
			List<Map<String, String>> result = mapper.nonCCR(param);
			data.put("data", result);

		}catch (Exception e){
			data.put("error", e.toString());
		}	  
        return data;       
    }

    public Map<String,Object> buffer(Map<String, Object> param) {
    	Map<String,Object> data = new HashMap<String,Object>();    	
		try{
			List<Map<String, String>> result = mapper.buffer(param);
			data.put("data", result);

		}catch (Exception e){
			data.put("error", e.toString());
		}	  
        return data;       
    }

    public Map<String,Object> bufferList(Map<String, Object> param) {
    	Map<String,Object> data = new HashMap<String,Object>();    	
		try{
			List<Map<String, String>> result = mapper.bufferList(param);
			data.put("data", result);

		}catch (Exception e){
			data.put("error", e.toString());
		}	  
        return data;       
    }

	public Map<String,Object> ccrOnHand(Map<String, Object> param) {
    	Map<String,Object> data = new HashMap<String,Object>();    	
		try{
			List<Map<String, String>> result = mapper.ccrOnHand(param);
			data.put("data", result);

		}catch (Exception e){
			data.put("error", e.toString());
		}	  
        return data;       
    }

}
