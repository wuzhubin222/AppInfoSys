package cn.appsys.service.dev.user;

import cn.appsys.pojo.DevUser;

public interface DevUserService {

	/**
	 * 根据开发者用户的编码和用户的密码查询开发者用户信息详情(用于实现登录功能)
	 * @param devCode 用户的编码
	 * @param devPassword 用户的密码
	 * @return 返回开发者用户信息详情
	 */
	DevUser queryCodeAndPwd(String devCode,String devPassword);
}
