package data.academic.taskCenter.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import data.framework.support.AbstractValidatorController;
import data.framework.validation.Errors;
import data.academic.taskCenter.entity.EntityTaskInfo;
import data.academic.taskCenter.service.TaskReceiverService;

/**
 * OA-任务中心-任务中心控制器类。
 * 
 * @author JohnXU
 * 
 */
@Controller
@RequestMapping("taskManagement/taskCenter")
public class TaskCenterController extends AbstractValidatorController {
	@Autowired
	private TaskReceiverService taskReceiverService;

	@Override
	protected void validate(Object obj, Errors errors) {
	}

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
	}

	@RequestMapping(params = "command=loadTaskInfoById")
	public void loadTaskInfoInfoById(@RequestParam("data") String data, java.io.PrintWriter out) {
		Map<String, Object> requestMap = getSerializer().parseMap(data);
		EntityTaskInfo entityTaskInfo = taskReceiverService.loadEntityTaskInfo(formatString(requestMap.get("id")));
		out.print(getSerializer().formatObject(entityTaskInfo));
	}
}