package data.academic.examInfo.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import data.academic.examInfo.entity.EntityExamPaper;
import data.academic.examInfo.service.ExamInfoService;
import data.academic.examInfo.service.ExamPaperUploadService;
import data.academic.examNumberManagement.service.ExamNumberManageService;
import data.academic.noticeManage.service.NoticeBasicInfoService;
import data.academic.schoolManage.service.PoliticalInstructorService;
import data.academic.statisticsAnalysis.service.SchoolPlainAdminScoreSearchService;
import data.framework.data.DataSerializer;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractBaseController;
import data.framework.support.ConfigContext;
import data.framework.utility.FileHelper;
import data.framework.utility.UploadHelper;
import data.platform.authority.security.SecurityContext;
import data.platform.service.PlatformDataDictionaryService;

/**
 * @Title examUpload
 * @description 试卷管理-试卷上传
 * @author wangchaofa
 * @CreateDate Oct 25,2016
 */

@RestController
@RequestMapping("examInfo/examPaperUpload")
public class ExamPaperUploadController extends AbstractBaseController {

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	@Autowired
	@Value("#{implementsMap['dataSerializer']}")
	private DataSerializer serializer;

	@Autowired
	private ExamPaperUploadService examPaperUploadService;

	@Autowired
	private ExamInfoService examInfoService;

	@Autowired
	private PoliticalInstructorService politicalInstructorService;

	@Autowired
	private ExamNumberManageService examNumberManageService;

	@Autowired
	private SchoolPlainAdminScoreSearchService schoolPlainAdminScoreSearchService;

	@Autowired
	private PlatformDataDictionaryService dictionaryService;

	@Autowired
	private NoticeBasicInfoService noticeBasicInfoService;

	private static String uploadPath = UploadHelper.getRealUploadPath();
	private static String charSetName = ConfigContext.getStringSection("framework.web.charset");

	/**
	 * @Title searchExamPaper
	 * @Description 显示某个考试下所有的科目和试卷名称
	 * @param data
	 * @return void
	 * @author wangchaofa
	 * @CreateDate Oct 26,2016
	 * 
	 */
	@RequestMapping(params = "command=searchExamPaper")
	public void selectExamPaper(@RequestParam("data") String data, PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);

		String gradeCode = trimString(requestMap.get("gradeCode"));
		String examCode = trimString(requestMap.get("examCode"));
		requestMap.put("examCode", examCode);
		requestMap.put("gradeCode", gradeCode);

		List<Map<String, Object>> pagingResult = examPaperUploadService.getCourseByExamCode(requestMap);

		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> map : pagingResult) {
			map.put("examCode", examCode);
			map.put("course", map.get("Course"));
			List<Map<String, Object>> paperList = examPaperUploadService.serachExamPaper(map);

			map.put("FileList", paperList);

			paramList.add(map);

		}

		out.print(getSerializer().formatList(paramList));
	}

	/**
	 * @Title uploadExamPaper
	 * @Description 针对科目上传试卷附件
	 * @author wangchaofa
	 * @CreateDate Oct 26,2016
	 * @param request
	 * @param response
	 * @param files
	 * @throws Exception
	 */
	@RequestMapping(params = "command=upload")
	public void uploadExamPaper(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file") CommonsMultipartFile[] files) throws Exception {

		String examId = request.getParameter("examId");
		String examCode = request.getParameter("examCode");
		String course = request.getParameter("course");
		// String courseName = new
		// String(request.getParameter("courseName").getBytes("iso-8859-1"),"utf-8");
		// String examName = new
		// String(request.getParameter("examName").getBytes("iso-8859-1"),"utf-8");
		String courseName = request.getParameter("courseName");
		String examName = request.getParameter("examName");
		examName = examName + courseName + "试卷";
		String loginId = SecurityContext.getPrincipal().getUsername(); // 当前登录人的USER_UID
		String orgId = politicalInstructorService.selectOrgIdByLoginName(loginId);    //根据USER_UID获取组织id
		Map<String,Object> param = new HashMap();
		param.put("orgId", orgId);
		String orgCode = noticeBasicInfoService.getOrgCode(param);    //组织code 
		
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding(charSetName);
		Calendar today = Calendar.getInstance();
		String year = String.valueOf(today.get(Calendar.YEAR));
		String month = String.valueOf(today.get(Calendar.MONTH) + 1);

		String upPath = uploadPath + File.separator + year + File.separator + month + File.separator;
		File uploadFolder = new File(upPath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		List<FileItem> items = new ArrayList<FileItem>();
		for (int i = 0; i < files.length; i++) {
			CommonsMultipartFile commonsMultipartFile = files[i];
			items.add(commonsMultipartFile.getFileItem());
		}

		for (FileItem item : items) {
			String fileName = item.getName();
			if (fileName != null && fileName.length() != 0) {
				// 上传文件
				File fullFile = new File(fileName);
				String realName = fullFile.getName();
				String realExtName = FileHelper.getExtFromFileName(realName);
				String serverName = UploadHelper.createFileName(request, realExtName);
				String serverPath = upPath + serverName;
				realName = examName + "." + realExtName;
				File savedFile = new File(serverPath);
				item.write(savedFile);

				// 持久化文件信息
				EntityExamPaper fileEntity = new EntityExamPaper();
				fileEntity.setExamId(examId);
				fileEntity.setExamCode(examCode);
				fileEntity.setCourse(course);
				fileEntity.setFileName(realName);
				fileEntity.setFileNameExtension(realExtName);
				fileEntity.setFileNameInServer(serverName);
				fileEntity.setFilePathInServer(savedFile.getPath());
				fileEntity.setFileSize(item.getSize());
				// SecurityContext.getPrincipal().getId();
				fileEntity.setOperator(loginId);
				fileEntity.setUpdatePerson(loginId);
				fileEntity.setIsPublic("1");
				fileEntity.setSchoolCode(orgCode);
				
				examPaperUploadService.save(fileEntity);
				Map<String, String> map = new HashMap<String, String>();
				map.put("fileName", realName);
				//System.out.println("filePath" + realName);
				map.put("path", "");
				map.put("id", fileEntity.getId());
				map.put("filePathInServer", fileEntity.getFilePathInServer());
				map.put("fileNameExtension", fileEntity.getFileNameExtension());
				result.add(map);
			}
		}

		String oie = request.getParameter("oie");
		if (StringUtils.isNotBlank(oie)) {
			response.getWriter()
					.write("<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'></head><body>"
							+ serializer.formatList(result) + "</body></html>");
		} else {
			response.getWriter().write(serializer.formatList(result));
		}

	}

	/**
	 * @Title remove
	 * @Description 删除科目对应的试卷附件
	 * @author wangchaofa
	 * @CreateDate Oct 27,2016
	 * @param id
	 * @param out
	 */

	@RequestMapping(params = "command=remove")
	public void remove(@RequestParam("id") String id, PrintWriter out) {
		EntityExamPaper entity = examPaperUploadService.load(id);
		examPaperUploadService.remove(id);
		File file = new File(entity.getFilePathInServer());
		if (file.exists()) {
			file.delete();
		}
		out.print("success");

	}

	/**
	 * @Title download
	 * @Description 下载科目对应的试卷附件
	 * @author wangchaofa
	 * @CreateDate Oct 27,2016
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(params = "command=download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileId = request.getParameter("id");
		if (fileId != null && fileId.length() != 0) {
			EntityExamPaper fileEntity = examPaperUploadService.load(fileId);
			if (fileEntity != null) {
				UploadHelper.sendFile(response, fileEntity.getFilePathInServer(), fileEntity.getFileName(),
						"application/x-msdownload");
				return;
			}
		}
		response.getWriter().write("<script type=\"text/javascript\">alert('文件不存在！');</script>");
	}

	/**
	 * @Title showExamPaper
	 * @Description 门户页面显示前6条试卷信息
	 * @author wangchaofa
	 * @CreateDate Nov 4,2016
	 * @param
	 * @return
	 */
	@RequestMapping(params = "command=showPaper")
	public void showExamPaper(@RequestParam("data") String data, PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);

		String id = SecurityContext.getPrincipal().getId(); // 当前登录人的主键id
		String loginId = SecurityContext.getPrincipal().getUsername(); // 当前登录人的USER_UID
		String orgId = politicalInstructorService.selectOrgIdByLoginName(loginId); // 根据USER_UID获取单位code

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		String orgCode = noticeBasicInfoService.getOrgCode(map); // 组织code
		List<String> operatorList = new ArrayList<>();
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		if (!ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']").equals(orgCode)) { // 区级管理员、区级教研员
			operatorList.add(ConfigContext.getValue("framework.tmis.roleCode['qpOrgCode']"));
		}
		operatorList.add(orgCode);
		map.put("operatorList", operatorList);
		paramList = examPaperUploadService.getExamPaper(map);
		out.print(getSerializer().formatList(paramList));
	}

	/**
	 * @Title loadPaper
	 * @Description 加载试卷的公开状态（0-公开，1-不公开）
	 * @author wangchaofa
	 * @CreateDate Nov 8,2016
	 * @param data
	 * @return
	 */
	@RequestMapping(params = "command=loadPaper")
	public void loadPaper(@RequestParam("data") String data, PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String examCode = trimString(requestMap.get("examCode"));
		// String isPublic = trimString(requestMap.get("isPublic"));
		String courseCode = trimString(requestMap.get("courseCode"));
		requestMap.put("examCode", examCode);
		// requestMap.put("isPublic", isPublic);
		requestMap.put("courseCode", courseCode);

		String isPub = examPaperUploadService.loadPaper(requestMap);
		if (!"".equals(isPub) && !"null".equals(isPub) && null != isPub) {
			out.print(getSerializer().formatObject(isPub));
		} else {
			out.print(getSerializer().formatObject(""));
		}

	}

	/**
	 * @Title updatePaper
	 * @Description 修改试卷的公开状态（0-公开，1-不公开）
	 * @author wangchaofa
	 * @CreateDate Nov 4,2016
	 * @param data
	 */
	@RequestMapping(params = "command=updatePaper")
	public void updatePaper(@RequestParam("data") String data, PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		String examCode = trimString(requestMap.get("examCode"));
		String isPublic = trimString(requestMap.get("isPublic"));
		String courseCode = trimString(requestMap.get("courseCode"));
		requestMap.put("examCode", examCode);
		requestMap.put("isPublic", isPublic);
		requestMap.put("courseCode", courseCode);

		examPaperUploadService.updatePaper(requestMap);

		out.print("success");
	}

}
