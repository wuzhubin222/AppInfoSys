package cn.appsys.service.dev.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface DevAppInfoService {

	List<AppInfo> queryAppPageInfo(
			@Param("querySoftwareName")String querySoftwareName,
			@Param("queryStatus")Integer queryStatus,
			@Param("queryFlatformId")Integer queryFlatformId,
			@Param("queryCategoryLevel1")Integer queryCategoryLevel1,
			@Param("queryCategoryLevel2")Integer queryCategoryLevel2,
			@Param("queryCategoryLevel3")Integer queryCategoryLevel3,
			@Param("from") Integer from,   //位置偏移量
			@Param("pageSize") Integer pageSize);

	//查询总记录数，用于分页
	int queryCount(
			@Param("querySoftwareName")String querySoftwareName,
			@Param("queryStatus")Integer queryStatus,
			@Param("queryFlatformId")Integer queryFlatformId,
			@Param("queryCategoryLevel1")Integer queryCategoryLevel1,
			@Param("queryCategoryLevel2")Integer queryCategoryLevel2,
			@Param("queryCategoryLevel3")Integer queryCategoryLevel3);
	/**
	 * 通过appinfo表的主键id与APKName字段查询app详情信息。
	 * @param id 主键id
	 * @param APKName
	 * @return app详情信息。
	 */
	AppInfo queryInfoByIdAndAPK(Integer id, String APKName);

	//新增
	int insertAppInfo(AppInfo info);
	
	/**
	 * 根据id将数据库中app_info表里指定的logoPicPath和logoLocPath两个列的值给清空
	 * @param id appinfo的id
	 * @return 受影响的行数
	 */
	boolean deleteLogPath(Integer id);
	
	//修改
	boolean modifyAppInfo(AppInfo appInfo);
}
