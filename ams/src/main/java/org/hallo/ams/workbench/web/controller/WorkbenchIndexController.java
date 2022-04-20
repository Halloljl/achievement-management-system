package org.hallo.ams.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author hallo
 * @create 2022-04-03 18:43
 */
@Controller
public class WorkbenchIndexController {
    /**
     * 跳转到业务首页
     * @return
     */
    @GetMapping("/workbench/index.do")
    public String index(){
        return "workbench/index";
    }

    @GetMapping("workbench/studentIndex.do")
    public String studentIndex(){
        return "workbench/studentIndex";
    }
}
