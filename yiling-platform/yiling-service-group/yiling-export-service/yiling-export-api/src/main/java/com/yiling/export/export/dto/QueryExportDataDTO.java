package com.yiling.export.export.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QueryExportDataDTO implements Serializable {

	private static final long serialVersionUID = 3386547482216174324L;

	/**
     * 数据查询状态：1查询成功；2数据量超标；
     */
	private Integer status = 1;

	/**
	结果描述
	*/
	private String message;

    /**
     * excel数据
     */
	private List<ExportDataDTO> sheets;
	/**
	 * 是否启用模版导出数据
	 */
	private  boolean useExcelTemplete=false;
	/**
	 * 模版路径
	 */
	private String excelTempletePath;
}