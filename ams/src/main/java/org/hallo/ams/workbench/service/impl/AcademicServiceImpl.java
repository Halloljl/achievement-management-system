package org.hallo.ams.workbench.service.impl;

import org.hallo.ams.workbench.domain.Academic;
import org.hallo.ams.workbench.mapper.AcademicMapper;
import org.hallo.ams.workbench.service.AcademicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hallo
 * @datetime 2022-04-19 0:17
 * @description
 */
@Service("academicService")
public class AcademicServiceImpl implements AcademicService {

    @Autowired
    private AcademicMapper academicMapper;

    @Override
    public int saveCreateAcademic(Academic academic) {
        return academicMapper.insertAcademic(academic);
    }

    @Override
    public List<Academic> queryAcademicByConditionForPage(Map<String, Object> map) {
        return academicMapper.selectAcademicByConditionForPage(map);
    }

    @Override
    public int queryCountOfAcademicByCondition(Map<String, Object> map) {
        return academicMapper.selectCountOfAcademicByCondition(map);
    }

    @Override
    public int deleteAcademicByIds(String[] ids) {
        return academicMapper.deleteAcademicByIds(ids);
    }

    @Override
    public Academic queryAcademicById(String id) {
        return academicMapper.selectAcademicById(id);
    }

    @Override
    public int saveEditAcademic(Academic academic) {
        return academicMapper.updateAcademic(academic);
    }

    @Override
    public List<Academic> queryAllAcademics() {
        return academicMapper.selectAllAcademics();
    }

    @Override
    public List<Academic> queryAcademicsByIds(String[] ids) {
        return academicMapper.selectAcademicsByIds(ids);
    }

    @Override
    public int saveCreateAcademicsByList(List<Academic> academicList) {
        return academicMapper.insertAcademicsByList(academicList);
    }

    @Override
    public Academic queryAcademicForDetailById(String id) {
        return academicMapper.selectAcademicForDetailById(id);
    }
}
