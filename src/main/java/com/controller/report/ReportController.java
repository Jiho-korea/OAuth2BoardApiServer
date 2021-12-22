package com.controller.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.controller.workcenter.WorkcenterController;
import com.service.report.ReportService;
import com.service.workcenter.WorkcenterService;
import com.util.Pagination;

import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController {

	private static final Logger LOG = LogManager.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;
	@Autowired
	private WorkcenterService workcenterService;

//	@PostMapping("/dailyGraph")
//	public ResponseEntity<Map<String, Object>> dailyGraph(@RequestParam Map<String, Object> param) {
//		Map<String, Object> data = reportService.dailyGraph(param);
//		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
//	}
//
//	@PostMapping("/totalGraph")
//	public ResponseEntity<Map<String, Object>> totalGraph(@RequestParam Map<String, Object> param) {
//		Map<String, Object> data = reportService.totalGraph(param);
//		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
//	}

	@GetMapping("/ccr")
	@PreAuthorize("#oauth2.hasAnyScope('read')")
	public ResponseEntity<Pagination> ccr(@RequestParam Map<String, Object> param,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "1") int range, OAuth2Authentication auth) {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String today = dateFormatter.format(cal.getTime());
		cal.add(Calendar.MONTH, -2); // 두달 전
		String twoMonthAgo = dateFormatter.format(cal.getTime());
		reportParams.put("company", "LBD716"); // 포랙스
		reportParams.put("user_id", "test");
//		reportParams.put("company", "L02460"); // SNK
//		reportParams.put("user_id", "admin");
//		reportParams.put("company", param.get("company"));
//		reportParams.put("user_id", param.get("user_id"));
		reportParams.put("date_from", twoMonthAgo);
		reportParams.put("date_to", today);
		reportParams.put("type", "sum");

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

		// ccr workcenter 개수
		int workcenterListCnt = workcenterService.workcenterReadCount(workcenterParams);
		// log.info(workcenterListCnt);
		// Pagination 객체생성
		Pagination pagination = new Pagination();
		pagination.pageInfo(page, range, workcenterListCnt);

		// log.info(pagination.getStartPage());
		// log.info(pagination.getEndPage());
		// log.info(pagination.getPageCnt());

		workcenterParams.put("startList", pagination.getStartList());
		workcenterParams.put("listSize", pagination.getListSize());

		Map<String, Object> workcenterListData = workcenterService.workcenterRead(workcenterParams);

		// log.info(workcenterListData.get("data").getClass().getName()); // ArrayList

		ArrayList<HashMap> workcenterList = (ArrayList<HashMap>) workcenterListData.get("data");

		// log.info(workcenterList.get(0));
		// log.info(workcenterList.get(0).getClass().getName()); // HashMap

		workcenterList.forEach(e -> {
			// log.info(e.get("workcenter"));
			reportParams.put("workcenter", e.get("workcenter"));

			Map<String, Object> totalGraphListData = reportService.totalGraph(reportParams);
			ArrayList<HashMap> totalGraphList = (ArrayList<HashMap>) totalGraphListData.get("data");
			e.put("totalGraph", totalGraphList);
		});

		pagination.setWorkcenterListData(workcenterListData);
		pagination.setName(auth.getName());
		// reportParams.put("workcenter", "F1ML01");
		// Map<String,Object> data = reportService.totalGraph(reportParams);
		return new ResponseEntity<Pagination>(pagination, HttpStatus.OK);
	}

	@GetMapping("/nonCCR")
	public ResponseEntity<Pagination> nonCCR(@RequestParam Map<String, Object> param,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "1") int range) {

		Map<String, Object> reportParams = new HashMap<String, Object>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String today = dateFormatter.format(cal.getTime());
		cal.add(Calendar.MONTH, -2); // 두달 전
		String twoMonthAgo = dateFormatter.format(cal.getTime());

		reportParams.put("company", "LBD716"); // 포랙스
		reportParams.put("user_id", "test");
//		reportParams.put("company", "L02460"); // SNK
//		reportParams.put("user_id", "admin");
//		reportParams.put("company", param.get("company"));
//		reportParams.put("user_id", param.get("user_id"));
		reportParams.put("date_from", twoMonthAgo);
		reportParams.put("date_to", today);

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

		// Non-CCR workcenter 개수
		int workcenterListCnt = workcenterService.workcenterReadCount(workcenterParams);
		// log.info(workcenterListCnt);

		// Pagination 객체생성
		Pagination pagination = new Pagination();
		pagination.pageInfo(page, range, workcenterListCnt);
		// log.info(pagination.getStartPage());
		// log.info(pagination.getEndPage());
		// log.info(pagination.getPageCnt());

		workcenterParams.put("startList", pagination.getStartList());
		workcenterParams.put("listSize", pagination.getListSize());

		Map<String, Object> workcenterListData = workcenterService.workcenterRead(workcenterParams);

		// log.info(workcenterListData.get("data").getClass().getName()); // ArrayList
		ArrayList<HashMap> workcenterList = (ArrayList<HashMap>) workcenterListData.get("data");
		// log.info(workcenterList.size());

		workcenterList.forEach(e -> {
			// log.info(e.get("workcenter"));
			reportParams.put("workcenter", e.get("workcenter"));

			Map<String, Object> nonCCRListData = reportService.nonCCR(reportParams);
			ArrayList<HashMap> nonCCRList = (ArrayList<HashMap>) nonCCRListData.get("data");
			e.put("nonCCR", nonCCRList);
		});

		pagination.setWorkcenterListData(workcenterListData);

		// Map<String, Object> data = reportService.nonCCR(reportParams);
		return new ResponseEntity<Pagination>(pagination, HttpStatus.OK);
	}

//	@PostMapping("/nonCCR")
//	public ResponseEntity<Map<String, Object>> nonCCR(@RequestParam Map<String, Object> param) {
//		Map<String, Object> data = reportService.nonCCR(param);
//		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
//	}

	@GetMapping("/buffer")
	public ResponseEntity<Pagination> buffer(@RequestParam Map<String, Object> param,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "1") int range) {
		Map<String, Object> reportParams = new HashMap<String, Object>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String today = dateFormatter.format(cal.getTime());

		reportParams.put("company", "LBD716"); // 포랙스
		reportParams.put("user_id", "test");
//		reportParams.put("company", "L02460"); // SNK
//		reportParams.put("user_id", "admin");
//		reportParams.put("company", param.get("company"));
//		reportParams.put("user_id", param.get("user_id"));
		reportParams.put("target_date", today);
		// reportParams.put("target_date", "2021-10-18");

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

		// ccr workcenter 개수
		int workcenterListCnt = workcenterService.workcenterReadCount(workcenterParams);
		// log.info(workcenterListCnt);

		// Pagination 객체생성
		Pagination pagination = new Pagination();
		pagination.pageInfo(page, range, workcenterListCnt);

		// log.info(pagination.getStartPage());
		// log.info(pagination.getEndPage());
		// log.info(pagination.getPageCnt());

		workcenterParams.put("startList", pagination.getStartList());
		workcenterParams.put("listSize", pagination.getListSize());

		Map<String, Object> workcenterListData = workcenterService.workcenterRead(workcenterParams);

		// log.info(workcenterListData.get("data").getClass().getName()); // ArrayList

		ArrayList<HashMap> workcenterList = (ArrayList<HashMap>) workcenterListData.get("data");

		// 버퍼확보율 리스트 없을 때 사용
//		Iterator<HashMap> i = workcenterList.iterator();
//		while (i.hasNext()) {
//			HashMap wc = i.next(); // must be called before you can call i.remove()
//			reportParams.put("workcenter", wc.get("workcenter"));
//			Map<String, Object> bufferListData = reportService.buffer(reportParams);
//			ArrayList<HashMap> bufferList = (ArrayList<HashMap>) bufferListData.get("data");
//			// 버퍼확보율 리스트가 없을 떄
//			if (bufferList.isEmpty()) {
//				//wc.put("empty", true); // empty 속성 추가
//				i.remove(); // 리스트에서 wc 삭제
//			}
//			wc.put("buffer", bufferList);
//		}
		
//		log.info(workcenterList.size());
		
		workcenterList.forEach(e -> {
			reportParams.put("workcenter", e.get("workcenter"));
			Map<String, Object> bufferListData = reportService.buffer(reportParams);
			ArrayList<HashMap> bufferList = (ArrayList<HashMap>) bufferListData.get("data");
			e.put("buffer", bufferList);
		});

		pagination.setWorkcenterListData(workcenterListData);

		// Map<String, Object> data = reportService.buffer(param);
		return new ResponseEntity<Pagination>(pagination, HttpStatus.OK);
	}

//	@PostMapping("/bufferList")
//	public ResponseEntity<Map<String, Object>> bufferList(@RequestParam Map<String, Object> param) {
//		Map<String, Object> data = reportService.bufferList(param);
//		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
//	}
//
//	@PostMapping("/ccrOnHand")
//	public ResponseEntity<Map<String, Object>> ccrOnHand(@RequestParam Map<String, Object> param) {
//		Map<String, Object> data = reportService.ccrOnHand(param);
//		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
//	}

}
