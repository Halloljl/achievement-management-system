package org.hallo.ams.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hallo
 * @date 2022/3/31 - 20:42
 */

@Controller
public class IndexController {

    /**
     * 首页
     * @return
     */
    @RequestMapping("/")
    public String index(){
        return "index";

    }
}

