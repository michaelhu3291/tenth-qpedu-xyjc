package data.academic.examPaper.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
@Service
public class ExamPaperManageService extends AbstractService{
	
	/**
	 * @Title: addExam
	 * @Description: 添加试卷
	 * @author xiahuajun
	 * @date 2016年8月14日 
	 * @param map
	 * @return void
	 */
	public void addExamPaper(Map<String,Object> map){
		insert("examPaper.addExamPaper", map);
	}
	
	/**
	 * @Title: serachExamPaperPaging
	 * @Description: 分页查询
	 * @author xiahuajun
	 * @date 2016年8月14日 
	 * @param requestMap
	 * @param sortField
	 * @param sort
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @return PagingResult<Map<String,Object>>
	 */
	public PagingResult<Map<String, Object>> serachExamPaperPaging(Map<String, Object> requestMap, String sortField,
			String sort, int currentPage, int pageSize) {
		if( StringUtils.isBlank(sortField)){
			sortField = "Create_Time" ;
		}
        if( StringUtils.isBlank( sort )){
        	sort = "desc" ;
        }
		return  selectPaging( "examPaper.selectExamPaperPaging", requestMap, sortField, sort, currentPage, pageSize ) ;
	}
	
	/**
	 * @Title: remove
	 * @Description:删除试卷
	 * @author xiahuajun
	 * @date 2016年8月14日 
	 * @param string
	 * @return void
	 */
	public void remove(Map<String, Object> map) {
		delete("examPaper.deleteExamPaper",map);
	}
	
	/**
	 * @Title: selectExamPaperById
	 * @Description: 修改回显
	 * @author xiahuajun
	 * @date 2016年8月14日 
	 * @param paramMap
	 * @return
	 * @return Map<String,Object>
	 */
	public Map<String, Object> selectExamPaperById(Map<String, Object> paramMap) {
		return selectOne("examPaper.selectExamPaperById", paramMap);
	}
	
	/**
	 * @Title: UpdateExamPaperById
	 * @Description: 修改试卷
	 * @author xiahuajun
	 * @date 2016年8月14日 
	 * @param paramMap
	 * @return void
	 */
	public void UpdateExamPaperById(Map<String, Object> paramMap) {
		super.update("examPaper.UpdateExamPaperById", paramMap);
		
	}

}
