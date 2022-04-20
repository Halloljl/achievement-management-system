package org.hallo.ams.settings.web.controller;

import org.hallo.ams.commons.Constants.Contants;
import org.hallo.ams.commons.domain.Msg;
import org.hallo.ams.settings.domain.User;
import org.hallo.ams.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hallo
 * @date 2022/3/31 - 20:47
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到登录界面
     * @return
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        // 请求转发
        return "settings/qx/user/login";
    }


    /**
     * 登录账号
     * @param loginAct
     * @param loginPwd
     * @param isRemPwd
     * @return
     */
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response){
        //封装
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        //返回给前端的数据封装
        Msg msg = new Msg();

        //查询
        User user = userService.queryUserByLoginActAndLoginPwd(map);

        if(user == null){
            msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            msg.setMessage("用户名或者密码错误");
        }else {
            //判断账号的ip
            if(!user.getAllowIps().contains(request.getRemoteAddr())){
                //未授权的ip
                msg.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                msg.setMessage("ip受限");
            }else {
                //登录成功
                msg.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                msg.setRetData(user.getRoleId());
                //把user信息保存到session
                request.getSession().setAttribute(Contants.SESSION_USER,user);
                //记住密码,如果需要记住密码，则使用cookie,  这里保存密码的cookie应该使用密文，
                if("true".equals(isRemPwd)){
                    Cookie loginActCookie = new Cookie("loginAct", user.getId());
                    Cookie loginPwdCookie = new Cookie("loginPwd", user.getLoginPwd());
                    //设置最大存贮时间
                    loginActCookie.setMaxAge(10*24*60*60);
                    loginPwdCookie.setMaxAge(10*24*60*60);
                    //返回Cookie
                    response.addCookie(loginActCookie);
                    response.addCookie(loginPwdCookie);
                }else{
                    //把没有过期的Cookie删除,将Cookie的生命周期置零,cookie的值任意，一般取1
                    Cookie loginActCookie = new Cookie("loginAct", "1");
                    Cookie loginPwdCookie = new Cookie("loginPwd", "1");
                    //设置最大存贮时间,为0 可以让浏览器删除
                    loginActCookie.setMaxAge(0);
                    loginPwdCookie.setMaxAge(0);
                    //返回Cookie
                    response.addCookie(loginActCookie);
                    response.addCookie(loginPwdCookie);
                }
            }
        }
        return msg;
    }



    /**
     * 安全退出
     * @return
     */
    @GetMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session){
        //销毁cookie
        Cookie loginActCookie = new Cookie("loginAct", "");
        Cookie loginPwdCookie = new Cookie("loginPwd", "");
        //设置最大存贮时间,为0 可以让浏览器删除
        loginActCookie.setMaxAge(0);
        loginPwdCookie.setMaxAge(0);
        //返回Cookie
        response.addCookie(loginActCookie);
        response.addCookie(loginPwdCookie);
        //销毁session
        session.invalidate();
        return "redirect:/";
    }


    
}

