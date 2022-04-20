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
import org.hallo.ams.workbench.domain.Scientific;
import org.hallo.ams.workbench.service.InstructorService;
import org.hallo.ams.workbench.service.ScientificService;
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
 * @create 2022-04-03 21:14
 */
@Controller
public class ScientificController {

    @Autowired
    private ScientificService scientificService;
    @Autowired
    private UserService userService;
    @Autowired
    private InstructorService instructorService;

    /**
     * 首页
     * @return
     */
    @RequestMapping("/workbench/scientific/index.do")
    public String index(HttpServletRequest servletRequest){
        //跳转到 index
        return "workbench/scientific/index";
    }

    /**
     * 科研项目的添加功能
     * @param scientific
     * @return
     */
    @RequestMapping("/workbench/scientific/createScientific.do")
    @ResponseBody
    public Object createScientific(Scientific scientific , HttpServletRequest request){
        //UUID
        scientific.setId(UUIDUtils.getUUID());
        scientific.setCreateTime(DateUtils.formateDateTime(new Date()));
        //
        scientific.setCreateBy(((User) request.getSession().getAttribute(Contants.SESSION_USER)).getId());

        Msg msg = new Msg();



        try {
            //调用service层方法
            int i = scientificService.saveCreateScientific(scientific);

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

    @RequestMapping("/workbench/scientific/retStuName.do")
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
    @RequestMapping("/workbench/scientific/queryteaNameByName.do")
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
    @RequestMapping("/workbench/scientific/queryScientificByConditionForPage.do")
    public Object queryScientificByConditionForPage(String projectName, String ownerId,
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
        List<Scientific> scientificList = scientificService.queryScientificByConditionForPage(map);
        int totalRows = scientificService.queryCountOfScientificByCondition(map);

        //响应信息 java对象转json    -》 map 实体类 基本类型 list 数组
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("scientificList", scientificList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }

    /**
     * 根据id批量删除
     * @param id
     * @return
     */
    @RequestMapping("/workbench/scientific/deleteScientificIds.do")
    @ResponseBody
    public Object deleteScientificIds(String[] id) {

        Msg msg = new Msg();

        try {
            int ret = scientificService.deleteScientificByIds(id);
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
    @RequestMapping("/workbench/scientific/queryScientificById.do")
    @ResponseBody
    public Object queryScientificById(String id) {
        return scientificService.queryScientificById(id);
    }


    /**
     * 处理保存更新创新创业计划
     *
     * @return 创新创业计划修改状态
     */
    @RequestMapping("/workbench/scientific/updateEditScientific.do")
    @ResponseBody
    public Object updateEditScientific(Scientific scientific, HttpServletRequest request) {
        //被谁修改的，从session中取出user的id
        scientific.setEditBy(((User) request.getSession().getAttribute(Contants.SESSION_USER)).getId());
        scientific.setEditTime(DateUtils.formateDateTime(new Date()));

        Msg msg = new Msg();
        try {
            //update
            int ret = scientificService.saveEditScientific(scientific);
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
    @RequestMapping("/workbench/scientific/exportAllScientifics.do")
    public void exportAllScientifics(HttpServletResponse response) throws IOException {
        //查询所有创新创业计划
        List<Scientific> scientificList = scientificService.queryAllScientifics();
        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("科研项目计划表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        {
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("项目名称");
            cell = row.createCell(2);
            cell.setCellValue("学生学号");
            cell = row.createCell(3);
            cell.setCellValue("学生姓名");
            cell = row.createCell(4);
            cell.setCellValue("学年");
            cell = row.createCell(5);
            cell.setCellValue("项目负责人");
            cell = row.createCell(6);
            cell.setCellValue("创建时间");
            cell = row.createCell(7);
            cell.setCellValue("创建者");
            cell = row.createCell(8);
            cell.setCellValue("修改时间");
            cell = row.createCell(9);
            cell.setCellValue("修改者");
        }

        //遍历list，创建HSSFRow，生成数据行
        Scientific scientific = null;
        if (scientificList != null && scientificList.size() > 0) {
            for (int i = 0; i < scientificList.size(); i++) {
                scientific = scientificList.get(i);

                //每遍历出一个scientific，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从scientific中获取
                cell = row.createCell(0);
                cell.setCellValue(scientific.getId());
                cell = row.createCell(1);
                cell.setCellValue(scientific.getProjectName());
                cell = row.createCell(2);
                cell.setCellValue(scientific.getStuId());
                cell = row.createCell(3);
                cell.setCellValue(scientific.getUser().getName());
                cell = row.createCell(4);
                cell.setCellValue(scientific.getSchoolYear());
                cell = row.createCell(5);
                cell.setCellValue(scientific.getInstructor());
                cell = row.createCell(6);
                cell.setCellValue(scientific.getCreateTime());
                cell = row.createCell(7);
                cell.setCellValue(scientific.getCreateBy());
                cell = row.createCell(8);
                cell.setCellValue(scientific.getEditTime());
                cell = row.createCell(9);
                cell.setCellValue(scientific.getEditBy());
            }
        }

        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=scientificList.xls");
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
    @RequestMapping("/workbench/scientific/exportScientificsByIds.do")
    public void exportScientificsByIds(String[] id,HttpServletResponse response) throws IOException {

        // 调用service层方法，根据ids查询被选中的创新创业计划的信息
        List<Scientific> scientificList = scientificService.queryScientificsByIds(id);
        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("科研项目计划表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        {
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("项目名称");
            cell = row.createCell(2);
            cell.setCellValue("学生学号");
            cell = row.createCell(3);
            cell.setCellValue("学生姓名");
            cell = row.createCell(4);
            cell.setCellValue("学年");
            cell = row.createCell(5);
            cell.setCellValue("项目负责人");
            cell = row.createCell(6);
            cell.setCellValue("创建时间");
            cell = row.createCell(7);
            cell.setCellValue("创建者");
            cell = row.createCell(8);
            cell.setCellValue("修改时间");
            cell = row.createCell(9);
            cell.setCellValue("修改者");
        }

        //遍历list，创建HSSFRow，生成数据行
        Scientific scientific = null;
        if (scientificList != null && scientificList.size() > 0) {
            for (int i = 0; i < scientificList.size(); i++) {
                scientific = scientificList.get(i);

                //每遍历出一个scientific，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从scientific中获取
                cell = row.createCell(0);
                cell.setCellValue(scientific.getId());
                cell = row.createCell(1);
                cell.setCellValue(scientific.getProjectName());
                cell = row.createCell(2);
                cell.setCellValue(scientific.getStuId());
                cell = row.createCell(3);
                cell.setCellValue(scientific.getUser().getName());
                cell = row.createCell(4);
                cell.setCellValue(scientific.getSchoolYear());
                cell = row.createCell(5);
                cell.setCellValue(scientific.getInstructor());
                cell = row.createCell(6);
                cell.setCellValue(scientific.getCreateTime());
                cell = row.createCell(7);
                cell.setCellValue(scientific.getCreateBy());
                cell = row.createCell(8);
                cell.setCellValue(scientific.getEditTime());
                cell = row.createCell(9);
                cell.setCellValue(scientific.getEditBy());
            }
        }


        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=scientificXzList.xls");
        OutputStream out=response.getOutputStream();

        workbook.write(out);

        workbook.close();
        out.flush();
    }



    /**
     * 导入文件,文件上传需要配置文件上传解析器
     * @param scientificFile 响应对象
     */
    @RequestMapping("/workbench/scientific/importScientificByList.do")
    @ResponseBody
    public Msg importScientificByList(MultipartFile scientificFile, HttpServletRequest request , HttpSession session) {
        Msg msg = new Msg();

        User user = (User) session.getAttribute(Contants.SESSION_USER);

        try {
            // 把excel文件写如磁盘目录中
           /* String originalFilename = activityFile.getOriginalFilename();
            File file = new File("D:\\满级人类\\mynotes\\CRM客户管理系统\\serverDir",originalFilename);
            activityFile.transferTo(file);*/

            // 解析excel文件，获取文件中的数据，并且封装成activityList
            // FileInputStream is = new FileInputStream("D:\\满级人类\\mynotes\\CRM客户管理系统\\serverDir" + originalFilename);

            InputStream is = scientificFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(is);

            // 获取sheet
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFRow row = null;
            Scientific scientific = null;
            HSSFCell cell =null;

            List<Scientific> scientificList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                scientific = new Scientific();
                scientific.setId(UUIDUtils.getUUID());
                scientific.setCreateTime(DateUtils.formateDateTime(new Date()));
                scientific.setCreateBy(user.getId());

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if (j==0){
                        scientific.setProjectName(cellValue);
                    }else if (j==1){
                        scientific.setStuId(cellValue);
                    }else if (j==2){
                        scientific.setInstructor(cellValue);
                    }else if (j==3){
                        scientific.setSchoolYear(cellValue);
                    }
                }
                scientificList.add(scientific);
            }

            int ret = scientificService.saveCreateScientificsByList(scientificList);


            msg.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            msg.setRetData(ret);

        } catch (IOException e) {
            e.printStackTrace();
            msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            msg.setMessage("系统繁忙，请稍后重试...");
        }
        return msg;

    }


    @RequestMapping("/workbench/scientific/detailScientific.do")
    public String detailScientific(String id,HttpServletRequest request){
        Scientific scientific = scientificService.queryScientificForDetailById(id);
        request.setAttribute("scientific",scientific);
        return "workbench/scientific/detail";
    }

}
