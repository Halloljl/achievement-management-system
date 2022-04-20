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
import org.hallo.ams.workbench.domain.Innovation;
import org.hallo.ams.workbench.service.InnovationService;
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

/**
 * @author hallo
 * @create 2022-04-03 21:09
 */
@Controller
public class InnovationController {

    @Autowired
    private InnovationService innovationService;
    @Autowired
    private UserService userService;
    @Autowired
    private InstructorService instructorService;

    /**
     * 创新创业计划的首页
     * @param request
     * @return
     */
    @RequestMapping("/workbench/innovation/index.do")
    public String index(HttpServletRequest request){

        //跳转到innovation index
        return "workbench/innovation/index";
    }

    /**
     * 创新创业计划的添加功能
     * @param innovation
     * @return
     */
    @RequestMapping("/workbench/innovation/createInnovation.do")
    @ResponseBody
    public Object createInnovation(Innovation innovation ,HttpServletRequest request){
        //UUID
        innovation.setId(UUIDUtils.getUUID());
        innovation.setCreateTime(DateUtils.formateDateTime(new Date()));
        //
        innovation.setCreateBy(((User) request.getSession().getAttribute(Contants.SESSION_USER)).getId());

        Msg msg = new Msg();



        try {
            //调用service层方法，添加创新创业计划
            int i = innovationService.saveCreateInnovation(innovation);

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


    @RequestMapping("/workbench/innovation/retStuName.do")
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
     * 保存页面中 根据简称来获取教师全名
     * @return
     */
    @RequestMapping("/workbench/innovation/queryteaNameByName.do")
    public @ResponseBody Object queryteaNameByName(String teaName){

        List<String> instructorList = instructorService.queryteaNameByName(teaName);

        return instructorList;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @ResponseBody
    @RequestMapping("/workbench/innovation/queryInnovationByConditionForPage.do")
    public Object queryInnovationByConditionForPage(String projectName, String ownerId,
                                                  String ownerName, String schoolYear,
                                                  String instructor,int pageNo, int pageSize) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("projectName", projectName);
        map.put("stuId", ownerId);
        map.put("stuName", ownerName);
        map.put("schoolYear", schoolYear);
        map.put("instructor", instructor);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);

        //查询
        List<Innovation> innovationList = innovationService.queryInnovationByConditionForPage(map);
        int totalRows = innovationService.queryCountOfInnovationByCondition(map);

        //响应信息 java对象转json    -》 map 实体类 基本类型 list 数组
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("innovationList", innovationList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }


    /**
     * 根据id批量删除
     * @param id
     * @return
     */
    @RequestMapping("/workbench/innovation/deleteInnovationIds.do")
    @ResponseBody
    public Object deleteInnovationIds(String[] id) {

        Msg msg = new Msg();

        try {
            int ret = innovationService.deleteInnovationByIds(id);
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
     * @param id 创新创业计划id
     * @return 创新创业计划
     */
    @RequestMapping("/workbench/innovation/queryInnovationById.do")
    @ResponseBody
    public Object queryInnovationById(String id) {
        return innovationService.queryInnovationById(id);
    }

    /**
     * 处理保存更新创新创业计划
     *
     * @return 创新创业计划修改状态
     */
    @RequestMapping("/workbench/innovation/updateEditInnovation.do")
    @ResponseBody
    public Object updateEditInnovation(Innovation innovation, HttpServletRequest request) {
        //被谁修改的，从session中取出user的id
        innovation.setEditBy(((User) request.getSession().getAttribute(Contants.SESSION_USER)).getId());
        innovation.setEditTime(DateUtils.formateDateTime(new Date()));

        Msg msg = new Msg();
        try {
            //update
            int ret = innovationService.saveEditInnovation(innovation);
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
    @RequestMapping("/workbench/innovation/exportAllInnovations.do")
    public void exportAllInnovations(HttpServletResponse response) throws IOException {
        //查询所有创新创业计划
        List<Innovation> innovationList = innovationService.queryAllInnovations();
        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("创新创业计划表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        {
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("项目名称");
            cell = row.createCell(2);
            cell.setCellValue("项目级别");
            cell = row.createCell(3);
            cell.setCellValue("学生学号");
            cell = row.createCell(4);
            cell.setCellValue("学生姓名");
            cell = row.createCell(5);
            cell.setCellValue("学年");
            cell = row.createCell(6);
            cell.setCellValue("指导教师");
            cell = row.createCell(7);
            cell.setCellValue("创建时间");
            cell = row.createCell(8);
            cell.setCellValue("创建者");
            cell = row.createCell(9);
            cell.setCellValue("修改时间");
            cell = row.createCell(10);
            cell.setCellValue("修改者");
        }

        //遍历list，创建HSSFRow，生成数据行
        Innovation innovation = null;
        if (innovationList != null && innovationList.size() > 0) {
            for (int i = 0; i < innovationList.size(); i++) {
                innovation = innovationList.get(i);

                //每遍历出一个innovation，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从innovation中获取
                cell = row.createCell(0);
                cell.setCellValue(innovation.getId());
                cell = row.createCell(1);
                cell.setCellValue(innovation.getProjectName());
                cell = row.createCell(2);
                cell.setCellValue(innovation.getProjectGrade());
                cell = row.createCell(3);
                cell.setCellValue(innovation.getStuId());
                cell = row.createCell(4);
                cell.setCellValue(innovation.getUser().getName());
                cell = row.createCell(5);
                cell.setCellValue(innovation.getSchoolYear());
                cell = row.createCell(6);
                cell.setCellValue(innovation.getInstructor());
                cell = row.createCell(7);
                cell.setCellValue(innovation.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(innovation.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(innovation.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(innovation.getEditBy());
            }
        }

        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=innovationList.xls");
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
    @RequestMapping("/workbench/innovation/exportInnovationsByIds.do")
    public void exportInnovationsByIds(String[] id,HttpServletResponse response) throws IOException {

        // 调用service层方法，根据ids查询被选中的创新创业计划的信息
        List<Innovation> innovationList = innovationService.queryInnovationsByIds(id);
        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("创新创业计划表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        {
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("项目名称");
            cell = row.createCell(2);
            cell.setCellValue("项目级别");
            cell = row.createCell(3);
            cell.setCellValue("学生学号");
            cell = row.createCell(4);
            cell.setCellValue("学生姓名");
            cell = row.createCell(5);
            cell.setCellValue("学年");
            cell = row.createCell(6);
            cell.setCellValue("指导教师");
            cell = row.createCell(7);
            cell.setCellValue("创建时间");
            cell = row.createCell(8);
            cell.setCellValue("创建者");
            cell = row.createCell(9);
            cell.setCellValue("修改时间");
            cell = row.createCell(10);
            cell.setCellValue("修改者");
        }

        //遍历list，创建HSSFRow，生成数据行
        Innovation innovation = null;
        if (innovationList != null && innovationList.size() > 0) {
            for (int i = 0; i < innovationList.size(); i++) {
                innovation = innovationList.get(i);

                //每遍历出一个innovation，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从innovation中获取
                cell = row.createCell(0);
                cell.setCellValue(innovation.getId());
                cell = row.createCell(1);
                cell.setCellValue(innovation.getProjectName());
                cell = row.createCell(2);
                cell.setCellValue(innovation.getProjectGrade());
                cell = row.createCell(3);
                cell.setCellValue(innovation.getStuId());
                cell = row.createCell(4);
                cell.setCellValue(innovation.getUser().getName());
                cell = row.createCell(5);
                cell.setCellValue(innovation.getSchoolYear());
                cell = row.createCell(6);
                cell.setCellValue(innovation.getInstructor());
                cell = row.createCell(7);
                cell.setCellValue(innovation.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(innovation.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(innovation.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(innovation.getEditBy());
            }
        }

        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=innovationXzList.xls");
        OutputStream out=response.getOutputStream();

        workbook.write(out);

        workbook.close();
        out.flush();
    }


    /**
     * 导入文件,文件上传需要配置文件上传解析器
     * @param innovationFile 响应对象
     */
    @RequestMapping("/workbench/innovation/importInnovationsByList.do")
    @ResponseBody
    public Msg importInnovationsByList(MultipartFile innovationFile, HttpServletRequest request ,HttpSession session) {
        Msg msg = new Msg();

        User user = (User) session.getAttribute(Contants.SESSION_USER);

        try {
            // 把excel文件写如磁盘目录中
           /* String originalFilename = activityFile.getOriginalFilename();
            File file = new File("D:\\满级人类\\mynotes\\CRM客户管理系统\\serverDir",originalFilename);
            activityFile.transferTo(file);*/

            // 解析excel文件，获取文件中的数据，并且封装成activityList
            // FileInputStream is = new FileInputStream("D:\\满级人类\\mynotes\\CRM客户管理系统\\serverDir" + originalFilename);

            InputStream is = innovationFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(is);

            // 获取sheet
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFRow row = null;
            Innovation innovation = null;
            HSSFCell cell =null;

            List<Innovation> innovationList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                innovation = new Innovation();
                innovation.setId(UUIDUtils.getUUID());
                innovation.setCreateTime(DateUtils.formateDateTime(new Date()));
                innovation.setCreateBy(user.getId());

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if (j==0){
                        innovation.setProjectName(cellValue);
                    }else if (j==1){
                        innovation.setProjectGrade(cellValue);
                    }else if (j==2){
                        innovation.setStuId(cellValue);
                    }else if (j==3){
                        innovation.setInstructor(cellValue);
                    }else if (j==4){
                        innovation.setSchoolYear(cellValue);
                    }
                }
                innovationList.add(innovation);
            }

            int ret = innovationService.saveCreateInnovationsByList(innovationList);


            msg.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            msg.setRetData(ret);

        } catch (IOException e) {
            e.printStackTrace();
            msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            msg.setMessage("系统繁忙，请稍后重试...");
        }
        return msg;

    }


    @RequestMapping("/workbench/innovation/detailInnovation.do")
    public String detailInnovation(String id,HttpServletRequest request){
        Innovation innovation = innovationService.queryInnovationForDetailById(id);
        request.setAttribute("innovation",innovation);
        return "workbench/innovation/detail";
    }


}
