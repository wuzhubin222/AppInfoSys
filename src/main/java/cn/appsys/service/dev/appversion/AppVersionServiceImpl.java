package cn.appsys.service.dev.appversion;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.dev.appversion.AppVersionMapper;
import cn.appsys.pojo.AppVersion;

@Service("appVersionService")
public class AppVersionServiceImpl implements AppVersionService {

	@Resource
	private AppVersionMapper appVersionMapper;
	
	@Override
	public List<AppVersion> queryAppVersionListByAppId(Integer id) {
		return appVersionMapper.queryAppVersionListByAppId(id);
	}

	@Override
	public boolean addAppVersion(AppVersion appVersion) {
		int result = appVersionMapper.addAppVersion(appVersion);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public AppVersion getAppVersionByVid(Integer vid) {
		return appVersionMapper.getAppVersionByVid(vid);
	}

	@Override
	public boolean deleteApkPath(Integer vid) {
		int result = appVersionMapper.deleteApkPath(vid);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

}
