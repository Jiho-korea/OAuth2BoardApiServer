package com.mapper.report;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper {
	
	public List<Map<String, String>> dailyGraph(Map<String, Object> param);
	public List<Map<String, String>> totalGraph(Map<String, Object> param);

	public List<Map<String, String>> nonCCR(Map<String, Object> param);

	public List<Map<String, String>> buffer(Map<String, Object> param);

	public List<Map<String, String>> bufferList(Map<String, Object> param);

	public List<Map<String, String>> ccrOnHand(Map<String, Object> param);

}
