package data.platform.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import data.framework.support.AbstractService;
import data.framework.utility.EncryptHelper;
import data.platform.entity.EntityPlatformUserBua;

/**
 * 平台－用户服务类。
 * 
 * @author wanggq
 */
@Service
@Path(value = "/platformUserService")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlatformUserBuaService extends AbstractService {

	/**
	 * 保存或更新用户信息。
	 * 
	 * @param entity
	 *            用户实体
	 */
	public void saveOrUpdate(EntityPlatformUserBua entity) {
		if (StringUtils.isNotBlank(entity.getId())) {
			update("platformUserBua.updateUser", entity);
		}
	}

	/**
	 * 根据用户编号获取用户信息。
	 * 
	 * @param id
	 *            用户编号
	 * @return 用户实体
	 */
	public EntityPlatformUserBua load(String id) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("id", id);
		return selectOne("platformUserBua.loadUser", param);
	}

}