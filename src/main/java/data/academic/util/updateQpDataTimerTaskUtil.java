package data.academic.util;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/** 
* @ClassName: updateQpData
* @Description: 定时访更新青浦接口数据
* @author zhaohuanhuan
* @date 2017年9月4日 
*/
public class updateQpDataTimerTaskUtil/* extends QuartzJobBean*/{

	/*@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		HttpClientUtil httpClientUtil=new HttpClientUtil();
		httpClientUtil.getUpdateData();
	}*/
	
	public void updateDateTask(){
		HttpClientUtil httpClientUtil=new HttpClientUtil();
		//暂时不更新数据
		//httpClientUtil.getUpdateData();
	}
}
