package com.xunmaw.dormitory.service.impl;


import com.xunmaw.dormitory.dao.ClassDao;
import com.xunmaw.dormitory.po.Class;
import com.xunmaw.dormitory.po.PageInfo;
import com.xunmaw.dormitory.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户Service接口实现类
 */
@Service("classService")
@Transactional
public class ClassServiceImpl implements ClassService {
	// classDao
	@Autowired
	private ClassDao classDao;


	//分页查询
	@Override
	public PageInfo<Class> findPageInfo(String c_classname, String c_counsellor, Integer c_classid, Integer pageIndex, Integer pageSize) {
		PageInfo<Class> pi = new PageInfo<Class>();
		pi.setPageIndex(pageIndex);
		pi.setPageSize(pageSize);
		//获取总条数
		Integer totalCount = classDao.totalCount(c_classname,c_classid,c_counsellor);
		if (totalCount>0){
			pi.setTotalCount(totalCount);
			//每一页显示班级信息数
			//currentPage = (pageIndex-1)*pageSize  当前页码数减1*最大条数=开始行数
		List<Class> classList =	classDao.getClassList(c_classname,c_classid,c_counsellor,
				     (pi.getPageIndex()-1)*pi.getPageSize(),pi.getPageSize());
		  pi.setList(classList);
		}
		return pi;
	}

	@Override
	public List<Class> getAll(){
		List<Class> classList = classDao.getAll();
		return  classList;
	}

	//通过id删除班级信息
	@Override
	public int deleteClass(Integer c_id) {
		return classDao.deleteClass(c_id);
	}

	//添加班级信息
	@Override
	public boolean addClass(Class uclass) {
		Class one = classDao.findClassByCid(uclass.getC_classid());
		if (one != null) return false;
		else {
			classDao.addClass(uclass);
			return true;
		}
	}

	@Override
	public Class findClassById (Integer c_id){
		Class c = classDao.findClassById(c_id);
		return  c;
	}

	@Override
	public Class findClassByCid(Integer c_classid) {
		return classDao.findClassByCid(c_classid);
	}

	//修改班级信息
	@Override
	public int updateClass(Class uclass) {
		return classDao.updateClass(uclass);
	}

	//查询宿舍人员信息
	@Override
	public List<Class> findClassStudent(Class uclass) {
		List<Class> c = classDao.findClassStudent(uclass);
		return c;
	}
}
