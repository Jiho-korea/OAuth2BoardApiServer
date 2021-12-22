package com.mapper.dispatchControlResult;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DispatchControlResultMapper {

	public List<Map<String, String>> dispatchControlResultRead(Map<String, Object> param);

	public int dispatchControlResultCount(Map<String, Object> param);

	public void dispatchControlResultUpdate(Map<String, Object> param);	

}
