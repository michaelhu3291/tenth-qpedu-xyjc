package data.academic.test.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import data.academic.test.service.EchartsService;
import data.framework.support.AbstractBaseController;

/**
 * @Title: EchartsController
 * @Description: echarts3.0版本测试
 * @author huxian
 * @date 2016年8月19日
 */
@RestController
@RequestMapping("echarts")
public class EchartsController extends AbstractBaseController {
	@Autowired
	private EchartsService echartsService;

	@Override
	protected void init(ModelMap model, HttpServletRequest request) {

	}

	@RequestMapping(params = "command=getSchoolNumber")
	public void getSchoolNumber(java.io.PrintWriter out) {
		List<Map<String, Object>> list = echartsService.getSchoolNumber();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("data", list);
		out.print(getSerializer().formatMap(resultMap));
	}
}
