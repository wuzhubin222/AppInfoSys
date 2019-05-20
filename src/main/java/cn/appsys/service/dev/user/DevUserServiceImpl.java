package cn.appsys.service.dev.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.dev.user.DevUserMapper;
import cn.appsys.pojo.DevUser;

@Service("devUserService")
public class DevUserServiceImpl implements DevUserService {

	@Resource
	private DevUserMapper devUserMapper;
	
	@Override
	public DevUser queryCodeAndPwd(String devCode, String devPassword) {
		return devUserMapper.queryCodeAndPwd(devCode, devPassword);
	}

}
