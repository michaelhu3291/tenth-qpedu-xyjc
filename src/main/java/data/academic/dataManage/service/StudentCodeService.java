package data.academic.dataManage.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import data.framework.support.AbstractService;
@Service
public class StudentCodeService extends AbstractService{
	
	/**
	 * @Title: insertSmallTitle
	 * @Description: 
	 * @author xiahuajun导入学籍号和考号
	 * @date 2016年9月1日 
	 * @param list
	 * @return void
	 */
	public void insertStuCodeAndExamCode(List<Map<String,Object>> list){
			insert("studentCodeAndExamCode.insertStuCodeExamCode",list);
		
	}
	
	/**
	 * @Title: deleteStuCode
	 * @Description: 防止重复导入，导入前删除
	 * @author xiahuajun
	 * @date 2016年9月1日 
	 * @param map
	 * @return void
	 */
	public void deleteStuCode(Map<String, Object> map) {
		delete("studentCodeAndExamCode.deleteStuCode", map);
		
	}

}
