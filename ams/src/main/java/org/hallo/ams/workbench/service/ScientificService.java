package org.hallo.ams.workbench.service;

import org.hallo.ams.workbench.domain.Scientific;

import java.util.List;
import java.util.Map;

/**
 * @author hallo
 * @datetime 2022-04-18 0:31
 * @description
 */
public interface ScientificService {

    int saveCreateScientific(Scientific scientific);


    /**
     * 根据条件查询科研项目分页
     * @param map
     * @return
     */
    List<Scientific> queryScientificByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询科研项目总条数
     * @param map
     * @return
     */
    int queryCountOfScientificByCondition(Map<String,Object> map);

    /**
     * 根据id批量删除科研项目
     * @param ids
     * @return
     */
    int deleteScientificByIds(String [] ids);

    /**
     * 根据id查询科研项目
     * @param id
     * @return
     */
    Scientific queryScientificById(String id);


    /**
     * 更新科研项目
     * @param scientific
     * @return
     */
    int saveEditScientific(Scientific scientific);


    /**
     * 查询所有的科研项目
     * @return
     */
    List<Scientific> queryAllScientifics();

    /**
     * 根据ids查询科研项目
     * @return
     */
    List<Scientific> queryScientificsByIds(String[] ids);

    /**
     * 批量添加科研项目
     * @param scientificList
     * @return
     */
    int saveCreateScientificsByList(List<Scientific> scientificList);

    /**
     * 根据科研项目id查询科研项目的详细详细
     * @param id
     * @return
     */
    Scientific queryScientificForDetailById(String id);


    
}
