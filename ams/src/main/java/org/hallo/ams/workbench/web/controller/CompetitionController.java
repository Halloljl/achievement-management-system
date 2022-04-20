package org.hallo.ams.workbench.web.controller;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hallo.ams.commons.Constants.Contants;
import org.hallo.ams.commons.domain.Msg;
import org.hallo.ams.commons.utils.DateUtils;
import org.hallo.ams.commons.utils.HSSFUtils;
import org.hallo.ams.commons.utils.UUIDUtils;
import org.hallo.ams.settings.domain.User;
import org.hallo.ams.settings.service.UserService;
import org.hallo.ams.workbench.domain.Competition;
import org.hallo.ams.workbench.service.CompetitionService;
import org.hallo.ams.workbench.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Controller
public class CompetitionController {
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private UserService userService;
    @Autowired
    private InstructorService instructorService;

    /**
     * 跳转首页
     * @return
     */
    @RequestMapping("/workbench/competition/index.do")
    public String index(){
        //跳转到competition index
        return "workbench/competition/index";
    }
    /**
     * 省级及以上竞赛的添加功能
     * @param competition
     * @return
     */
    @RequestMapping("/workbench/competition/createCompetition.do")
    @ResponseBody
    public Object createCompetition(Competition competition , HttpServletRequest request){
        //UUID
        competition.setId(UUIDUtils.getUUID());
        competition.setCreateTime(DateUtils.formateDateTime(new Date()));
        //
        competition.setCreateBy(((User) request.getSession().getAttribute(Contants.SESSION_USER)).getId());

        Msg msg = new Msg();



        try {
            //调用service层方法
            int i = competitionService.saveCreateCompetition(competition);

            if(i>0){
                msg.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                msg.setMessage("系统繁忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            msg.setMessage("系统繁忙，请稍后重试...");
        }
        return msg;
    }

    @RequestMapping("/workbench/competition/retStuName.do")
    @ResponseBody
    public Object retStuName(String id){

        String stuName = userService.queryStuNameById(id);

        Msg msg = new Msg();
        if(stuName!=null){
            msg.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            msg.setRetData(stuName);
        }else {
            msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
        }
        return msg;
    }

    /**
     * 保存页面中 根据简称来获取客户全名
     * @return
     */
    /*@RequestMapping("/workbench/competition/queryteaNameByName.do")
    public @ResponseBody Object queryteaNameByName(String teaName){

        List<String> instructorList = instructorService.queryteaNameByName(teaName);

        return instructorList;
    }*/

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @ResponseBody
    @RequestMapping("/workbench/competition/queryCompetitionByConditionForPage.do")
    public Object queryCompetitionByConditionForPage(String projectName, String ownerId,
                                                    String ownerName,int pageNo, int pageSize) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("projectName", projectName);
        map.put("stuId", ownerId);
        map.put("stuName", ownerName);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);

        //查询
        List<Competition> competitionList = competitionService.queryCompetitionByConditionForPage(map);
        int totalRows = competitionService.queryCountOfCompetitionByCondition(map);

        //响应信息 java对象转json    -》 map 实体类 基本类型 list 数组
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("competitionList", competitionList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }

    /**
     * 根据id批量删除
     * @param id
     * @return
     */
    @RequestMapping("/workbench/competition/deleteCompetitionIds.do")
    @ResponseBody
    public Object deleteActivityIds(String[] id) {

        Msg msg = new Msg();

        try {
            int ret = competitionService.deleteCompetitionByIds(id);
            if (ret == id.length) {
                msg.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                msg.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            msg.setMessage("系统忙，请稍后重试...");
        }
        return msg;
    }


    /**
     * 根据id查询
     *
     * @param id 省级及省级以上id
     * @return 省级及省级以上
     */
    @RequestMapping("/workbench/competition/queryCompetitionById.do")
    @ResponseBody
    public Object queryCompetitionById(String id) {
        return competitionService.queryCompetitionById(id);
    }

    /**
     * 处理保存更新省级及省级以上
     *
     * @return 省级及省级以上修改状态
     */
    @RequestMapping("/workbench/competition/updateEditCompetition.do")
    @ResponseBody
    public Object updateEditCompetition(Competition competition, HttpServletRequest request) {
        //被谁修改的，从session中取出user的id
        competition.setEditBy(((User) request.getSession().getAttribute(Contants.SESSION_USER)).getId());
        competition.setEditTime(DateUtils.formateDateTime(new Date()));

        Msg msg = new Msg();
        try {
            //update
            int ret = competitionService.saveEditCompetition(competition);
            if (ret > 0) {
                msg.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                msg.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            msg.setMessage("系统忙，请稍后重试...");
        }
        return msg;
    }




    /**
     * 导出文件
     * 由于返回的是文件，同步请求的页面不会刷新，但是会弹出下载的窗口
     * @param response 响应对象 ，void用字节流回传，并且设置响应头。 mvc传文件复杂，
     */
    @RequestMapping("/workbench/competition/exportAllCompetitions.do")
    public void exportAllCompetitions(HttpServletResponse response) throws IOException {
        //查询所有竞赛项目
        List<Competition> competitionList = competitionService.queryAllCompetitions();
        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("省级及以上竞赛计划表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        {
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("项目名称");
            cell = row.createCell(2);
            cell.setCellValue("竞赛名称");
            cell = row.createCell(3);
            cell.setCellValue("学生学号");
            cell = row.createCell(4);
            cell.setCellValue("学生姓名");
            cell = row.createCell(5);
            cell.setCellValue("排名");
            cell = row.createCell(6);
            cell.setCellValue("获奖时间");
            cell = row.createCell(7);
            cell.setCellValue("获奖类别");
            cell = row.createCell(8);
            cell.setCellValue("获奖等级");
            cell = row.createCell(9);
            cell.setCellValue("创建时间");
            cell = row.createCell(10);
            cell.setCellValue("创建者");
            cell = row.createCell(11);
            cell.setCellValue("修改时间");
            cell = row.createCell(12);
            cell.setCellValue("修改者");
        }

        //遍历list，创建HSSFRow，生成数据行
        Competition competition = null;
        if (competitionList != null && competitionList.size() > 0) {
            for (int i = 0; i < competitionList.size(); i++) {
                competition = competitionList.get(i);

                //每遍历出一个competition，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从competition中获取
                cell = row.createCell(0);
                cell.setCellValue(competition.getId());
                cell = row.createCell(1);
                cell.setCellValue(competition.getProjectName());
                cell = row.createCell(2);
                cell.setCellValue(competition.getStuId());
                cell = row.createCell(3);
                cell.setCellValue(competition.getUser().getName());
                cell = row.createCell(4);
                cell.setCellValue(competition.getRanking());
                cell = row.createCell(5);
                cell.setCellValue(competition.getAwardTime());
                cell = row.createCell(6);
                cell.setCellValue(competition.getAwardCategory());
                cell = row.createCell(7);
                cell.setCellValue(competition.getAwardLevel());
                cell = row.createCell(8);
                cell.setCellValue(competition.getCreateTime());
                cell = row.createCell(9);
                cell.setCellValue(competition.getCreateBy());
                cell = row.createCell(10);
                cell.setCellValue(competition.getEditTime());
                cell = row.createCell(11);
                cell.setCellValue(competition.getEditBy());
            }
        }

        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=competitionList.xls");
        OutputStream out=response.getOutputStream();

        workbook.write(out);

        workbook.close();
        out.flush();
    }

    /**
     * 选择导出文件
     * 由于返回的是文件，同步请求的页面不会刷新，但是会弹出下载的窗口
     * @param response 响应对象 ，void用字节流回传，并且设置响应头。 mvc传文件复杂，
     */
    @RequestMapping("/workbench/competition/exportCompetitionsByIds.do")
    public void exportCompetitionsByIds(String[] id,HttpServletResponse response) throws IOException {

        // 调用service层方法，根据ids查询被选中的创新创业计划的信息
        List<Competition> competitionList = competitionService.queryCompetitionsByIds(id);



        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("省级及以上竞赛计划表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        {
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("项目名称");
            cell = row.createCell(2);
            cell.setCellValue("竞赛名称");
            cell = row.createCell(3);
            cell.setCellValue("学生学号");
            cell = row.createCell(4);
            cell.setCellValue("学生姓名");
            cell = row.createCell(5);
            cell.setCellValue("排名");
            cell = row.createCell(6);
            cell.setCellValue("获奖时间");
            cell = row.createCell(7);
            cell.setCellValue("获奖类别");
            cell = row.createCell(8);
            cell.setCellValue("获奖等级");
            cell = row.createCell(9);
            cell.setCellValue("创建时间");
            cell = row.createCell(10);
            cell.setCellValue("创建者");
            cell = row.createCell(11);
            cell.setCellValue("修改时间");
            cell = row.createCell(12);
            cell.setCellValue("修改者");
        }

        //遍历list，创建HSSFRow，生成数据行
        Competition competition = null;
        if (competitionList != null && competitionList.size() > 0) {
            for (int i = 0; i < competitionList.size(); i++) {
                competition = competitionList.get(i);

                //每遍历出一个competition，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从competition中获取
                cell = row.createCell(0);
                cell.setCellValue(competition.getId());
                cell = row.createCell(1);
                cell.setCellValue(competition.getProjectName());
                cell = row.createCell(2);
                cell.setCellValue(competition.getStuId());
                cell = row.createCell(3);
                cell.setCellValue(competition.getUser().getName());
                cell = row.createCell(4);
                cell.setCellValue(competition.getRanking());
                cell = row.createCell(5);
                cell.setCellValue(competition.getAwardTime());
                cell = row.createCell(6);
                cell.setCellValue(competition.getAwardCategory());
                cell = row.createCell(7);
                cell.setCellValue(competition.getAwardLevel());
                cell = row.createCell(8);
                cell.setCellValue(competition.getCreateTime());
                cell = row.createCell(9);
                cell.setCellValue(competition.getCreateBy());
                cell = row.createCell(10);
                cell.setCellValue(competition.getEditTime());
                cell = row.createCell(11);
                cell.setCellValue(competition.getEditBy());
            }
        }




        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=competitionXzList.xls");
        OutputStream out=response.getOutputStream();

        workbook.write(out);

        workbook.close();
        out.flush();
    }



    /**
     * 导入文件,文件上传需要配置文件上传解析器
     * @param competitionFile 响应对象
     */
    @RequestMapping("/workbench/competition/importCompetitionByList.do")
    @ResponseBody
    public Msg importCompetitionByList(MultipartFile competitionFile, HttpServletRequest request , HttpSession session) {
        Msg msg = new Msg();

        User user = (User) session.getAttribute(Contants.SESSION_USER);

        try {
            // 把excel文件写如磁盘目录中
           /* String originalFilename = activityFile.getOriginalFilename();
            File file = new File("D:\\满级人类\\mynotes\\CRM客户管理系统\\serverDir",originalFilename);
            activityFile.transferTo(file);*/

            // 解析excel文件，获取文件中的数据，并且封装成activityList
            // FileInputStream is = new FileInputStream("D:\\满级人类\\mynotes\\CRM客户管理系统\\serverDir" + originalFilename);

            InputStream is = competitionFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(is);

            // 获取sheet
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFRow row = null;
            Competition competition = null;
            HSSFCell cell =null;

            List<Competition> competitionList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                competition = new Competition();
                competition.setId(UUIDUtils.getUUID());
                competition.setCreateTime(DateUtils.formateDateTime(new Date()));
                competition.setCreateBy(user.getId());

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if (j==0){
                        competition.setProjectName(cellValue);
                    }else if (j==1){
                        competition.setCompetitionName(cellValue);
                    }else if (j==2){
                        competition.setStuId(cellValue);
                    }else if (j==3){
                        competition.setRanking(cellValue);
                    }else if (j==4){
                        competition.setAwardTime(cellValue);
                    }else if (j==5){
                        competition.setAwardCategory(cellValue);
                    }else if (j==6){
                        competition.setAwardLevel(cellValue);
                    }

                }
                competitionList.add(competition);
            }

            int ret = competitionService.saveCreateCompetitionsByList(competitionList);


            msg.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            msg.setRetData(ret);

        } catch (IOException e) {
            e.printStackTrace();
            msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            msg.setMessage("系统繁忙，请稍后重试...");
        }
        return msg;

    }






    @RequestMapping("/workbench/competition/detailCompetition.do")
    public String detailCompetition(String id,HttpServletRequest request){
        Competition competition = competitionService.queryCompetitionForDetailById(id);
        request.setAttribute("competition",competition);
        return "workbench/competition/detail";
    }
}
