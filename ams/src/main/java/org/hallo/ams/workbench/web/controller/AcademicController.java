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
import org.hallo.ams.workbench.domain.Academic;
import org.hallo.ams.workbench.service.AcademicService;
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
 * @datetime 2022-04-19 0:26
 * @description
 */
@Controller
public class AcademicController {
    
    @Autowired
    private AcademicService academicService;
    @Autowired
    private UserService userService;



    /**
     * 学术论文的首页
     * @param request
     * @return
     */
    @RequestMapping("/workbench/academic/index.do")
    public String index(HttpServletRequest request){

        //跳转到Academic index
        return "workbench/academic/index";
    }


    /**
     * 学术论文的添加功能
     * @param academic
     * @return
     */
    @RequestMapping("/workbench/academic/createAcademic.do")
    @ResponseBody
    public Object createAcademic(Academic academic , HttpServletRequest request){
        //UUID
        academic.setId(UUIDUtils.getUUID());
        academic.setCreateTime(DateUtils.formateDateTime(new Date()));
        //
        academic.setCreateBy(((User) request.getSession().getAttribute(Contants.SESSION_USER)).getId());

        Msg msg = new Msg();



        try {
            //调用service层方法，添加学术论文
            int i = academicService.saveCreateAcademic(academic);

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


    @RequestMapping("/workbench/academic/retStuName.do")
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
    /*@RequestMapping("/workbench/Academic/queryteaNameByName.do")
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
    @RequestMapping("/workbench/academic/queryAcademicByConditionForPage.do")
    public Object queryAcademicByConditionForPage(String academicName, String ownerId,
                                                String ownerName, String periodical,
                                                String publishedTime,int pageNo, int pageSize) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("academicName", academicName);
        map.put("stuId", ownerId);
        map.put("stuName", ownerName);
        map.put("periodical", periodical);
        map.put("publishedTime", publishedTime);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);

        //查询
        List<Academic> academicList = academicService.queryAcademicByConditionForPage(map);
        int totalRows = academicService.queryCountOfAcademicByCondition(map);

        //响应信息 java对象转json    -》 map 实体类 基本类型 list 数组
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("academicList", academicList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }


    /**
     * 根据id批量删除
     * @param id
     * @return
     */
    @RequestMapping("/workbench/academic/deleteAcademicIds.do")
    @ResponseBody
    public Object deleteAcademicIds(String[] id) {

        Msg msg = new Msg();

        try {
            int ret = academicService.deleteAcademicByIds(id);
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
     * @param id 学术论文id
     * @return 学术论文
     */
    @RequestMapping("/workbench/academic/queryAcademicById.do")
    @ResponseBody
    public Object queryAcademicById(String id) {
        return academicService.queryAcademicById(id);
    }

    /**
     * 处理保存更新学术论文
     *
     * @return 学术论文修改状态
     */
    @RequestMapping("/workbench/academic/updateEditAcademic.do")
    @ResponseBody
    public Object updateEditAcademic(Academic academic, HttpServletRequest request) {
        //被谁修改的，从session中取出user的id
        academic.setEditBy(((User) request.getSession().getAttribute(Contants.SESSION_USER)).getId());
        academic.setEditTime(DateUtils.formateDateTime(new Date()));

        Msg msg = new Msg();
        try {
            //update
            int ret = academicService.saveEditAcademic(academic);
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
    @RequestMapping("/workbench/academic/exportAllAcademics.do")
    public void exportAllAcademics(HttpServletResponse response) throws IOException {
        //查询所有学术论文
        List<Academic> academicList = academicService.queryAllAcademics();
        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("学术论文表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        {
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("论文名称");
            cell = row.createCell(2);
            cell.setCellValue("学号");
            cell = row.createCell(3);
            cell.setCellValue("学生作者");
            cell = row.createCell(4);
            cell.setCellValue("排名");
            cell = row.createCell(5);
            cell.setCellValue("发表期刊");
            cell = row.createCell(6);
            cell.setCellValue("发表时间");
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
        Academic academic = null;
        if (academicList != null && academicList.size() > 0) {
            for (int i = 0; i < academicList.size(); i++) {
                academic = academicList.get(i);

                //每遍历出一个Academic，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从Academic中获取
                cell = row.createCell(0);
                cell.setCellValue(academic.getId());
                cell = row.createCell(1);
                cell.setCellValue(academic.getAcademicName());
                cell = row.createCell(2);
                cell.setCellValue(academic.getStuId());
                cell = row.createCell(3);
                cell.setCellValue(academic.getUser().getName());
                cell = row.createCell(4);
                cell.setCellValue(academic.getRanking());
                cell = row.createCell(5);
                cell.setCellValue(academic.getPeriodical());
                cell = row.createCell(6);
                cell.setCellValue(academic.getPublishedTime());
                cell = row.createCell(7);
                cell.setCellValue(academic.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(academic.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(academic.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(academic.getEditBy());
            }
        }

        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=academicList.xls");
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
    @RequestMapping("/workbench/academic/exportAcademicsByIds.do")
    public void exportAcademicsByIds(String[] id,HttpServletResponse response) throws IOException {

        // 调用service层方法，根据ids查询被选中的学术论文的信息
        List<Academic> academicList = academicService.queryAcademicsByIds(id);

        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("学术论文表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        {
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("论文名称");
            cell = row.createCell(2);
            cell.setCellValue("学号");
            cell = row.createCell(3);
            cell.setCellValue("学生作者");
            cell = row.createCell(4);
            cell.setCellValue("排名");
            cell = row.createCell(5);
            cell.setCellValue("发表期刊");
            cell = row.createCell(6);
            cell.setCellValue("发表时间");
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
        Academic academic = null;
        if (academicList != null && academicList.size() > 0) {
            for (int i = 0; i < academicList.size(); i++) {
                academic = academicList.get(i);

                //每遍历出一个Academic，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从Academic中获取
                cell = row.createCell(0);
                cell.setCellValue(academic.getId());
                cell = row.createCell(1);
                cell.setCellValue(academic.getAcademicName());
                cell = row.createCell(2);
                cell.setCellValue(academic.getStuId());
                cell = row.createCell(3);
                cell.setCellValue(academic.getUser().getName());
                cell = row.createCell(4);
                cell.setCellValue(academic.getRanking());
                cell = row.createCell(5);
                cell.setCellValue(academic.getPeriodical());
                cell = row.createCell(6);
                cell.setCellValue(academic.getPublishedTime());
                cell = row.createCell(7);
                cell.setCellValue(academic.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(academic.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(academic.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(academic.getEditBy());
            }
        }


        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=academicXzList.xls");
        OutputStream out=response.getOutputStream();

        workbook.write(out);

        workbook.close();
        out.flush();
    }


    /**
     * 导入文件,文件上传需要配置文件上传解析器
     * @param academicFile 响应对象
     */
    @RequestMapping("/workbench/academic/importAcademicsByList.do")
    @ResponseBody
    public Msg importAcademicsByList(MultipartFile academicFile, HttpServletRequest request , HttpSession session) {
        Msg msg = new Msg();

        User user = (User) session.getAttribute(Contants.SESSION_USER);

        try {
            // 把excel文件写如磁盘目录中
           /* String originalFilename = activityFile.getOriginalFilename();
            File file = new File("D:\\满级人类\\mynotes\\CRM客户管理系统\\serverDir",originalFilename);
            activityFile.transferTo(file);*/

            // 解析excel文件，获取文件中的数据，并且封装成activityList
            // FileInputStream is = new FileInputStream("D:\\满级人类\\mynotes\\CRM客户管理系统\\serverDir" + originalFilename);

            InputStream is = academicFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(is);

            // 获取sheet
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFRow row = null;
            Academic academic = null;
            HSSFCell cell =null;

            List<Academic> academicList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                academic = new Academic();
                academic.setId(UUIDUtils.getUUID());
                academic.setCreateTime(DateUtils.formateDateTime(new Date()));
                academic.setCreateBy(user.getId());

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if (j==0){
                        academic.setAcademicName(cellValue);
                    }else if (j==1){
                        academic.setStuId(cellValue);
                    }else if (j==2){
                        academic.setRanking(cellValue);
                    }else if (j==3){
                        academic.setPeriodical(cellValue);
                    }else if (j==4){
                        academic.setPublishedTime(cellValue);
                    }
                }
                academicList.add(academic);
            }

            System.out.println(academicList);
            int ret = academicService.saveCreateAcademicsByList(academicList);


            msg.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            msg.setRetData(ret);

        } catch (IOException e) {
            e.printStackTrace();
            msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            msg.setMessage("系统繁忙，请稍后重试...");
        }
        return msg;

    }


    @RequestMapping("/workbench/academic/detailAcademic.do")
    public String detailAcademic(String id,HttpServletRequest request){
        Academic academic = academicService.queryAcademicForDetailById(id);
        request.setAttribute("academic",academic);
        return "workbench/academic/detail";
    }

}
