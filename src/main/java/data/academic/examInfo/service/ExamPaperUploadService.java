package data.academic.examInfo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import data.academic.examInfo.entity.EntityExamPaper;
import data.framework.support.AbstractService;
/**
 * @Title examPaperUpload
 * @Description 试卷管理-试卷上传
 * @author wangchaofa
 * @CreateDate Oct 26,2016
 */
@Service
public class ExamPaperUploadService extends AbstractService{

	/**
	 * @Title serachExamPaper
	 * @Description 显示某个考试下所有的科目和试卷
	 * @author wangchaofa
	 * @CreateDate Oct 26,2016
	 * @param map
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> serachExamPaper(Map<String, Object> map){
		return selectList( "examPaper.getExamPaperByCourse", map) ;
	}
	
	
	
	/**
	 * @Title getCourseByParentDictionaryserachCoursePaging
	 * @Description 根据父节点加载科目
	 * @author wangchaofa
	 * @CreateDate Oct 26,2016
	 * @param map
	 * @return List<Map<String, Object>>
	 */
	public  List<Map<String, Object>> getCourseByExamCode(Map<String, Object> map) {

		return  selectList( "examPaper.getCoursesByExamCode", map) ;
	}
	
	
	
    /**
     * @Title load
     * @Description 加载试卷附件信息
     * @author wangchaofa
     * @CreateDate Oct 26,2016
     * @param id
     * @return EntityExamPaper
     */
	public EntityExamPaper load(String id){
		Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "id", id ) ;
        return selectOne( "examPaper.loadExamPaper", id ) ;
	}
	
	
	
	/**
     * @Title save
     * @Description 保存試卷附件信息。
     * @author wangchaofa
     * @CreateDate Oct 26,2016
     * @param entity 試卷附件
     */
	public void save(EntityExamPaper entity){
		insert("examPaper.insertExamPaper", entity);
	}
	
	
	
	/**
	 * @Title remove
	 * @Description 删除试卷信息
	 * @author wangchaofa
	 * @CreateDate Oct 26,2016
	 * @param id 试卷id
	 * 
	 */
	public void remove(String id){
		Map<String,Object> param = new HashMap<String,Object>() ;
        param.put( "id", id ) ;
        delete( "examPaper.deletePaper", param ) ;        
	}
	
	
	
	/**
	 * @Title getIdByCode
	 * @Description 根据组织id得到主键id
	 * @author wangchaofa
	 * @CreateDate Nov 4,2016
	 * @param Map<String,Object>
	 * @return List<Map<String,Object>>
	 * 
	 */
	public List<Map<String,Object>> getIdByCode(Map<String,Object> map){
		return selectList("examPaper.getIdByCode", map);
	}
	

	
	/**
	 * @Title getExamPaper
	 * @Description 门户页面加载前6条试卷的信息
	 * @author wangchaofa
	 * @CreatreDate Nov 4,2016
	 * @param Map<String,Object>
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getExamPaper(Map<String,Object> map){
		return selectList( "examPaper.getExamPaper", map) ;
	}
	
	
	
	/**
	 * @Title selectExamPapers
	 * @Description 门户页面加载所有试卷的信息
	 * @author wangchaofa
	 * @CreatreDate Nov 4,2016
	 * @param Map<String,Object>
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectExamPapers(Map<String,Object> map){
		return selectList( "examPaper.selectExamPapers", map) ;
	}
	
	
	
	/**
	 * @Title loadPaper
	 * @Description 加载试卷的公开状态
	 * @author wangchaofa
	 * @CreateDate Nov 8,2016
	 * @param map
	 * @return String
	 */
	public String loadPaper(Map<String,Object> map){
		return selectOne("examPaper.loadPaper", map);
	}
	
	
	
	/**
	 * @Title updatePaper
	 * @Description 修改试卷的公开状态（0-公开，1-不公开）
	 * @author wangchaofa
	 * @CreateDate Nov 4,2016
	 * @param
	 */
	public void updatePaper(Map<String,Object> map){
		update("examPaper.updatePaper",map);
	}
	
	
	
}
