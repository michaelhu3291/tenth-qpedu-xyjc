package data.academic.transfer.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import data.academic.transfer.service.StudentTransferService;
import data.framework.support.AbstractBaseController;

 
@Component
@Aspect
public class SystemMessageController extends AbstractBaseController 
{
	@Override
	protected void init(ModelMap model, HttpServletRequest request) {
	}
	
	/**
	 * @Title: stuAndTeacherTransfer
	 * @Description: 抓取申请调动学生信息
	 * @author zhaohuanhuan
	 * @date 2016年12月7日 
	 * @param point
	 * @return void
	 */
	@AfterReturning(pointcut="execution(* data.academic.transfer.service.StudentTransferService.submitApply(..))" )
	public void studentTransfer(JoinPoint point) {
		    //获取传入触发抓取事件的service的参数
			Object[] args = point.getArgs();
			StringBuffer sb=new StringBuffer();
			Map<String, Object> map = new ConcurrentHashMap<String, Object>();
			Map<String, Object> requestMap=new ConcurrentHashMap<String, Object>();
			if(args.length!=0)
			{
				 map =(Map<String, Object>) args[0];
				 List<String> pkIdList=(List<String>) map.get("selArr");
				 map.put("pkId",pkIdList.get(0));
				 requestMap.put("pkId", map.get("pkId"));
				 String name=studentTransferService.getStudentByPid(requestMap);
				 requestMap.put("roleState",0);
				 requestMap.put("name",name);
				 requestMap.put("create_person", map.get("create_person"));
				 requestMap.put("newadr", map.get("newadr"));
				 requestMap.put("create_time", map.get("create_time"));
				 requestMap.put("schoolCode", map.get("schoolCode"));
				 sb.append("申请调动：(").
				 append(name).
				 append(") 学生到  ").
				 append( map.get("newadr"));	
				 requestMap.put("title", sb.toString());
				 studentTransferService.insertTaskRamking(requestMap);
			}
			
	}
	
	/**
	 * @Title: teacherTransfer
	 * @Description: 抓取申请调动老师信息
	 * @author zhaohuanhuan
	 * @date 2016年12月7日 
	 * @param point
	 * @return void
	 */
	@AfterReturning(pointcut="execution(* data.academic.teacherManagement.service.TeacherManagementService.submitApply(..))" )
	public void teacherTransfer(JoinPoint point) {
		    //获取传入触发抓取事件的service的参数
			Object[] args = point.getArgs();
			StringBuffer sb=new StringBuffer();
			Map<String, Object> map = new ConcurrentHashMap<String, Object>();
			Map<String, Object> requestMap=new ConcurrentHashMap<String, Object>();
			if(args.length!=0)
			{
				 map =(Map<String, Object>) args[0];
				 List<String> pkIdList=(List<String>) map.get("selArr");
				 map.put("pkId",pkIdList.get(0));
				 requestMap.put("pkId", map.get("pkId"));
				 String name=studentTransferService.getTeacherByPid(requestMap);
				 requestMap.put("name",name);
				 requestMap.put("roleState",1);
				 requestMap.put("create_person", map.get("create_person"));
				 requestMap.put("newadr", map.get("newadr"));
				 requestMap.put("create_time", map.get("create_time"));
				 requestMap.put("schoolCode", map.get("schoolCode"));
				 sb.append("申请调动：(").
				 append(name).
				append(") 老师到  ").
				append( map.get("newadr"));	
				 requestMap.put("title", sb.toString());
				 studentTransferService.insertTaskRamking(requestMap);
			}
	}
	
	/**
	 * @Title: deleteStuTransfer
	 * @Description:  抓取区级管理员同意学生申请时的数据
	 * @author zhaohuanhuan
	 * @date 2016年12月7日 
	 * @param point
	 * @return void
	 */
	@AfterReturning(pointcut="execution(* data.academic.transfer.service.StudentTransferService.insertRefStudentSchool(..))" )
	public void deleteStuTransfer(JoinPoint point) {
		    //获取传入触发抓取事件的service的参数
			Object[] args = point.getArgs();
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> requestMap = new HashMap<String, Object>();
			if(args.length!=0)
			{
				 map =(Map<String, Object>) args[0];
				 List<String> pkIdList=new ArrayList<>();
				 pkIdList.add(map.get("studentPk").toString());
				 requestMap.put("pkIdList", pkIdList);
				 studentTransferService.removeTransferPson(requestMap);
			}
	}
	
	
	/**
	 * @Title: deleteStuTransferUpdate
	 * @Description: 抓取区级管理员同意学生申请时的数据
	 * @author zhaohuanhuan
	 * @date 2016年12月7日 
	 * @param point
	 * @return void
	 */
	@AfterReturning(pointcut="execution(* data.academic.transfer.service.StudentTransferService.updateStudentState(..))" )
	public void deleteStuTransferUpdate(JoinPoint point) {
		    //获取传入触发抓取事件的service的参数
			Object[] args = point.getArgs();
			StringBuffer sb=new StringBuffer();
			sb.append(args[0]);
			//Map<String, Object> map = new ConcurrentHashMap<String, Object>();
			Map<String, Object> requestMap=new ConcurrentHashMap<String, Object>();
			if(args.length!=0)
			{
				 List<String> pkIdList=new ArrayList<>();
				 pkIdList.add(sb.toString());
				 requestMap.put("pkIdList", pkIdList);
				 studentTransferService.removeTransferPson(requestMap);
			}
	}
	
	
	/**
	 * @Title: deleteTeacherTransferInsert
	 * @Description: 抓取区级管理员同意老师申请时的数据
	 * @author zhaohuanhuan
	 * @date 2016年12月7日 
	 * @param point
	 * @return void
	 */
	@AfterReturning(pointcut="execution(* data.academic.transfer.service.TeacherTransferService.insertRefTeacherSchool(..))" )
	public void deleteTeacherTransferInsert(JoinPoint point) {
		    //获取传入触发抓取事件的service的参数
			Object[] args = point.getArgs();
			Map<String, Object> map = new ConcurrentHashMap<String, Object>();
			Map<String, Object> requestMap=new ConcurrentHashMap<String, Object>();
			if(args.length!=0)
			{
				map =(Map<String, Object>) args[0];
				 List<String> pkIdList=new ArrayList<>();
				 pkIdList.add(map.get("teacherPk").toString());
				 requestMap.put("pkIdList", pkIdList);
				 studentTransferService.removeTransferPson(requestMap);
			}
	}
	
	/**
	 * @Title: deleteTeacherTransferUpdate
	 * @Description: 抓取区级管理员同意老师申请时的数据
	 * @author zhaohuanhuan
	 * @date 2016年12月7日 
	 * @param point
	 * @return void
	 */
	@AfterReturning(pointcut="execution(* data.academic.transfer.service.TeacherTransferService.updateTeacherState(..))" )
	public void deleteTeacherTransferUpdate(JoinPoint point) {
		    //获取传入触发抓取事件的service的参数
			Object[] args = point.getArgs();
			Map<String, Object> map = new ConcurrentHashMap<String, Object>();
			Map<String, Object> requestMap=new ConcurrentHashMap<String, Object>();
			if(args.length!=0)
			{
				map =(Map<String, Object>) args[0];
				 List<String> pkIdList=new ArrayList<>();
				 pkIdList.add(map.get("teacherPk").toString());
				 requestMap.put("pkIdList", pkIdList);
				 studentTransferService.removeTransferPson(requestMap);
			}
	}
	
	
	/**
	 * @Title: cancelStuSubmitApply
	 * @Description: TODO
	 * @author zhaohuanhuan
	 * @date 2016年12月7日 
	 * @param point
	 * @return void
	 */
	@AfterReturning(pointcut="execution(* data.academic.transfer.service.StudentTransferService.cancelSubmitApply(..))" )
	public void cancelStuSubmitApply(JoinPoint point) {
		    //获取传入触发抓取事件的service的参数
			Object[] args = point.getArgs();
			Map<String, Object> maps = new HashMap<String, Object>();
			Map<String, Object> requestMap=new ConcurrentHashMap<String, Object>();
			if(args.length!=0)
			{
				maps =(Map<String, Object>)args[0];
				 List<String> pkIdList=new ArrayList<>();
				String selArr=maps.get("selArr").toString();
				selArr = selArr.substring(1, selArr.length() - 1);
				String[] selStrArray = selArr.split(", ");
				for(int i=0;i<selStrArray.length;i++){
					pkIdList.add(selStrArray[i]);
				}
			  requestMap.put("pkIdList", pkIdList);
			  studentTransferService.removeTransferPson(requestMap);
			}
	}
	
	
	/**
	 * @Title: cancelTeacherSubmitApply
	 * @Description: TODO
	 * @author zhaohuanhuan
	 * @date 2016年12月7日 
	 * @param point
	 * @return void
	 */
	@AfterReturning(pointcut="execution(* data.academic.teacherManagement.service.TeacherManagementService.cancelSubmitApply(..))" )
	public void cancelTeacherSubmitApply(JoinPoint point) {
		    //获取传入触发抓取事件的service的参数
			Object[] args = point.getArgs();
			Map<String, Object> maps = new HashMap<String, Object>();
			Map<String, Object> requestMap=new ConcurrentHashMap<String, Object>();
			if(args.length!=0)
			{
				maps =(Map<String, Object>)args[0];
				 List<String> pkIdList=new ArrayList<>();
				String selArr=maps.get("selArr").toString();
				selArr = selArr.substring(1, selArr.length() - 1);
				String[] selStrArray = selArr.split(", ");
				for(int i=0;i<selStrArray.length;i++){
					pkIdList.add(selStrArray[i]);
				}
			  requestMap.put("pkIdList", pkIdList);
			  studentTransferService.removeTransferPson(requestMap);
			}
	}
	
	
	@Autowired
	private StudentTransferService studentTransferService;
}