package com.service.workcenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mapper.workcenter.WorkcenterMapper;

@Service
public class WorkcenterService {

	private static final Logger log = LogManager.getLogger(WorkcenterService.class);
	
	@Autowired
	private WorkcenterMapper mapper;
		
    public Map<String,Object> workcenterRead(Map<String, Object> param) {
    	Map<String,Object> data = new HashMap<String,Object>();
		try{
			List<Map<String, String>> result = mapper.workcenterRead(param);		
			data.put("data"	, result);
			if(param.get("paging") != null){
				int total = mapper.workcenterReadCount(param);
				data.put("total", total);			
			}
			if(param.get("equipment") != null){
				param.put("workcenter_seq",param.get("seq"));
				data.put("equipment",mapper.workcenterEquipmentRead(param));
			}

		}catch (Exception e){
			data.put("error", e.toString());
		}
        return data;        
    }    
    
    public int workcenterReadCount(Map<String, Object> param) {
        return mapper.workcenterReadCount(param);	
    }    

    @Transactional
    public Map<String,Object> workcenterCreate(Map<String, Object> param) {    	
    	Map<String,Object> data=new HashMap<String,Object>();
    	try {	
			Integer seq = mapper.workcenterCreate(param);
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap<String, Object> json = objectMapper.readValue(param.get("listData")+"", HashMap.class);
			List<?> list = (List<?>)json.get("equipment");
			for(int i=0;i<list.size();i++){
				HashMap<String, Object> map = (HashMap<String, Object>)list.get(i);
				param.putAll(map);
				param.put("workcenter_seq",seq);					   
				Integer cnt = mapper.workcenterEquipmentCount(param);  
				if(cnt == 0) {
					mapper.workcenterEquipmentCreate(param);
				}else {							
					Map<String,String> info = mapper.workcenterEquipmentInfo(param);  
					data.put("error", info.get("equipment_name")+"는 작업장 "+info.get("workcenter")+" 에 적용되어 있습니다.");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	
				}
			}
		}catch(org.springframework.dao.DuplicateKeyException e){
			data.put("error", "[Server] 이미 등록된 작업장코드입니다.");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}catch(org.springframework.dao.DataIntegrityViolationException e){
			data.put("error", "[Server] 필수 입력값이 입력되지 않았습니다.");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}catch (Exception e) {			
			data.put("error", e.toString());
			log.error(e.toString());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
        return data;        
    }               

    @Transactional
    public Map<String,Object> workcenterUpdate(Map<String, Object> param) {    	
    	Map<String,Object> data = new HashMap<String,Object>();    	
    	try {
			mapper.workcenterUpdate(param);
			param.put("workcenter_seq",param.get("seq"));
			mapper.workcenterEquipmentDelete(param);
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap<String, Object> json = objectMapper.readValue(param.get("listData")+"", HashMap.class);
			List<?> list = (List<?>)json.get("equipment");
			for(int i=0;i<list.size();i++){
				HashMap<String, Object> map = (HashMap<String, Object>)list.get(i);
				param.putAll(map);
				Integer cnt = mapper.workcenterEquipmentCount(param);  
				if(cnt == 0) {
					mapper.workcenterEquipmentCreate(param);
				}else {							
					Map<String,String> info = mapper.workcenterEquipmentInfo(param);  
					data.put("error", info.get("equipment_name")+"는 작업장 "+info.get("workcenter")+" 에 적용되어 있습니다.");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();	
				}
			}
			param.put("file_target_seq", param.get("seq"));
		}catch(org.springframework.dao.DuplicateKeyException e){
			data.put("error", "[Server] 이미 등록된 작업장코드입니다.");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}catch(org.springframework.dao.DataIntegrityViolationException e){
			data.put("error", "[Server] 필수 입력값이 입력되지 않았습니다.");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}catch(Exception e){
			log.info(e);
			data.put("error", e.toString());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
    	
        return data;        
    }   

    @Transactional
    public Map<String,Object> workcenterDelete(Map<String, Object> param) {    	
    	Map<String,Object> data=new HashMap<String,Object>();
    	try {
			mapper.workcenterDelete(param); 	           
		} catch (Exception e) {			
			data.put("error", e.toString());
			log.error(e.toString());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
        return data;        
    }   	

}
