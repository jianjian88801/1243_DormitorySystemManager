package com.xunmaw.dormitory.service;
import com.xunmaw.dormitory.po.Class;
import com.xunmaw.dormitory.po.PageInfo;

import java.util.List;

/**
 * 用户Service层接口
 */
public interface ClassService {

	//分页查询
	public PageInfo<Class> findPageInfo(String c_classname, String c_counsellor, Integer c_classid, Integer pageIndex, Integer pageSize);

	public int deleteClass(Integer c_id);   //删除班级信息
	public boolean addClass(Class ucalss);    //添加班级信息
	public Class findClassById(Integer c_id);
	public Class findClassByCid(Integer c_classid);
	public int updateClass(Class uclass); //修改班级信息
	public List<Class> findClassStudent(Class uclass);//查询班级人员信息
	public List<Class> getAll();
}
