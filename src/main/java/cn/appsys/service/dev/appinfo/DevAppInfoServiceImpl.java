package cn.appsys.service.dev.appinfo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.dev.appinfo.DevAppInfoMapper;
import cn.appsys.pojo.AppInfo;

@Service("devAppInfoService")
public class DevAppInfoServiceImpl implements DevAppInfoService {

	@Resource
	private DevAppInfoMapper devAppInfoMapper;
	
	@Override
	public List<AppInfo> queryAppPageInfo(String querySoftwareName,
			Integer queryStatus, Integer queryFlatformId,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer from, Integer pageSize) {
		return devAppInfoMapper.queryAppPageInfo(querySoftwareName, queryStatus, queryFlatformId, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, from, pageSize);
	}

	@Override
	public int queryCount(String querySoftwareName, Integer queryStatus,
			Integer queryFlatformId, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3) {
		return devAppInfoMapper.queryCount(querySoftwareName, queryStatus, queryFlatformId, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3);
	}

	@Override
	public AppInfo queryInfoByIdAndAPK(Integer id, String APKName) {
		return devAppInfoMapper.queryInfoByIdAndAPK(id, APKName);
	}

	@Override
	public int insertAppInfo(AppInfo info) {
		// TODO Auto-generated method stub
		return devAppInfoMapper.insertAppInfo(info);
	}

	@Override
	public boolean deleteLogPath(Integer id) {
		int result = devAppInfoMapper.deleteLogPath(id);
		if (result > 0) {
			return true;  //表示修改成功
		} else {
			return false; //表示修改失败
		}
	}

	@Override
	public boolean modifyAppInfo(AppInfo appInfo) {
		int result = devAppInfoMapper.modifyAppInfo(appInfo);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

}
