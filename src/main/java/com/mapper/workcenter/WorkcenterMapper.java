package com.mapper.workcenter;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkcenterMapper {
	
	public List<Map<String, String>> workcenterRead(Map<String, Object> param);

	public int workcenterReadCount(Map<String, Object> param);

	public List<Map<String, String>> workcenterEquipmentRead(Map<String, Object> param);

	public int getWorkcenterCount(Map<String, Object> param);

	public int getWorkcenterDeleteCount(Map<String, Object> param);

	public int workcenterCreate(Map<String, Object> param);

	public int workcenterEquipmentCreate(Map<String, Object> param);

	public void workcenterUpdate(Map<String, Object> param);

	public void workcenterDelete(Map<String, Object> param);

	public void workcenterEquipmentDelete(Map<String, Object> param);

	public int workcenterEquipmentCount(Map<String, Object> param);	

	public Map<String, String> workcenterEquipmentInfo(Map<String, Object> param);

}
