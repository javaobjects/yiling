package com.yiling.export.export.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.yiling.export.export.dto.QueryExportDataDTO;

import cn.afterturn.easypoi.handler.inter.IExcelExportServer;

/**
 * searchData接口
 * 
 * @author jian.mei
 * @date 2020-05-05
 */
public interface  BaseExportQueryDataService<T> extends IExcelExportServer {

	/**
	 * 查询excel中的数据(通过getFileSuffix支持 xlsx和xls两种格式导出，默认xlsx)
	 * 
	 * @param t
	 * @return
	 */
	QueryExportDataDTO queryData(T t);

	T getParam(Map<String, Object> map);

    /**
     * 获取导出文件后缀，默认为xlsx
     * @return
     */
	default String getFileSuffix(){
	    return "xlsx";
    }

    /**
     * 是否返回数据
     * @return
     */
    default boolean isReturnData(){
        return true;
    }

    /**
     * 获取下载文件字节流（不返回数据时使用）
     * @return
     */
    default byte[] getExportByte(T t, String fileName){
        return null;
    }

    @Override
    default List<Object> selectListForExcelExport(Object queryParams, int page) {

        return Collections.emptyList();
    }
}
