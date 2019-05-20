package cn.appsys.service.dev.datadictionary;

import java.util.List;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryService {
	
	List<DataDictionary> queryByTypeCode(String typeCode);
}
