package org.hallo.ams.workbench.service;

import org.hallo.ams.workbench.domain.Innovation;

import java.util.List;
import java.util.Map;

/**
 * @author hallo
 * @create 2022-04-03 22:32
 */
public interface InnovationService {

    int saveCreateInnovation(Innovation innovation);

    /**
     * 根据条件查询创新创业计划分页
     * @param map
     * @return
     */
    List<Innovation> queryInnovationByConditionForPage(Map<String,Object> map);


    /**
     * 根据条件查询创新创业计划总条数
     * @param map
     * @return
     */
    int queryCountOfInnovationByCondition(Map<String,Object> map);


    /**
     * 根据id批量删除创新创业计划
     * @param ids
     * @return
     */
    int deleteInnovationByIds(String [] ids);

    /**
     * 根据id查询创新创业计划
     * @param id
     * @return
     */
    Innovation queryInnovationById(String id);


    /**
     * 更新创新创业计划
     * @param innovation
     * @return
     */
    int saveEditInnovation(Innovation innovation);

    /**
     * 查询所有的创新创业计划
     * @return
     */
    List<Innovation> queryAllInnovations();

    /**
     * 根据ids查询创新创业计划
     * @return
     */
    List<Innovation> queryInnovationsByIds(String[] ids);

    /**
     * 批量添加创新创业计划
     * @param innovationList
     * @return
     */
    int saveCreateInnovationsByList(List<Innovation> innovationList);

    /**
     * 根据创新创业计划id查询创新创业计划的详细详细
     * @param id
     * @return
     */
    Innovation queryInnovationForDetailById(String id);

}
