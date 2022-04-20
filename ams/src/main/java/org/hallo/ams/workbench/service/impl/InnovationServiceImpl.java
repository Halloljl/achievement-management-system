package org.hallo.ams.workbench.service.impl;

import org.hallo.ams.workbench.domain.Innovation;
import org.hallo.ams.workbench.mapper.InnovationMapper;
import org.hallo.ams.workbench.service.InnovationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hallo
 * @create 2022-04-03 22:33
 */
@Service("innovationService")
public class InnovationServiceImpl implements InnovationService {

    @Autowired
    private InnovationMapper innovationMapper;

    @Override
    public int saveCreateInnovation(Innovation innovation) {
        return innovationMapper.insertInnovation(innovation);
    }

    @Override
    public List<Innovation> queryInnovationByConditionForPage(Map<String, Object> map) {
        return innovationMapper.selectInnovationByConditionForPage(map);
    }

    @Override
    public int queryCountOfInnovationByCondition(Map<String, Object> map) {
        return innovationMapper.selectCountOfInnovationByCondition(map);
    }

    @Override
    public int deleteInnovationByIds(String[] ids) {
        return innovationMapper.deleteInnovationByIds(ids);
    }

    @Override
    public Innovation queryInnovationById(String id) {
        return innovationMapper.selectInnovationById(id);
    }

    @Override
    public int saveEditInnovation(Innovation innovation) {
        return innovationMapper.updateInnovation(innovation);
    }

    @Override
    public List<Innovation> queryAllInnovations() {
        return innovationMapper.selectAllInnovations();
    }

    @Override
    public List<Innovation> queryInnovationsByIds(String[] ids) {
        return innovationMapper.selectInnovationsByIds(ids);
    }

    @Override
    public int saveCreateInnovationsByList(List<Innovation> innovationList) {
        return innovationMapper.insertInnovationsByList(innovationList);
    }

    @Override
    public Innovation queryInnovationForDetailById(String id) {
        return innovationMapper.selectInnovationForDetailById(id);
    }
}
