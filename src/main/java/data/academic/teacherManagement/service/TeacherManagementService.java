package data.academic.teacherManagement.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;

/**
 * @Title: TeacherManagementService
 * @Description: 教师管理服务层
 * @author zhaohuanhuan
 * @date 2016年7月29日 
 */
@Service
public class TeacherManagementService extends AbstractService {
	/**
	 * 分页查询
	 */
	public PagingResult<Map<String,Object>> searchPaging(Map<String,Object> param, String sortField, String sort, int currentPage, int pageSize ){
		if( StringUtils.isBlank( sortField ) )
            sortField = "School_Type_Sequence" ;
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "teacher.selectPaging", param, sortField, sort, currentPage, pageSize ) ;
    }
	/**
	 * 得到其他的学校，过滤当前学校
	 */
	public List<Map<String, String>> getSchool(List<String> ids){
		Map<String, Object> param=new HashMap<>();
		param.put("ids", ids);
		return selectList("refTeacherSchoool.getSchool",param);
	}
	
	
	/**
	 * 根据教师id得到对应的学校
	 * @param id
	 * @return
	 */
	public List<Map<String, String>> getSchoolByTeaId(String id){
		Map<String, String> param=new HashMap<String, String>();
		param.put("id", id);
		return selectList("refTeacherSchoool.getSchoolByTeaId",id);
	}
	/**
	 * 根据老师id删除老师所在的学校
	 */
	 /**
	 * @Title: removeSchool
	 * @Description: TODO
	 * @author zhaohuanhuan
	 * @date 2016年7月29日 
	 * @param id
	 * @return
	 * @return int
	 */
	public int removeSchool(String id )
	    {
	        Map<String,String> param = new HashMap<String,String>() ;
	        param.put( "id", id ) ;
	        return delete( "refTeacherSchoool.deleteSchoolByTeaId", param ) ;
	    }
	 /**
	 * @Title: teaReSchool
	 * @Description: 给当前老师添加学校
	 * @author zhaohuanhuan
	 * @date 2016年8月2日 
	 * @param teacherId
	 * @param schoolCode
	 * @return void
	 */
	public void teaReSchool( String teacherId,String schoolCode )
	 {
		 Map<String, String> param=new HashMap<>();
		 param.put("teacherId", teacherId);
		 param.put("schoolCode", schoolCode);
	   insert( "refTeacherSchoool.teaReSchool", param ) ;
     }
	 
	 
		/**
		 * @Title: getClass
		 * @Description: 得到其他的班级，过滤当前班级
		 * @author zhaohuanhuan
		 * @date 2016年8月2日 
		 * @param ids
		 * @return
		 * @return List<Map<String,String>>
		 */
		public List<Map<String, String>> getClass(List<String> ids){
			Map<String, Object> param=new HashMap<>();
			param.put("ids", ids);
			return selectList("classRefTeacher.getClass",param);
		}
		
		
		public List<Map<String, String>> getClassByIdAndSchoolCode(List<String> ids,String schoolCode){
			Map<String, Object> param=new HashMap<>();
			param.put("ids", ids);
			param.put("schoolCode", schoolCode);
			return selectList("classRefTeacher.getClassByIdAndSchoolCode",param);
		}
		
		/**
		 * 根据教师id得到对应的班级
		 * @param id
		 * @return
		 */
		public List<Map<String, String>> getClassByTeaId(String id){
			Map<String, String> param=new HashMap<String, String>();
			param.put("id", id);
			return selectList("classRefTeacher.getClassByTeaId",param);
		}
		
		
		/**
		 * @Title: getClassByTeaIdAndSchoolCode
		 * @Description: 根据教师id和学校code得到对应的班级
		 * @author zhaohuanhuan
		 * @date 2016年9月6日 
		 * @param id
		 * @param schoolCode
		 * @return
		 * @return List<Map<String,String>>
		 */
		public List<Map<String, String>> getClassByTeaIdAndSchoolCode(String id,String schoolCode){
			Map<String, String> param=new HashMap<String, String>();
			param.put("id", id);
			param.put("schoolCode", schoolCode);
			return selectList("classRefTeacher.getClassByTeaIdAndSchoolCode",param);
		}
		
		public String getSchoolCodeBysLoginName(String loginName){
			return selectOne("teacher.selectSchoolCodeByLoginName", loginName);
		}
		
		/**
		 * 根据老师id删除老师所在的班级
		 */
		 public int removeClass(Map<String,Object> paramMap)
		    {
		        return delete( "classRefTeacher.deleteClassByTeaId", paramMap ) ;
		    }
		 
		 
	     /**
		 * 给当前老师添加班级
		 */
		 public void teaReClass( String teacherId,String classId )
		 {
			 Map<String, String> param=new HashMap<>();
			 param.put("teacherId", teacherId);
			 param.put("classId", classId);
		   insert( "classRefTeacher.teaReClass", param ) ;
	     }
		 
		 
		 

		 /**
		 * @Title: removeGrade
		 * @Description: 根据老师id删除老师所在年级
		 * @author zhaohuanhuan
		 * @date 2016年9月7日 
		 * @param id
		 * @return
		 * @return int
		 */
		public int removeGrade(String id )
		    {
		        Map<String,String> param = new HashMap<String,String>() ;
		        param.put( "id", id ) ;
		        return delete( "gradeRefTeacher.deleteGradeByTeaId", param ) ;
		    }
		
		/**
		 * @Title: teaReGrade
		 * @Description: 给当前老师添加年级
		 * @author zhaohuanhuan
		 * @date 2016年9月7日 
		 * @param teacherId
		 * @param gradeId
		 * @return void
		 */
		public void teaReGrade( String teacherId,String gradeId )
		 {
			 Map<String, String> param=new HashMap<>();
			 param.put("teacherId", teacherId);
			 param.put("gradeId", gradeId);
		   insert( "gradeRefTeacher.teaReGrade", param ) ;
	     }
		
		
		/**
		 * @Title: getGradeByTeaId
		 * @Description: 根据教师id得到对应的年级
		 * @author zhaohuanhuan
		 * @date 2016年9月7日 
		 * @param id
		 * @return
		 * @return List<Map<String,String>>
		 */
		public List<Map<String, String>> getGradeByTeaId(String id){
			Map<String, String> param=new HashMap<String, String>();
			param.put("id", id);
			return selectList("gradeRefTeacher.getGradeByTeaId",id);
		}
		 
		 
		 
		    /**
			 * 得到其他的科目，过滤当前科目
			 */
			public List<Map<String, String>> getCourse(List<String> ids){
				Map<String, Object> param=new HashMap<>();
				param.put("ids", ids);
				return selectList("courseRefTeacher.getCourse",param);
			}
			/**
			 * 根据教师id得到对应的科目
			 * @param id
			 * @return
			 */
			public List<Map<String, String>> getCourseByTeaId(String id){
				Map<String, String> param=new HashMap<String, String>();
				param.put("id", id);
				return selectList("courseRefTeacher.getCourseByTeaId",param);
			}
			
			/**
			 * @Title: getCourseByTeaId
			 * @Description: 根据id和当前年查询老师所带的科目
			 * @author xiahuajun
			 * @date 2016年10月22日 
			 * @param map
			 * @return
			 * @return List<Map<String,String>>
			 */
			public List<Map<String, String>> getCourseByTeaIdAndYear(Map<String,String> map){
				//Map<String, String> param=new HashMap<String, String>();
				//param.put("id", id);
				return selectList("courseRefTeacher.getCourseByTeaId",map);
			}
			/**
			 * 根据老师id删除老师所在的科目
			 */
			 public int removeCourse(Map<String,Object> paramMap)
			    {
			        return delete( "courseRefTeacher.deleteCourseByTeaId", paramMap ) ;
			    }
		     /**
			 * 给当前老师添加科目
			 */
			 public void teaReCourse(Map<String,Object>  param )
			 { 
			   insert( "courseRefTeacher.teaReCourse", param ) ;
		     }
			 
			 
			 /**
			 * @Title: getSchoolCodeByLoginName
			 * @Description: 根据教师登录名获取学校code 
			 * @author zhaohuanhuan
			 * @date 2016年8月11日 
			 * @param loginName
			 * @return
			 * @return List<Map<String,Object>>
			 */
			public List<Map<String, Object>> getSchoolCodeByLoginName(String loginName){
					Map<String, String> param = new HashMap<>();
					param.put("loginName", loginName);
					return selectList("getInfoByLoginName.getSchoolCodeByLoginName", param);
				}
			
			
			/**
			 * @Title: getSchoolTypeByTeaPkId
			 * @Description: 根据选中的老师id得到学校类型标识
			 * @author zhaohuanhuan
			 * @date 2016年8月24日 
			 * @param teaPkId
			 * @return
			 * @return List<Map<String,String>>
			 */
			public List<Map<String, String>> getSchoolTypeByLoginName(String loginName){
				Map<String, String> param=new HashMap<String, String>();
				param.put("loginName", loginName);
				return selectList("courseRefTeacher.getSchoolTypeSequenceByLoginName",param);
			}
			
			/**
			 * @Title: getCourseByIdAndCoursePid
			 * @Description: 根据科目的父id和当前老师的id 过滤该老师已有的科目，得到带选择的科目
			 * @author zhaohuanhuan
			 * @date 2016年8月24日 
			 * @param ids
			 * @param CoursePid
			 * @return
			 * @return List<Map<String,String>>
			 */
			public List<Map<String, String>> getCourseByIdAndCoursePid(List<String> ids ,String CoursePid){
				Map<String, Object> param=new HashMap<>();
				param.put("ids", ids);
				param.put("CoursePid", CoursePid);
				return selectList("courseRefTeacher.getCourseByIdAndCoursePid",param);
			}
			
			/**
			 * @Title: getSchoolTypeAndName
			 * @Description: 根据登录用户显示其所属的学校类型和学校名称
			 * @author xiahuajun
			 * @date 2016年8月31日 
			 * @param username
			 * @return
			 * @return Map<String,Object>
			 */
			public List<Map<String, Object>> getSchoolTypeAndName(String username) {
				return selectList("getInfoByLoginName.getSchoolTypeAndNameByUsername", username);
			}
			
			
			/**
			 * @Title: selectSchoolTypeList
			 * @Description: //查询学校类型(小学，初中，高中)
			 * @author xiahuajun
			 * @date 2016年9月7日 
			 * @param string
			 * @return
			 * @return List<Map<String,Object>>
			 */
			public List<Map<String, Object>> selectSchoolTypeList(String schoolType) {
				return selectList("platformDataDictionary.selectSchoolTypeForAdmin",schoolType);
			}
			
			/**
			 * @Title: submitApply
			 * @Description: 本校管理员提交调度老师
			 * @author xiahuajun
			 * @date 2016年9月19日 
			 * @param paramMap
			 * @return void
			 */
			public void submitApply(Map<String, Object> paramMap) {
				update("teacher.updateTeacherState", paramMap);
			}
			
			/**
			 * @Title: findIsSubmitApply
			 * @Description: 撤销前先查询有没有未提交的调度
			 * @author xiahuajun
			 * @date 2016年9月19日 
			 * @param paramMap
			 * @return
			 * @return List<Map<String,Object>>
			 */
			public List<Map<String, Object>> findIsSubmitApply(Map<String, Object> paramMap) {
				// TODO Auto-generated method stub
				return selectList("teacher.selectIsSubmit",paramMap);
			}
			
			/**
			 * @Title: cancelSubmitApply
			 * @Description: 撤销调度申请
			 * @author xiahuajun
			 * @date 2016年9月19日 
			 * @param paramMap
			 * @return void
			 */
			public void cancelSubmitApply(Map<String, Object> paramMap) {
				update("teacher.cancelSubmitApply", paramMap);
			}
			
			/**
			 * @Title: addCreatePerson
			 * @Description: 添加申请人和申请时间
			 * @author xiahuajun
			 * @date 2016年9月19日 
			 * @param paramMap
			 * @return void
			 */
			public void addCreatePerson(Map<String, Object> paramMap) {
				
				update("teacher.addCreatePerson", paramMap);
			}
			
			/**
			 * @Title: findIsAlreadyTransfer
			 * @Description: 调动前先查询此老师有没有被调走
			 * @author xiahuajun
			 * @date 2016年9月20日 
			 * @param paramMap
			 * @return
			 * @return List<Map<String,Object>>
			 */
			public List<Map<String, Object>> findIsAlreadyTransfer(Map<String, Object> paramMap) {
				// TODO Auto-generated method stub
				return selectList("teacher.selectIsAlreadyTransfer",paramMap);
			}
			
			/**
			 * @Title: findIsAlreadyApply
			 * @Description: 防止重复申请
			 * @author xiahuajun
			 * @date 2016年9月21日 
			 * @param paramMap
			 * @return
			 * @return List<Map<String,Object>>
			 */
			public List<Map<String, Object>> findIsAlreadyApply(Map<String, Object> paramMap) {
				// TODO Auto-generated method stub
				return selectList("teacher.selectIsRepeatApply",paramMap);
			}
			
			/**
			 * @Title: selectRoleCodeByLoginName
			 * @Description: 根据登录名查询角色Code
			 * @author xiahuajun
			 * @date 2016年10月18日 
			 * @param loginName
			 * @return
			 * @return String
			 */
			public String selectRoleCodeByLoginName(String loginName) {
				return selectOne("historyScore.selectRoleCodeByLoginName",loginName);
			}
			
			/**
			 * @Title: getClassByTeaPk
			 * @Description: 得到当前老师所在班级
			 * @author xiahuajun
			 * @date 2016年10月20日 
			 * @param id
			 * @return
			 * @return List<Map<String,String>>
			 */
			public List<Map<String, String>> getClassByTeaPk(String id,String currentYear) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", id);
				map.put("currentYear", currentYear);
				return selectList("teacher.selectClassByTeaPk",map);
			}
			
			/**
			 * @Title: selectAllClassBySchoolCode
			 * @Description: 通过school_code查询该学校所有班级和年级
			 * @author xiahuajun
			 * @date 2016年10月20日 
			 * @param schoolCode
			 * @return
			 * @return List<Map<String,String>>
			 */
			public List<Map<String, String>> selectAllClassBySchoolCode(Map<String, Object> map) {
				return selectList("teacher.selectAllClassBySchoolCode",map);
			}
			
			/**
			 * @Title: addClassForTeacher
			 * @Description: 给老师添加班级
			 * @author xiahuajun
			 * @date 2016年10月20日 
			 * @param list
			 * @return void
			 */
			public void addClassForTeacher(Map<String, Object> map) {
				insert("classRefTeacher.addClassForTeacher",map);
			}
			
			/**
			 * @Title: selectClassByGrade
			 * @Description: 查询老师所带的班级(年级多选)
			 * @author xiahuajun
			 * @date 2016年10月22日 
			 * @param requestMap
			 * @return
			 * @return List<Map<String,String>>
			 */
			public List<Map<String, String>> selectClassByGrade(Map<String, Object> requestMap) {
				return selectList("classRefTeacher.selectClassByGrade",requestMap);
			}
			
			/**
			 * @Title: getCourseByTeaIdAndYear
			 * @Description: 根据id和当前年得到当前老师所拥有的科目
			 * @author xiahuajun
			 * @date 2016年10月22日 
			 * @param id
			 * @return
			 * @return List<Map<String,String>>
			 */
			public List<Map<String, String>> getCourseByTeaIdAndYear(String id,String currentYear) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", id);
				map.put("currentYear", currentYear);
				return selectList("courseRefTeacher.getCourseByTeaIdAndYear",map);
			}
			
			/**
			 * @Title: selectCoursesBySchoolType
			 * @Description: 通过学校类型查询科目(过滤掉老师已选的科目)
			 * @author xiahuajun
			 * @date 2016年10月26日 
			 * @param paramMap
			 * @return
			 * @return List<Map<String,String>>
			 */
			public List<Map<String, String>> selectCoursesBySchoolType(Map<String, Object> paramMap) {
				return selectList("platformDataDictionary.selectCoursesBySchoolType",paramMap);
			}
			
}
