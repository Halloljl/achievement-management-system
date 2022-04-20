package org.hallo.ams.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hallo
 * @datetime 2022-04-19 13:58
 * @description
 */
@Controller
public class ChartController {

    /**
     * 跳转到innovation index
     * @return
     */
    @RequestMapping("/workbench/chart/innovation/innovationIndex.do")
    public String innovationIndex(){

        return "workbench/chart/innovation/index";
    }


    /**
     * AcademicIndex
     * @return
     */
    @RequestMapping("/workbench/chart/academic/academicIndex.do")
    public String academicIndex(){

        return "workbench/chart/academic/index";
    }


    @RequestMapping("/workbench/chart/competition/competitionIndex.do")
    public String competitionIndex(){

        return "workbench/chart/competition/index";
    }


    @RequestMapping("/workbench/chart/patent/patentIndex.do")
    public String patentIndex(){

        return "workbench/chart/patent/index";
    }



    @RequestMapping("/workbench/chart/scientific/scientificIndex.do")
    public String scientificIndex(){

        return "workbench/chart/scientific/index";
    }


}
