package com.controller.dispatchControlResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.service.dispatchControlResult.DispatchControlResultService;
import com.service.workcenter.WorkcenterService;
import com.util.Pagination;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dispatchControlResult")
public class DispatchControlResultController {

	private static final Logger log = LogManager.getLogger(DispatchControlResultController.class);

	@Autowired
	private DispatchControlResultService dispatchControlResultService;
	@Autowired
	private WorkcenterService workcenterService;

	@GetMapping("/ccr")
	public ResponseEntity<Pagination> dispatchControlResultCcr(@RequestParam Map<String, Object> param,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "1") int range) {

		Map<String, Object> dispatchControlResultParams = new HashMap<String, Object>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1); // 하루 전
		String resultDate = dateFormatter.format(cal.getTime());

		dispatchControlResultParams.put("company", "LBD716"); // 포랙스
		dispatchControlResultParams.put("user_id", "test");
//		dispatchControlResultParams.put("company", "L02460"); // SNK
//		dispatchControlResultParams.put("user_id", "admin");
//		dispatchControlResultParams.put("company", param.get("company"));
//		dispatchControlResultParams.put("user_id", param.get("user_id"));
		dispatchControlResultParams.put("result_date_from", resultDate);
		dispatchControlResultParams.put("result_date_to", resultDate);
		dispatchControlResultParams.put("limit", 6);

		Map<String, Object> workcenterCCRParams = new HashMap<String, Object>();
		workcenterCCRParams.put("company", "LBD716"); // 포랙스
		workcenterCCRParams.put("user_id", "test");
//		workcenterCCRParams.put("company", "L02460"); // SNK
//		workcenterCCRParams.put("user_id", "admin");
//		workcenterCCRParams.put("company", param.get("company"));
//		workcenterCCRParams.put("user_id", param.get("user_id"));
		workcenterCCRParams.put("sort", "ccr_flag");
		workcenterCCRParams.put("sortType", "ASC");
		workcenterCCRParams.put("sort2", "workcenter");
		workcenterCCRParams.put("sortType2", "ASC");
		workcenterCCRParams.put("ccr_flag", "X");

		// ccr workcenter 개수
		int workcenterCCRListCnt = workcenterService.workcenterReadCount(workcenterCCRParams);
		//log.info(workcenterCCRListCnt);
		
		// Pagination 객체생성
		Pagination workcenterPaginationCCR = new Pagination();
		workcenterPaginationCCR.setListSize(3);
		workcenterPaginationCCR.pageInfo(page, range, workcenterCCRListCnt);

		workcenterCCRParams.put("startList", workcenterPaginationCCR.getStartList());
		workcenterCCRParams.put("listSize", workcenterPaginationCCR.getListSize());

		Map<String, Object> workcenterCCRListData = workcenterService.workcenterRead(workcenterCCRParams);
		ArrayList<HashMap> workcenterCCRList = (ArrayList<HashMap>) workcenterCCRListData.get("data");

		workcenterCCRList.forEach(e -> {
			// log.info(e.get("workcenter"));
			dispatchControlResultParams.put("workcenter", e.get("workcenter"));
			dispatchControlResultParams.put("sort", "first");
			dispatchControlResultParams.put("sortType", "ASC");
			
			Map<String, Object> dispatchControlResultListData = dispatchControlResultService
					.dispatchControlResultRead(dispatchControlResultParams);
			ArrayList<HashMap> dispatchControlResultList = (ArrayList<HashMap>) dispatchControlResultListData
					.get("data");
			e.put("dispatchControlResult", dispatchControlResultList);
			//log.info(dispatchControlResultList.size());
		});

		workcenterPaginationCCR.setWorkcenterListData(workcenterCCRListData);

		// dispatchControlResult 개수
		// int dispatchControlResultListCnt =
		// dispatchControlResultService.dispatchControlResultCount(dispatchControlResultParams);
		// log.info(dispatchControlResultListCnt);

		// dispatchControlResultL Pagination 객체생성
		// Pagination dispatchControlResultPagination = new Pagination();
		// dispatchControlResultPagination.pageInfo(page, range,
		// dispatchControlResultListCnt);

		// Map<String,Object> data =
		// dispatchControlResultService.dispatchControlResultRead(dispatchControlResultParams);
		return new ResponseEntity<Pagination>(workcenterPaginationCCR, HttpStatus.OK);
	}
	
	@GetMapping("/nonCCR")
	public ResponseEntity<Pagination> dispatchControlResultNonCCR(@RequestParam Map<String, Object> param,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "1") int range) {

		Map<String, Object> dispatchControlResultParams = new HashMap<String, Object>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1); // 하루 전
		String resultDate = dateFormatter.format(cal.getTime());

		dispatchControlResultParams.put("company", "LBD716"); // 포랙스
		dispatchControlResultParams.put("user_id", "test");
//		dispatchControlResultParams.put("company", "L02460"); // SNK
//		dispatchControlResultParams.put("user_id", "admin");
//		dispatchControlResultParams.put("company", param.get("company"));
//		dispatchControlResultParams.put("user_id", param.get("user_id"));
		dispatchControlResultParams.put("result_date_from", resultDate);
		dispatchControlResultParams.put("result_date_to", resultDate);
		dispatchControlResultParams.put("limit", 6);

		Map<String, Object> workcenterNonCCRParams = new HashMap<String, Object>();
		workcenterNonCCRParams.put("company", "LBD716"); // 포랙스
		workcenterNonCCRParams.put("user_id", "test");
//		workcenterNonCCRParams.put("company", "L02460"); // SNK
//		workcenterNonCCRParams.put("user_id", "admin");
//		workcenterNonCCRParams.put("company", param.get("company"));
//		workcenterNonCCRParams.put("user_id", param.get("user_id"));
		workcenterNonCCRParams.put("sort", "ccr_flag");
		workcenterNonCCRParams.put("sortType", "ASC");
		workcenterNonCCRParams.put("sort2", "workcenter");
		workcenterNonCCRParams.put("sortType2", "ASC");
		workcenterNonCCRParams.put("non_ccr_flag", "X");

		// Non-CCR
		// NonCCR workcenter 개수
		int workcenterNonCCRListCnt = workcenterService.workcenterReadCount(workcenterNonCCRParams);
		//log.info(workcenterNonCCRListCnt);
		
		// Pagination 객체생성
		Pagination workcenterPaginationNonCCR = new Pagination();
		workcenterPaginationNonCCR.setListSize(3);
		workcenterPaginationNonCCR.pageInfo(page, range, workcenterNonCCRListCnt);

		workcenterNonCCRParams.put("startList", workcenterPaginationNonCCR.getStartList());
		workcenterNonCCRParams.put("listSize", workcenterPaginationNonCCR.getListSize());

		Map<String, Object> workcenterNonCCRListData = workcenterService.workcenterRead(workcenterNonCCRParams);
		ArrayList<HashMap> workcenterNonCCRList = (ArrayList<HashMap>) workcenterNonCCRListData.get("data");

		workcenterNonCCRList.forEach(e -> {
			// log.info(e.get("workcenter"));
			dispatchControlResultParams.put("workcenter", e.get("workcenter"));
			dispatchControlResultParams.put("sort", "delay_day");
			dispatchControlResultParams.put("sortType", "DESC");
			
			Map<String, Object> dispatchControlResultListData = dispatchControlResultService
					.dispatchControlResultRead(dispatchControlResultParams);
			ArrayList<HashMap> dispatchControlResultList = (ArrayList<HashMap>) dispatchControlResultListData
					.get("data");
			e.put("dispatchControlResult", dispatchControlResultList);
			//log.info(dispatchControlResultList.size());
		});

		workcenterPaginationNonCCR.setWorkcenterListData(workcenterNonCCRListData);

		return new ResponseEntity<Pagination>(workcenterPaginationNonCCR, HttpStatus.OK);
	}


//	@PostMapping("/read")
//	public ResponseEntity<Map<String, Object>> dispatchControlResultRead(@RequestParam Map<String, Object> param) {
//		Map<String, Object> data = dispatchControlResultService.dispatchControlResultRead(param);
//		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
//	}
//
//	@PostMapping("/update")
//	public ResponseEntity<Map<String, Object>> dispatchControlResultUpdate(@RequestParam Map<String, Object> param) {
//		Map<String, Object> data = dispatchControlResultService.dispatchControlResultUpdate(param);
//		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
//	}

}
