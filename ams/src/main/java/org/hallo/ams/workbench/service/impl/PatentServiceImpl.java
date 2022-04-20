package org.hallo.ams.workbench.service.impl;

import org.hallo.ams.workbench.domain.Patent;
import org.hallo.ams.workbench.mapper.PatentMapper;
import org.hallo.ams.workbench.service.PatentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author hallo
 * @datetime 2022-04-18 21:41
 * @description
 */
@Service("patentServiceImpl")
public class PatentServiceImpl implements PatentService {

    @Autowired
    private PatentMapper patentMapper;

    @Override
    public int saveCreatePatent(Patent patent) {
        return patentMapper.insertPatent(patent);
    }

    @Override
    public List<Patent> queryPatentByConditionForPage(Map<String, Object> map) {
        return patentMapper.selectPatentByConditionForPage(map);
    }

    @Override
    public int queryCountOfPatentByCondition(Map<String, Object> map) {
        return patentMapper.selectCountOfPatentByCondition(map);
    }

    @Override
    public int deletePatentByIds(String[] ids) {
        return patentMapper.deletePatentByIds(ids);
    }

    @Override
    public Patent queryPatentById(String id) {
        return patentMapper.selectPatentById(id);
    }

    @Override
    public int saveEditPatent(Patent patent) {
        return patentMapper.updatePatent(patent);
    }

    @Override
    public List<Patent> queryAllPatents() {
        return patentMapper.selectAllPatents();
    }

    @Override
    public List<Patent> queryPatentsByIds(String[] ids) {
        return patentMapper.selectPatentsByIds(ids);
    }

    @Override
    public int saveCreatePatentsByList(List<Patent> patentList) {
        return patentMapper.insertPatentsByList(patentList);
    }

    @Override
    public Patent queryPatentForDetailById(String id) {
        return patentMapper.selectPatentForDetailById(id);
    }
}
