package data.academic.taskCenter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import data.framework.support.AbstractService;
import data.framework.utility.FormatConvertor;
import data.academic.taskCenter.entity.EntityTaskInfo;
import data.academic.taskCenter.entity.EntityTaskReceiverInfo;

/**
 * OA-任务中心-任务(接收人)处理任务情况服务类。
 * 
 * @author JohnXU
 * 
 */
@Service
public class TaskReceiverService extends AbstractService {
	@Autowired
	private TaskRecordService taskRecordService;

	/**
	 * 添加和修改
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(EntityTaskReceiverInfo entity) {
		if (StringUtils.isNotBlank(entity.getId()))
			update("taskDealProcess.updateTaskDealProcess", entity);
		else
			insert("taskDealProcess.insertTaskDealProcess", entity);
	}

	/**
	 * 根据任务信息Id 来删除任务信息。
	 * 
	 * @param id
	 *            任务信息Id集合
	 * @return 删除处理人的任务信息数量
	 */
	public int remove(List<String> idAry) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idAry", idAry);
		return delete("taskDealProcess.deleteTaskDealProcessByTaskId", param);

	}

	/**
	 * 根据第三方唯一标示集合来先删除任务接收人表中的数据，后删除任务信息表中的数据。
	 * 
	 * @param thirdInfoIdList
	 *            第三方唯一标示集合
	 * @return 删除信息数据的数量
	 */
	public void removeDatasByThirdInfoIdList(List<String> thirdInfoIdList, Integer status) throws Exception {
		// 删除任务接收人表中的数据
		List<EntityTaskInfo> taskInfoList = taskRecordService.loadTaskInfoListByMarkedFlagList(thirdInfoIdList, status);
		List<String> taskInfoIdList = new ArrayList<String>();
		for (EntityTaskInfo entity : taskInfoList) {
			taskInfoIdList.add(entity.getId());
		}
		if (taskInfoIdList != null && taskInfoIdList.size() > 0) {
			remove(taskInfoIdList);
		}
		// 除任务信息表中的数据
		taskRecordService.remove(thirdInfoIdList);
	}

	/**
	 * @param id
	 * @return
	 */
	public EntityTaskInfo loadEntityTaskInfo(String id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		return selectOne("taskDealProcess.load", param);
	}
	
	public List<Map<String, Object>> loadFdyByZyNjId(String zyNjId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("zyNjId", zyNjId);
		param.put("date", FormatConvertor.formatDate(new Date(), "yyyy-MM-dd"));
		return selectList("taskDealProcess.loadFdyByZyNjId", param);
	}
}
