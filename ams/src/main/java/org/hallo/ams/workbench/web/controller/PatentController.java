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
import org.hallo.ams.workbench.domain.Patent;
import org.hallo.ams.workbench.service.PatentService;
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
public class PatentController {

    @Autowired
    private PatentService patentService;
    @Autowired
    private UserService userService;
    @Autowired
    private InstructorService instructorService;

    /**
     * 专利的首页
     * @param request
     * @return
     */
    @RequestMapping("/workbench/patent/index.do")
    public String index(HttpServletRequest request){

        //跳转到patent index
        return "workbench/patent/index";
    }


    /**
     * 专利的添加功能
     * @param patent
     * @return
     */
    @RequestMapping("/workbench/patent/createPatent.do")
    @ResponseBody
    public Object createPatent(Patent patent , HttpServletRequest request){
        //UUID
        patent.setId(UUIDUtils.getUUID());
        patent.setCreateTime(DateUtils.formateDateTime(new Date()));
        //
        patent.setCreateBy(((User) request.getSession().getAttribute(Contants.SESSION_USER)).getId());

        Msg msg = new Msg();



        try {
            //调用service层方法，添加专利
            int i = patentService.saveCreatePatent(patent);

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


    @RequestMapping("/workbench/patent/retStuName.do")
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
    /*@RequestMapping("/workbench/Patent/queryteaNameByName.do")
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
    @RequestMapping("/workbench/patent/queryPatentByConditionForPage.do")
    public Object queryPatentByConditionForPage(String patentName, String ownerId,
                                                    String ownerName, String acceptanceTime,
                                                    String approvalTime,int pageNo, int pageSize) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("patentName", patentName);
        map.put("stuId", ownerId);
        map.put("stuName", ownerName);
        map.put("acceptanceTime", acceptanceTime);
        map.put("approvalTime", approvalTime);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);

        //查询
        List<Patent> patentList = patentService.queryPatentByConditionForPage(map);
        int totalRows = patentService.queryCountOfPatentByCondition(map);

        //响应信息 java对象转json    -》 map 实体类 基本类型 list 数组
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("patentList", patentList);
        retMap.put("totalRows", totalRows);

        return retMap;
    }


    /**
     * 根据id批量删除
     * @param id
     * @return
     */
    @RequestMapping("/workbench/patent/deletePatentIds.do")
    @ResponseBody
    public Object deletePatentIds(String[] id) {

        Msg msg = new Msg();

        try {
            int ret = patentService.deletePatentByIds(id);
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
     * @param id 专利id
     * @return 专利
     */
    @RequestMapping("/workbench/patent/queryPatentById.do")
    @ResponseBody
    public Object queryPatentById(String id) {
        return patentService.queryPatentById(id);
    }

    /**
     * 处理保存更新专利
     *
     * @return 专利修改状态
     */
    @RequestMapping("/workbench/patent/updateEditPatent.do")
    @ResponseBody
    public Object updateEditPatent(Patent patent, HttpServletRequest request) {
        //被谁修改的，从session中取出user的id
        patent.setEditBy(((User) request.getSession().getAttribute(Contants.SESSION_USER)).getId());
        patent.setEditTime(DateUtils.formateDateTime(new Date()));

        Msg msg = new Msg();
        try {
            //update
            int ret = patentService.saveEditPatent(patent);
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
    @RequestMapping("/workbench/patent/exportAllPatents.do")
    public void exportAllPatents(HttpServletResponse response) throws IOException {
        //查询所有专利
        List<Patent> patentList = patentService.queryAllPatents();
        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("专利表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        {
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("专利名称");
            cell = row.createCell(2);
            cell.setCellValue("专利类别");
            cell = row.createCell(3);
            cell.setCellValue("发明人学号");
            cell = row.createCell(4);
            cell.setCellValue("姓名");
            cell = row.createCell(5);
            cell.setCellValue("受理时间");
            cell = row.createCell(6);
            cell.setCellValue("批准时间");
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
        Patent patent = null;
        if (patentList != null && patentList.size() > 0) {
            for (int i = 0; i < patentList.size(); i++) {
                patent = patentList.get(i);

                //每遍历出一个Patent，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从Patent中获取
                cell = row.createCell(0);
                cell.setCellValue(patent.getId());
                cell = row.createCell(1);
                cell.setCellValue(patent.getPatentName());
                cell = row.createCell(2);
                cell.setCellValue(patent.getCategory());
                cell = row.createCell(3);
                cell.setCellValue(patent.getStuId());
                cell = row.createCell(4);
                cell.setCellValue(patent.getUser().getName());
                cell = row.createCell(5);
                cell.setCellValue(patent.getAcceptanceTime());
                cell = row.createCell(6);
                cell.setCellValue(patent.getApprovalTime());
                cell = row.createCell(7);
                cell.setCellValue(patent.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(patent.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(patent.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(patent.getEditBy());
            }
        }

        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=patentList.xls");
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
    @RequestMapping("/workbench/patent/exportPatentsByIds.do")
    public void exportPatentsByIds(String[] id,HttpServletResponse response) throws IOException {

        // 调用service层方法，根据ids查询被选中的专利的信息
        List<Patent> patentList = patentService.queryPatentsByIds(id);

        //创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("专利表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        {
            cell.setCellValue("ID");
            cell = row.createCell(1);
            cell.setCellValue("专利名称");
            cell = row.createCell(2);
            cell.setCellValue("专利类别");
            cell = row.createCell(3);
            cell.setCellValue("发明人学号");
            cell = row.createCell(4);
            cell.setCellValue("姓名");
            cell = row.createCell(5);
            cell.setCellValue("受理时间");
            cell = row.createCell(6);
            cell.setCellValue("批准时间");
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
        Patent patent = null;
        if (patentList != null && patentList.size() > 0) {
            for (int i = 0; i < patentList.size(); i++) {
                patent = patentList.get(i);

                //每遍历出一个Patent，生成一行
                row=sheet.createRow(i+1);
                //每一行创建11列，每一列的数据从Patent中获取
                cell = row.createCell(0);
                cell.setCellValue(patent.getId());
                cell = row.createCell(1);
                cell.setCellValue(patent.getPatentName());
                cell = row.createCell(2);
                cell.setCellValue(patent.getCategory());
                cell = row.createCell(3);
                cell.setCellValue(patent.getStuId());
                cell = row.createCell(4);
                cell.setCellValue(patent.getUser().getName());
                cell = row.createCell(5);
                cell.setCellValue(patent.getAcceptanceTime());
                cell = row.createCell(6);
                cell.setCellValue(patent.getApprovalTime());
                cell = row.createCell(7);
                cell.setCellValue(patent.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(patent.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(patent.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(patent.getEditBy());
            }
        }



        //把生成的excel文件下载到客户端
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=patentXzList.xls");
        OutputStream out=response.getOutputStream();

        workbook.write(out);

        workbook.close();
        out.flush();
    }


    /**
     * 导入文件,文件上传需要配置文件上传解析器
     * @param patentFile 响应对象
     */
    @RequestMapping("/workbench/patent/importPatentsByList.do")
    @ResponseBody
    public Msg importPatentsByList(MultipartFile patentFile, HttpServletRequest request , HttpSession session) {
        Msg msg = new Msg();

        User user = (User) session.getAttribute(Contants.SESSION_USER);

        try {
            // 把excel文件写如磁盘目录中
           /* String originalFilename = activityFile.getOriginalFilename();
            File file = new File("D:\\满级人类\\mynotes\\CRM客户管理系统\\serverDir",originalFilename);
            activityFile.transferTo(file);*/

            // 解析excel文件，获取文件中的数据，并且封装成activityList
            // FileInputStream is = new FileInputStream("D:\\满级人类\\mynotes\\CRM客户管理系统\\serverDir" + originalFilename);

            InputStream is = patentFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(is);

            // 获取sheet
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFRow row = null;
            Patent patent = null;
            HSSFCell cell =null;

            List<Patent> patentList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                patent = new Patent();
                patent.setId(UUIDUtils.getUUID());
                patent.setCreateTime(DateUtils.formateDateTime(new Date()));
                patent.setCreateBy(user.getId());

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if (j==0){
                        patent.setPatentName(cellValue);
                    }else if (j==1){
                        patent.setCategory(cellValue);
                    }else if (j==2){
                        patent.setStuId(cellValue);
                    }else if (j==3){
                        patent.setAcceptanceTime(cellValue);
                    }else if (j==4){
                        patent.setApprovalTime(cellValue);
                    }
                }
                patentList.add(patent);
            }

            int ret = patentService.saveCreatePatentsByList(patentList);


            msg.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            msg.setRetData(ret);

        } catch (IOException e) {
            e.printStackTrace();
            msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            msg.setMessage("系统繁忙，请稍后重试...");
        }
        return msg;

    }


    @RequestMapping("/workbench/patent/detailPatent.do")
    public String detailPatent(String id,HttpServletRequest request){
        Patent patent = patentService.queryPatentForDetailById(id);
        request.setAttribute("patent",patent);
        return "workbench/patent/detail";
    }


}
