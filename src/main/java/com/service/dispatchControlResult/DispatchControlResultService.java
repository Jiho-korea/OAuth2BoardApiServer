package com.service.dispatchControlResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.stereotype.Service;								
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mapper.dispatchControlResult.DispatchControlResultMapper;

@Service
public class DispatchControlResultService {

	private static final Logger log = LogManager.getLogger(DispatchControlResultService.class);
	
	@Autowired
	private DispatchControlResultMapper mapper;
		
    public Map<String,Object> dispatchControlResultRead(Map<String, Object> param) {
    	Map<String,Object> data = new HashMap<String,Object>();
		try{
			List<Map<String, String>> result = mapper.dispatchControlResultRead(param);		
			data.put("data"	, result);
			if(param.get("paging") != null){
				int total = mapper.dispatchControlResultCount(param);
				data.put("total", total);			
			}                 
		}catch (Exception e){
			data.put("error", e.toString());
		}
        return data;        
    }     
    public int dispatchControlResultCount(Map<String, Object> param) {
        return mapper.dispatchControlResultCount(param);	
    }
    
    @Transactional
    public Map<String,Object> dispatchControlResultUpdate(Map<String, Object> param) {
    	Map<String,Object> data=new HashMap<String,Object>();
    	try {			
			mapper.dispatchControlResultUpdate(param);																						
		}catch(Exception e){
			log.info(e);
			data.put("error", e.toString());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}			
        return data;        
    }
}
