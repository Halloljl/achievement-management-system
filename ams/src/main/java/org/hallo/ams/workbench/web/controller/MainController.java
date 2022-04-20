package org.hallo.ams.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hallo
 * @create 2022-04-03 21:07
 */
@Controller
public class MainController {

    @RequestMapping("/workbench/main/index.do")
    public String index(){
        //跳转到main index
        return "workbench/main/index";
    }
}
