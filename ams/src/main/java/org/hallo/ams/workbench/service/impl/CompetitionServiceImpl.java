package org.hallo.ams.workbench.service.impl;

import org.hallo.ams.workbench.domain.Competition;
import org.hallo.ams.workbench.mapper.CompetitionMapper;
import org.hallo.ams.workbench.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("competitionService")
public class CompetitionServiceImpl implements CompetitionService {

    @Autowired
    private CompetitionMapper competitionMapper;

    @Override
    public int saveCreateCompetition(Competition competition) {
        return competitionMapper.insertCompetition(competition);
    }

    @Override
    public List<Competition> queryCompetitionByConditionForPage(Map<String, Object> map) {
        return competitionMapper.selectCompetitionByConditionForPage(map);
    }

    @Override
    public int queryCountOfCompetitionByCondition(Map<String, Object> map) {
        return competitionMapper.selectCountOfCompetitionByCondition(map);
    }

    @Override
    public int deleteCompetitionByIds(String[] ids) {
        return competitionMapper.deleteCompetitionByIds(ids);
    }

    @Override
    public Competition queryCompetitionById(String id) {
        return competitionMapper.selectCompetitionById(id);
    }


    @Override
    public int saveEditCompetition(Competition competition) {
        return competitionMapper.updateCompetition(competition);
    }

    @Override
    public List<Competition> queryAllCompetitions() {
        return competitionMapper.selectAllCompetitions();
    }

    @Override
    public List<Competition> queryCompetitionsByIds(String[] ids) {
        return competitionMapper.selectCompetitionsByIds(ids);
    }

    @Override
    public int saveCreateCompetitionsByList(List<Competition> competitionList) {
        return competitionMapper.insertCompetitionsByList(competitionList);
    }

    @Override
    public Competition queryCompetitionForDetailById(String id) {
        return competitionMapper.selectCompetitionForDetailById(id);
    }
}
