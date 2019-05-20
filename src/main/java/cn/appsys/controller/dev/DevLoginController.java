package cn.appsys.controller.dev;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.constant.Constants;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.dev.user.DevUserService;

@Controller
@RequestMapping("/dev/login")
public class DevLoginController {

	@Resource
	private DevUserService devUserService;
	
	// 只负责跳转到开发者平台的登录页面
	@RequestMapping("/login")
	public String login() {
		return "devlogin";
	}

	// 用于处理登录
	@RequestMapping("/dologin")
	public String doLogin(HttpSession session,HttpServletRequest request,
			@RequestParam("devCode") String devCode,
			@RequestParam("devPassword") String devPassword) {
		DevUser devUser = devUserService.queryCodeAndPwd(devCode, devPassword);
		if (devUser != null) {  //用于登录成功，将用户对象保存到session作用域中
			session.setAttribute(Constants.DEV_USER_SESSION, devUser);
			return "developer/main";  //转发到开发者平台的主页面
		} else {
			request.setAttribute("error", "用户名或密码不正确！");
			return "devlogin";
		}
	}
	
	//实现注销操作
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		//将当前登录用户从session作用域中进行删除
		session.removeAttribute(Constants.DEV_USER_SESSION);
		return "devlogin";  //跳转到开发者平台的登录页面。
	}
}
