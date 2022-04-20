package org.hallo.ams.workbench.service;

import org.hallo.ams.workbench.domain.Academic;

import java.util.List;
import java.util.Map;

/**
 * @author hallo
 * @datetime 2022-04-19 0:16
 * @description
 */
public interface AcademicService {

    int saveCreateAcademic(Academic academic);

    /**
     * 根据条件查询学术论文计划分页
     * @param map
     * @return
     */
    List<Academic> queryAcademicByConditionForPage(Map<String,Object> map);


    /**
     * 根据条件查询学术论文计划总条数
     * @param map
     * @return
     */
    int queryCountOfAcademicByCondition(Map<String,Object> map);


    /**
     * 根据id批量删除学术论文计划
     * @param ids
     * @return
     */
    int deleteAcademicByIds(String [] ids);

    /**
     * 根据id查询学术论文计划
     * @param id
     * @return
     */
    Academic queryAcademicById(String id);


    /**
     * 更新学术论文计划
     * @param Academic
     * @return
     */
    int saveEditAcademic(Academic academic);

    /**
     * 查询所有的学术论文计划
     * @return
     */
    List<Academic> queryAllAcademics();

    /**
     * 根据ids查询学术论文计划
     * @return
     */
    List<Academic> queryAcademicsByIds(String[] ids);

    /**
     * 批量添加学术论文计划
     * @param AcademicList
     * @return
     */
    int saveCreateAcademicsByList(List<Academic> academicList);

    /**
     * 根据学术论文计划id查询学术论文计划的详细详细
     * @param id
     * @return
     */
    Academic queryAcademicForDetailById(String id);

}
