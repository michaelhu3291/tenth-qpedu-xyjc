package data.platform.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import data.framework.pagination.model.PagingResult;
import data.framework.support.AbstractService;
/**
 * 学生管理服务类。
 * 4.21
 *
 */
@Service
public class PlatformStudentService extends  AbstractService {

	/**
     * 分页查询数据
     * @param operatorType 操作类型
     * @param module 模式
     * @param sortField 数据库排序字段
     * @param sort 排序方式（ASC|DESC）
     * @param currentPage 当前页数
     * @param pageSize 页大小
     * @return 分页查询集合
     * 
     * 4.21
     */
    public PagingResult<Map<String,Object>> serachStu(Map<String,Object> param, String sortField, String sort, int currentPage, int pageSize )
    {
        if( StringUtils.isBlank( sortField ) ){
            sortField = "stuName" ;}
        if( StringUtils.isBlank( sort ) )
            sort = "ASC" ;
        return selectPaging( "newStuManagement.selectPagingStudent", param, sortField, sort, currentPage, pageSize ) ;
    }

    /**
     * 查询学院
     * 4.23
     */
    public List<Map<String,Object>> selectCollege(Map<String,Object> param )
    {
        return selectList( "platformRole.selectRoleUser", param ) ;
    }
    
    
}
