package cn.appsys.dao.dev.user;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DevUser;

//开发者平台所对应用户接口
public interface DevUserMapper {
	
	/**
	 * 根据开发者用户的编码和用户的密码查询开发者用户信息详情(用于实现登录功能)
	 * @param devCode 用户的编码
	 * @param devPassword 用户的密码
	 * @return 返回开发者用户信息详情
	 */
	DevUser queryCodeAndPwd(@Param("devCode") String devCode,
			@Param("devPassword") String devPassword);
}