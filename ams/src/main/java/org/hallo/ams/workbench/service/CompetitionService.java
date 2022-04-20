package org.hallo.ams.workbench.service;

import org.hallo.ams.workbench.domain.Competition;

import java.util.List;
import java.util.Map;

public interface CompetitionService {

    int saveCreateCompetition(Competition competition);
    /**
     * 根据条件查询省级及以上竞赛分页
     * @param map
     * @return
     */
    List<Competition> queryCompetitionByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询省级及省级以上竞赛总条数
     * @param map
     * @return
     */
    int queryCountOfCompetitionByCondition(Map<String,Object> map);

    /**
     * 根据id批量删除省级及以上竞赛
     * @param ids
     * @return
     */
    int deleteCompetitionByIds(String [] ids);

    /**
     * 根据id查询省级及以上
     * @param id
     * @return
     */
    Competition queryCompetitionById(String id);


    /**
     * 更新省级及以上竞赛
     * @param competition
     * @return
     */
    int saveEditCompetition(Competition competition);

    /**
     * 查询所有的省级及以上竞赛
     * @return
     */
    List<Competition> queryAllCompetitions();

    /**
     * 根据ids查询省级及以上竞赛
     * @return
     */
    List<Competition> queryCompetitionsByIds(String[] ids);

    /**
     * 批量添加省级及以上竞赛
     * @param competitionList
     * @return
     */
    int saveCreateCompetitionsByList(List<Competition> competitionList);

    /**
     * 根据省级及以上d查询省级及以上的详细详细
     * @param id
     * @return
     */
    Competition queryCompetitionForDetailById(String id);

}
