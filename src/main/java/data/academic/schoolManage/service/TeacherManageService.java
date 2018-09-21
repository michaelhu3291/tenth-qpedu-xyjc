package data.academic.schoolManage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
@Service
public class TeacherManageService extends AbstractService{
	
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
		/**
		 * 根据教师id得到对应的班级
		 * @param id
		 * @return
		 */
		public List<Map<String, String>> getClassByTeaId(String id,String currentYear){
			Map<String, String> param=new HashMap<String, String>();
			param.put("id", id);
			param.put("currentYear", currentYear);
			return selectList("classRefTeacher.getClassByTeaIdAndYear",param);
		}
		/**
		 * 根据老师id删除老师所在的班级
		 */
		 public int removeClass(String id )
		    {
		        Map<String,String> param = new HashMap<String,String>() ;
		        param.put( "id", id ) ;
		        return delete( "classRefTeacher.deleteClassByTeaId", param ) ;
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
				return selectList("courseRefTeacher.getCourseByTeaId",id);
			}
			/**
			 * 根据老师id删除老师所在的科目
			 */
			 /*public int removeCourse(String id )
			    {
			        Map<String,String> param = new HashMap<String,String>() ;
			        param.put( "id", id ) ;
			        return delete( "courseRefTeacher.deleteCourseByTeaId", param ) ;
			    }*/
		     /**
			 * 给当前老师添加科目
			 */
			 public void teaReCourse( String teacherId,String courseId,String teacherType )
			 {
				 Map<String, String> param=new HashMap<>();
				 param.put("teacherId", teacherId);
				 param.put("courseId", courseId);
				 param.put("teacherType", teacherType);
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
			 * @Title: addTeacher
			 * @Description: 添加教师
			 * @author xiahuajun
			 * @date 2016年9月4日 
			 * @param paramMap
			 * @return void
			 */
			public void addTeacher(Map<String, Object> paramMap) {
				insert("teacher.addTeacher", paramMap);
			}
			
			/**
			 * @Title: selectTeacherPkByLoginName
			 * @Description: 根据登录名查询teacher_pk
			 * @author xiahuajun
			 * @date 2016年9月4日 
			 * @param string
			 * @return
			 * @return String
			 */
			public String selectTeacherPkByLoginName(String loginName) {
				// TODO Auto-generated method stub
				return selectOne("teacher.selectTeacherPkByLoginName", loginName);
			}
			
			/**
			 * @Title: selectSchoolCodeByLoginName
			 * @Description: 查询当前用户的学校code(根据登录名)
			 * @author xiahuajun
			 * @date 2016年9月4日 
			 * @param username
			 * @return
			 * @return String
			 */
			public String selectSchoolCodeByLoginName(String username) {
				return selectOne("teacher.selectSchoolCodeByLoginName", username);
			}
			
			/**
			 * @Title: addRefTeacherSchool
			 * @Description: 添加教师学校关系表
			 * @author xiahuajun
			 * @date 2016年9月4日 
			 * @param paramMap
			 * @return void
			 */
			public void addRefTeacherSchool(Map<String, Object> paramMap) {
				insert("teacher.addRefTeacherSchool", paramMap);
				
			}
			
			/**
			 * @Title: selectTeacherByChineseName
			 * @Description: 根据姓名查询外校老师信息
			 * @author xiahuajun
			 * @date 2016年9月5日 
			 * @param paramMap
			 * @return
			 * @return List<Map<String,Object>>
			 */
			public List<Map<String, Object>> selectTeacherByChineseName(Map<String, Object> paramMap) {
				return selectList("teacher.selectTeacherByChineseName", paramMap);
			}
			
			/**
			 * @Title: remove
			 * @Description: 删除老师
			 * @author xiahuajun
			 * @date 2016年9月5日 
			 * @param map
			 * @return void
			 */
			public int remove(Map<String, Object> map) {
				List<String> list = (List<String>) map.get("selArr");
				for(String li : list){
					map.put("li", li);
					//查询状态判断是社招还是校招1为社招2为调出
					String state = selectOne("teacher.selectState", map);
					if("4".equals(state)){
						//删除关系表
						delete("teacher.deleteTeacher",map);
					}
					else if("1".equals(state)){
						//删除关系表
						delete("teacher.deleteTeacher",map);
						//删除教师表
						delete("teacher.deleteMainTeacher",map);
					}
					else {
						return 1;
					}
				}
				return 0;
				
			}
			
			/**
			 * @Title: selectSchoolTypeByLoginName
			 * @Description: 根据用户名查询学校类型
			 * @author xiahuajun
			 * @date 2016年9月6日 
			 * @param username
			 * @return
			 * @return String
			 */
			public String selectSchoolTypeByLoginName(String schoolCode) {
				// TODO Auto-generated method stub
				return selectOne("teacher.selectSchoolTypeByLoginName", schoolCode);
			}
			
			/**
			 * @Title: selectCoursesByScholType
			 * @Description: 根据学校类型查询科目
			 * @author xiahuajun
			 * @date 2016年9月6日 
			 * @param schoolType
			 * @return
			 * @return List<Map<String,Object>>
			 */
			public List<Map<String, Object>> selectCoursesByScholType(String schoolType) {
				return selectList("platformDataDictionary.selectCoursesBySchoolCode", schoolType);
			}

			public List<Map<String, Object>> selectTeacherIsExist(Map<String, Object> paramMap) {
				return selectList("teacher.selectTeacherIsExist", paramMap);
			}
			
			/**
			 * @Title: getCourseByTeaId
			 * @Description: 根据当前老师的id得到所拥有的科目集合
			 * @author xiahuajun
			 * @date 2016年10月22日 
			 * @param teacherIds
			 * @param currentYear
			 * @return
			 * @return List<Map<String,String>>
			 */
			public List<Map<String, String>> getCourseByTeaId(String teacherIds, String currentYear) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", teacherIds);
				map.put("currentYear", currentYear);
				return selectList("courseRefTeacher.getCourseByTeaIdAndYear", map);
			}
			
			/**
			 * @Title: addBuaUserInfo
			 * @Description: 添加BUA_USER表中添加数据
			 * @author xiahuajun
			 * @date 2016年10月26日 
			 * @param paramMap
			 * @return void
			 */
			public void addBuaUserInfo(Map<String, Object> paramMap) {
				insert("teacher.addBuaUserInfo",paramMap);
				
			}
			
			/**
			 * @Title: selectRoleIdByRoleCode
			 * @Description: 通过role_code查询role_id
			 * @author xiahuajun
			 * @date 2016年10月26日 
			 * @param string
			 * @return
			 * @return String
			 */
			public String selectRoleIdByRoleCode(String roleCode) {
				return selectOne("teacher.selectRoleIdByRoleCode",roleCode);
			}
			
			/**
			 * @Title: addRefBuaUserInfo
			 * @Description: 添加Ref_Role_User表中添加数据
			 * @author xiahuajun
			 * @date 2016年10月26日 
			 * @param paramMap
			 * @return void
			 */
			public void addRefBuaUserInfo(Map<String, Object> paramMap) {
				insert("teacher.addRefBuaUserInfo",paramMap);
				
			}
			
			/**
			 * @Title: removeRefRoleUser
			 * @Description: 删除ref_role_user关系表
			 * @author xiahuajun
			 * @date 2016年10月26日 
			 * @param map
			 * @return void
			 */
			public void removeRefRoleUser(Map<String, Object> map) {
				delete("teacher.removeRefRoleUser",map);
			}
			
			/**
			 * @Title: removeBuaUser
			 * @Description: 删除bua_user表
			 * @author xiahuajun
			 * @date 2016年10月26日 
			 * @param map
			 * @return void
			 */
			public void removeBuaUser(Map<String, Object> map) {
				delete("teacher.removeBuaUser",map);
			}
			
			
	
}