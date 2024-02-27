package com.xunmaw.dormitory.controller;


import com.xunmaw.dormitory.po.Admin;
import com.xunmaw.dormitory.po.PageInfo;
import com.xunmaw.dormitory.service.AdminService;
import com.xunmaw.dormitory.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户控制器类
 */
@Controller
public class AdminController {
	// 依赖注入
	@Autowired
	private AdminService adminService;
	/**er
	 * 用户登录
	 */
	/**
	 * 将提交数据(username,password)写入Admin对象
	 */
	@ResponseBody
	@RequestMapping(value = "/login")
	public String login(@RequestBody Admin admin, HttpSession session, HttpServletRequest request) {
		// 通过账号和密码查询用户
		admin.setA_password(MD5Util.MD5EncodeUtf8(admin.getA_password()));
		Admin ad = adminService.findAdmin(admin);
		if(ad != null){
			session.setAttribute("admin", ad);
			return "success";
		}
		return "fail";
	}

	/**
	 * 退出登录
	 */
	@RequestMapping(value = "/loginOut")
	public String loginOut(Admin admin, Model model, HttpSession session) {
		session.invalidate();
		return "login";
	}

	/**
	 * 分页查询
	 */
	@RequestMapping(value = "/findAdmin")
	public String findAdmin(String a_username, String a_describe,Integer pageIndex,
							Integer a_id ,Integer pageSize, Model model) {

		PageInfo<Admin> ai = adminService.findPageInfo(a_username,a_describe,
								a_id,pageIndex,pageSize);
		model.addAttribute("ai",ai);
		return "admin_list";
	}

	/**
	 * 导出Excel
	 */
	@RequestMapping(value = "/exportadminlist" , method = RequestMethod.POST)
    @ResponseBody
	public List<Admin> exportAdmin(){
		List<Admin> admin = adminService.getAll();
		return admin;
	}

	/**
	 * 添加管理员信息
	 */
	@RequestMapping(value = "/addAdmin" ,method = RequestMethod.POST)
	@ResponseBody
	public String addAdmin( @RequestBody Admin admin) {
		admin.setA_password(MD5Util.MD5EncodeUtf8(admin.getA_password()));
		int a = adminService.addAdmin(admin);
		return "admin_list";
	}

	/**
	 * 删除管理员信息；将请求体a_id写入参数a_id
	 */
	@RequestMapping( "/deleteAdmin")
	@ResponseBody
	public String deleteAdmin(Integer a_id) {
		int a = adminService.deleteAdmin(a_id);
		return "admin_list";
	}

	/**
	 * 修改管理员信息
	 */
	/**
	 * 将提交数据(a_id,a_username...)写入Admin对象
	 */
	@RequestMapping( value = "/updateAdmin", method = RequestMethod.POST)
	public String updateAdmin(Admin admin) {

		admin.setA_password(MD5Util.MD5EncodeUtf8(admin.getA_password()));
		int a = adminService.updateAdmin(admin);
		return "redirect:/findAdmin";
	}

	@GetMapping("/homepage")
	public ModelAndView toHomepage(ModelAndView modelAndView){
		modelAndView.setViewName("homepage");
		return modelAndView;
	}


	/**
	 * 根据管理员Id搜索;将请求数据a_id写入参数a_id
	 */
	@RequestMapping( "/findAdminById")
	public String findAdminById( Integer a_id,HttpSession session) {
		Admin a= adminService.findAdminById(a_id);
		session.setAttribute("a",a);
		return "admin_edit";
	}

}
