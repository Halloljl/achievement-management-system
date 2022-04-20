package org.hallo.ams.workbench.service.impl;

import org.hallo.ams.workbench.mapper.InstructorMapper;
import org.hallo.ams.workbench.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hallo
 * @create 2022-04-04 17:32
 */
@Service("instructorService")
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    private InstructorMapper instructorMapper;

    @Override
    public List<String> queryteaNameByName(String name) {
        return instructorMapper.selectInstructorNameByName(name);
    }
}
