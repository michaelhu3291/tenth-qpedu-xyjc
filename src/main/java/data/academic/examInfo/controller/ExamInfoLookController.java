package data.academic.examInfo.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.academic.examInfo.service.ExamInfoLookService;
import data.academic.examInfo.service.ExamInfoService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.statisticsAnalysis.service.SchoolPlainAdminScoreSearchService;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformDataDictionaryService;

/**
 * @Description 门户页面考试的详情显示
 * @author wangchaofa
 * @CreateDate Nov 7,2016
 */
@RestController
@RequestMapping("examInfo/examInfoLook")
public class ExamInfoLookController extends AbstractBaseController {

	String examNumber = "";
	@Autowired
	private ExamInfoLookService examInfoLookService;
	@Autowired
	private ExamInfoService examInfoService;
	@Autowired
	private ExamNumberManageService examNumberManageService;
	@Autowired
	private SchoolPlainAdminScoreSearchService schoolPlainAdminScoreSearchService;
	@Autowired
	private PlatformDataDictionaryService dictionaryService;
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		examNumber = parseString(request.getParameter("examNumber"));
	}

	/**
	 * @Title showExam
	 * @Description 门户页面展示考试信息
	 * @author wangchaofa
	 * @CreateDate Nov 7,2016
	 * @param data
	 * @return
	 */
	@RequestMapping(params = "command=showExam")
	public void showExam(@RequestParam("data") String data, PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);

		String id = SecurityContext.getPrincipal().getId(); // 当前登录人的主键id
		String schoolCode = "";
		String roleCode = examInfoService.getRoleByUserId(id);
		List<String> gradeIdList = new ArrayList<>();
		List<String> schoolCodeList = new ArrayList<>();
		if (ConfigContext.getValue("framework.tmis.roleCode['qpRoleCode']").contains(roleCode)) {
			schoolCodeList.add(ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']"));
		} else {
			if ("".equals(parseString(requestMap.get("schoolCode")))) {
				// 根据登录名得到学校code
				schoolCode = examNumberManageService
						.getSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
			} else {
				schoolCode = parseString(requestMap.get("schoolCode"));
			}
			// 根据当前登录的用户名得到学校code用于得到年级
			String schoolCodes = examNumberManageService
					.getSchoolCodeByLoginName(SecurityContext.getPrincipal().getUsername());
			// 根据用户名得到学校类型
			String schoolSequence = schoolPlainAdminScoreSearchService.findSchoolTypeBySchoolCode(schoolCodes);
			String schoolTypeCode = "";
			if ("0".equals(schoolSequence)) {
				schoolTypeCode = "xx";
			} else if ("1".equals(schoolSequence)) {
				schoolTypeCode = "cz";
			} else if ("2".equals(schoolSequence) || "3".equals(schoolSequence) || "4".equals(schoolSequence)) {
				schoolTypeCode = "gz";
				if ("3062".equals(schoolCodes)) {
					schoolTypeCode = "cz";
				}
				if ("3004".equals(schoolCodes)) {
					schoolTypeCode = "wz";
				}
			} else if ("5".equals(schoolSequence)) {
				schoolTypeCode = "ygz";
			} else if ("".equals(schoolSequence) || "null".equals(schoolSequence) || null == schoolSequence) {
				schoolCode = ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']");
			}
			schoolCodeList.add(schoolCode);
			schoolCodeList.add(ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']"));
			// 根据学校类型查询年级
			List<Map<String, Object>> gradeList = dictionaryService.getGradesBySchoolType(schoolTypeCode);
			for (Map<String, Object> map : gradeList) {
				String gradeId = map.get("DictionaryCode").toString();
				gradeIdList.add(gradeId);
			}
		}

		requestMap.put("gradeIdList", gradeIdList);
		requestMap.put("schoolCodeList", schoolCodeList);
		List<Map<String, Object>> paramList=new ArrayList<>();
		if(ConfigContext.getValue("framework.tmis.roleCode['qpRoleCode']").contains(roleCode)){
			paramList= examInfoLookService.showExam(requestMap);

		}else{
			requestMap.put("schoolCode", schoolCode);
			paramList=examInfoLookService.getExamForSchool(requestMap);
		}
		 
		out.print(getSerializer().formatList(paramList));
	}

}
