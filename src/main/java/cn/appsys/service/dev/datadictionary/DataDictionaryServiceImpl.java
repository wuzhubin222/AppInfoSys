package cn.appsys.service.dev.datadictionary;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.dev.datadictionary.DataDictionaryMapper;
import cn.appsys.pojo.DataDictionary;

@Service("dataDictionaryService")
public class DataDictionaryServiceImpl implements DataDictionaryService {

	@Resource
	private DataDictionaryMapper dataDictionaryMapper;
	
	@Override
	public List<DataDictionary> queryByTypeCode(String typeCode) {
		return dataDictionaryMapper.queryByTypeCode(typeCode);
	}

}
