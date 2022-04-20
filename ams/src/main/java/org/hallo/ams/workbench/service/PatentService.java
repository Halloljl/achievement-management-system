package org.hallo.ams.workbench.service;

import org.hallo.ams.workbench.domain.Patent;

import java.util.List;
import java.util.Map;

/**
 * @author hallo
 * @datetime 2022-04-18 21:35
 * @description
 */
public interface PatentService {


    int saveCreatePatent(Patent patent);

    /**
     * 根据条件查询专利分页
     * @param map
     * @return
     */
    List<Patent> queryPatentByConditionForPage(Map<String,Object> map);


    /**
     * 根据条件查询专利总条数
     * @param map
     * @return
     */
    int queryCountOfPatentByCondition(Map<String,Object> map);


    /**
     * 根据id批量删除专利
     * @param ids
     * @return
     */
    int deletePatentByIds(String [] ids);

    /**
     * 根据id查询专利
     * @param id
     * @return
     */
    Patent queryPatentById(String id);


    /**
     * 更新专利
     * @param patent
     * @return
     */
    int saveEditPatent(Patent patent);

    /**
     * 查询所有的专利
     * @return
     */
    List<Patent> queryAllPatents();

    /**
     * 根据ids查询专利
     * @return
     */
    List<Patent> queryPatentsByIds(String[] ids);

    /**
     * 批量添加专利
     * @param patentList
     * @return
     */
    int saveCreatePatentsByList(List<Patent> patentList);

    /**
     * 根据专利id查询专利的详细详细
     * @param id
     * @return
     */
    Patent queryPatentForDetailById(String id);


}
