package cn.appsys.service.dev.appcategory;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.dev.appcategory.AppCategoryMapper;
import cn.appsys.pojo.AppCategory;

@Service("appCategoryService")
public class AppCategoryServiceImpl implements AppCategoryService {

	@Resource
	private AppCategoryMapper appCategoryMapper;
	
	@Override
	public List<AppCategory> queryById(Integer parentId) {
		return appCategoryMapper.queryById(parentId);
	}

}
