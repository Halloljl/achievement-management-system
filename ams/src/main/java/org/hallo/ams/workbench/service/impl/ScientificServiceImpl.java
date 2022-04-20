package org.hallo.ams.workbench.service.impl;

import org.hallo.ams.workbench.domain.Scientific;
import org.hallo.ams.workbench.mapper.ScientificMapper;
import org.hallo.ams.workbench.service.ScientificService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hallo
 * @datetime 2022-04-18 0:33
 * @description
 */
@Service("scientificServiceImpl")
public class ScientificServiceImpl implements ScientificService {

    @Autowired
    private ScientificMapper scientificMapper;

    @Override
    public int saveCreateScientific(Scientific scientific) {
        return scientificMapper.insertScientific(scientific);
    }

    @Override
    public List<Scientific> queryScientificByConditionForPage(Map<String, Object> map) {
        return scientificMapper.selectScientificByConditionForPage(map);
    }

    @Override
    public int queryCountOfScientificByCondition(Map<String, Object> map) {
        return scientificMapper.selectCountOfScientificByCondition(map);
    }

    @Override
    public int deleteScientificByIds(String[] ids) {
        return scientificMapper.deleteScientificByIds(ids);
    }

    @Override
    public Scientific queryScientificById(String id) {
        return scientificMapper.selectScientificById(id);
    }

    @Override
    public int saveEditScientific(Scientific scientific) {
        return scientificMapper.updateScientific(scientific);
    }

    @Override
    public List<Scientific> queryAllScientifics() {
        return scientificMapper.selectAllScientifics();
    }

    @Override
    public List<Scientific> queryScientificsByIds(String[] ids) {
        return scientificMapper.selectScientificsByIds(ids);
    }

    @Override
    public int saveCreateScientificsByList(List<Scientific> scientificList) {
        return scientificMapper.insertScientificsByList(scientificList);
    }

    @Override
    public Scientific queryScientificForDetailById(String id) {
        return scientificMapper.selectScientificForDetailById(id);
    }
}
