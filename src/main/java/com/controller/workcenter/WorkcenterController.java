package com.controller.workcenter;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lombok.RequiredArgsConstructor;

import com.service.workcenter.WorkcenterService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/workcenter")
public class WorkcenterController {

	private static final Logger log = LogManager.getLogger(WorkcenterController.class);
	
	@Autowired
	private WorkcenterService service;
	
	@GetMapping("/ccr")
    public ResponseEntity<Map<String, Object>> workcenterCCR(@RequestParam Map<String, Object> param) {
		Map<String, Object> workcenterParams = new HashMap<String, Object>();
		
		workcenterParams.put("company", "LBD716"); // 포랙스
		workcenterParams.put("user_id", "test");
//		workcenterParams.put("company", "L02460"); // SNK
//		workcenterParams.put("user_id", "admin");
//		workcenterParams.put("company", param.get("company"));
//		workcenterParams.put("user_id", param.get("user_id"));
		workcenterParams.put("sort", "ccr_flag");
		workcenterParams.put("sortType", "ASC");
		workcenterParams.put("sort2", "workcenter");
		workcenterParams.put("sortType2", "ASC");
		workcenterParams.put("ccr_flag", "X");
		
		Map<String,Object> data = service.workcenterRead(workcenterParams);				
        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);        
	}				  

	@GetMapping("/nonCCR")
    public ResponseEntity<Map<String, Object>> workcenterNonCCR(@RequestParam Map<String, Object> param) {
		Map<String, Object> workcenterParams = new HashMap<String, Object>();
		
		workcenterParams.put("company", "LBD716"); // 포랙스
		workcenterParams.put("user_id", "test");
//		workcenterParams.put("company", "L02460"); // SNK
//		workcenterParams.put("user_id", "admin");
//		workcenterParams.put("company", param.get("company"));
//		workcenterParams.put("user_id", param.get("user_id"));
		workcenterParams.put("sort", "ccr_flag");
		workcenterParams.put("sortType", "ASC");
		workcenterParams.put("sort2", "workcenter");
		workcenterParams.put("sortType2", "ASC");
		workcenterParams.put("non_ccr_flag", "X");
		
		Map<String,Object> data = service.workcenterRead(workcenterParams);				
        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);        
	}			
//	@PostMapping("/create")
//    public ResponseEntity<Map<String, Object>> workcenterCreate(@RequestParam Map<String, Object> param) {	
//		Map<String,Object> data = service.workcenterCreate(param);				
//        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);        
//	}
//
//	@PostMapping("/update")
//    public ResponseEntity<Map<String, Object>> workcenterUpdate(@RequestParam Map<String, Object> param) {		
//		Map<String,Object> data = service.workcenterUpdate(param);				
//        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);        
//	}
//	
//	@PostMapping("/delete")
//    public ResponseEntity<Map<String, Object>> workcenterDelete(@RequestParam Map<String, Object> param) {	
//		Map<String,Object> data = service.workcenterDelete(param);				
//        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);        
//	}

//
//	@PostMapping("/modify")
//    public ResponseEntity<Map<String, Object>> modify(@RequestParam Map<String, Object> param) {	
//		Map<String,Object> data = service.modify(param);				
//        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);        
//	}
//
}
