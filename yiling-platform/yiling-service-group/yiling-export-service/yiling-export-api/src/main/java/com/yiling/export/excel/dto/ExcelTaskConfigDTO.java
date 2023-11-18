package com.yiling.export.excel.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-17
 */
@Data
public class ExcelTaskConfigDTO extends BaseDTO {

    private String excelCode;

    /**
     * 实现BaseExcelImportService接口的类名
     */
    private String className;

    /**
     * 数据验证处理器beanName
     */
    private String verifyHandlerBeanName;

    /**
     * 数据导入处理器beanName
     */
    private String importDataHandlerBeanName;

    /**
     * 数据导入模型class路径
     */
    private String modelClass;

    /**
     * 所属组名
     */
    private String title;

    /**
     * 菜单地址
     */
    private String menuName;

    /**
     * 模板地址
     */
    private String templateUrl;

    /**
     * 限制类型 以分号隔开
     */
    private String limitType;

    /**
     * 大小限制
     */
    private Integer limitSize;

    /**
     * 行数限制
     */
    private Integer limitNumber;

    /**
     * 描述
     */
    private String ExcelDescribe;

    private Long source;

}
