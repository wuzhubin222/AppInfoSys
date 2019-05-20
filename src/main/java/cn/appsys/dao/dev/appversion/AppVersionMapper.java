package cn.appsys.dao.dev.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionMapper {

	/**
	 * 根据appinfo的id去查询当前app的历史版本列表信息(app_version)
	 * @param id appinfo的id
	 * @return app的历史版本列表信息
	 */
	List<AppVersion> queryAppVersionListByAppId(@Param("appId")Integer id);
	
	/**
	 * 新增app版本信息
	 * @param appVersion app版本对象，   appVersion对象中id的属性值是没有吧。
	 * @return 受影响的行数
	 */
	int addAppVersion(AppVersion appVersion);
	
	/**
	 * 根据版本id查询当前版本的详情对象信息
	 * @param vid 版本id
	 * @return 当前版本的详情对象信息
	 */
	AppVersion getAppVersionByVid(@Param("vid") Integer vid);
	/**
	 * 根据版本id，将app_version表中apkLocPath、downloadLink、apkFileName这三个字段给清空
	 * @param vid 版本id
	 * @return 受影响的行数
	 */
	int deleteApkPath(@Param("vid")Integer vid);
}
