package com.yiling.sjms.flow.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author: gxl
 * @date: 2023/6/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MonthFlowFormDTO extends BaseDTO {


    private static final long serialVersionUID = 2532881378685153846L;
    /**
     * 主流程表单id
     */
    private Long formId;



    /**
     * 文件名称
     */
    private String excelName;

    /**
     * 补传流向的月份
     */
    private Date appealMonth;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 上传人工号
     */
    private String uploader;

    /**
     * 上传人姓名
     */
    private String uploaderName;

    /**
     * 流向数据类型 1库存2采购3销售
     */
    private Integer dataType;

    /**
     * 数据检查状态 1 通过 2未通过 3检查中
     */
    private Integer checkStatus;

    /**
     * 数据检查未通过原因
     */
    private String reason;

    /**
     * 导入状态 1 成功 2失败 3导入中
     */
    private Integer importStatus;

    /**
     * 文件导入行数
     */
    private Integer lineNumber;

    /**
     * 来源excel地址
     */
    private String sourceUrl;

    /**
     * 目标excel地址
     */
    private String targetUrl;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

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


}