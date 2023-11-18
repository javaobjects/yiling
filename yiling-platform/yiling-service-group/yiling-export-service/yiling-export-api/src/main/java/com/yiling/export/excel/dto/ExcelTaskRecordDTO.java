package com.yiling.export.excel.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/11/17
 */
@Data
public class ExcelTaskRecordDTO extends BaseDTO {

    /**
     * 任务配置表ID
     */
    private Long taskConfigId;

    /**
     * 企业eid
     */
    private Long eid;

    /**
     * 所属组名
     */
    private String title;

    /**
     * 导入原文件oss地址
     */
    private String requestUrl;

    /**
     * 导入以后结果oss地址
     */
    private String resultUrl;

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
     * 菜单地址
     */
    private String menuName;

    /**
     * 导出状态状态：1-正在导入2-导入成功 3-导入失败
     */
    private Integer status;

    /**
     * 导入文件的名称
     */
    private String fileName;

    /**
     * 操作平台：:f2b商城;b2b商城;admin后台;
     */
    private String source;

    /**
     * 操作ip
     */
    private String exportIp;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 完成条数
     */
    private Integer successNumber;

    /**
     * 失败条数
     */
    private Integer failNumber;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 参数
     */
    private String param;
}
