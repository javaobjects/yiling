package com.yiling.sjms.flee.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销量申诉表单
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sales_appeal_form")
public class SalesAppealFormDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 清洗任务id
     */
    private Long taskId;

    /**
     * 表单类型 1销量申诉表，2销量申诉确认表
     */
    private Integer type;

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
     * 流向数据类型 1采购2销售3库存
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
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
