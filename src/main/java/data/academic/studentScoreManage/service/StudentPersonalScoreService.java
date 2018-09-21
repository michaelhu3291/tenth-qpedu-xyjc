package data.academic.studentScoreManage.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;

import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.schoolQualityAnalysis.service.SchoolScoreService;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.AbstractService;
import data.framework.support.ConfigContext;
import data.framework.utility.ArithmeticUtil;
import data.platform.authority.security.SecurityContext;
@Service
public class StudentPersonalScoreService extends AbstractService {

	public List<HashMap<String, String>> searchNumberAndCode(Map<String, Object> requestMap) {
		return selectList("studentScoreManage.searchNumberAndCode",requestMap);
	}

	public List<HashMap<String, Object>> searchScoreList(Map<String, Object> requestMap) {
		return selectList("studentScoreManage.searchScoreList",requestMap);
	}

	public List<HashMap<String, String>> searchExamInfoList(Map<String, Object> requestMap) {
		return selectList("studentScoreManage.searchExamInfoList",requestMap);
	}

	public List<Double> searchCourseScoreList(HashMap<String, Object> courseAndExam) {
		return selectList("studentScoreManage.searchCourseScoreList",courseAndExam);
	}

	
	
	
}
