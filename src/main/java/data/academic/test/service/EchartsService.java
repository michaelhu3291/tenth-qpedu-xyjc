package data.academic.test.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import data.framework.support.AbstractService;

/**
 * @Title: EchartsService
 * @Description: echarts3.0版本测试
 * @author huxian
 * @date 2016年8月19日 
 */
@Service
public class EchartsService extends AbstractService {
	
	/**
	 * @Title: getSchoolNumber
	 * @Description: TODO
	 * @author huxian
	 * @date 2016年8月19日 
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> getSchoolNumber(){
		Map<String, String> param = new HashMap<>();
		return selectList("echarts.getSchoolNumber", param);
	}
}
