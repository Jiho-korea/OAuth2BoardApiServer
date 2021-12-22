package com.controller.report;

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

import com.service.report.ReportService;

import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private ReportService service;
	
	@GetMapping("/test")
    public ResponseEntity<Map<String, Object>> dailyGraph(@RequestParam Map<String, Object> param) {	
		Map<String,Object> data = new HashMap<>();
		data.put("test", "test");
        return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);        
	}
}
