package cn.appsys.dao.dev.datadictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryMapper {

	/**
	 * 通过类型编码查询数据字典的相关信息
	 * @param typeCode 类型编码
	 * @return 数据字典的相关信息
	 */
	List<DataDictionary> queryByTypeCode(@Param("typeCode")String typeCode);
}