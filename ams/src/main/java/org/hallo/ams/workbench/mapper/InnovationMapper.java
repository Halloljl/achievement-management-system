package org.hallo.ams.workbench.mapper;

import org.hallo.ams.workbench.domain.Innovation;

import java.util.List;
import java.util.Map;

public interface InnovationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_innovation
     *
     * @mbg.generated Mon Apr 04 17:47:24 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_innovation
     *
     * @mbg.generated Mon Apr 04 17:47:24 CST 2022
     */
    int insert(Innovation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_innovation
     *
     * @mbg.generated Mon Apr 04 17:47:24 CST 2022
     */
    int insertSelective(Innovation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_innovation
     *
     * @mbg.generated Mon Apr 04 17:47:24 CST 2022
     */
    Innovation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_innovation
     *
     * @mbg.generated Mon Apr 04 17:47:24 CST 2022
     */
    int updateByPrimaryKeySelective(Innovation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_innovation
     *
     * @mbg.generated Mon Apr 04 17:47:24 CST 2022
     */
    int updateByPrimaryKey(Innovation record);

    /**
     *  保存创建的创新创业计划
     * @param Innovation
     * @return
     */
    int insertInnovation(Innovation Innovation);

    /**
     * 查看分页创新创业计划
     * @param map 查询条件
     * @return 分页的创新创业计划列表
     */
    List<Innovation> selectInnovationByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询记录的数量
     * @param map
     * @return
     */
    int selectCountOfInnovationByCondition(Map<String,Object> map);

    /**
     * 根据ids来批量删除创新创业计划
     * @param ids
     * @return
     */
    int deleteInnovationByIds(String[] ids);

    /**
     * 根据id查询创新创业计划的信息
     * @param id
     * @return
     */
    Innovation selectInnovationById(String id);

    /**
     * 更新创新创业计划
     * @param innovation
     * @return
     */
    int updateInnovation(Innovation innovation);


    /**
     * 查询所有的创新创业计划
     * @return
     */
    List<Innovation> selectAllInnovations();

    /**
     * 根据ids查找创新创业计划列表
     * @param ids
     * @return
     */
    List<Innovation> selectInnovationsByIds(String[] ids);

    /**
     *  批量保存创新创业计划
     * @return
     */
    int insertInnovationsByList(List<Innovation> innovationList);

    /**
     * 根据创新创业计划id查询创新创业计划的详细详细
     * @param id
     * @return
     */
    Innovation selectInnovationForDetailById(String id);
}