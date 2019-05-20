package cn.appsys.service.dev.appcategory;

import java.util.List;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryService {
	
	//按父分类id进行查询，如：当用户选择一级分类所在下拉框时，这时拿到一级分类的id到这里将当前这个
	//一级分类下面的所有二级分类都查询出来，保存到List泛型中，用于在二级分类所在下拉框中进行显示。
	List<AppCategory> queryById(Integer parentId);
}
