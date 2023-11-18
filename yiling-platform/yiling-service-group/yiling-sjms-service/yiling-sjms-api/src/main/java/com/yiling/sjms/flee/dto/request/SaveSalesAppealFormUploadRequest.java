package com.yiling.sjms.flee.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSalesAppealFormUploadRequest extends BaseRequest {

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件地址
     */
    private String key;

    /**
     * 表单类型 1销量申诉表，2销量申诉确认表
     */
    private Integer type;

    /**
     * 调整月流向对应的调整事项 1漏做客户关系对照 2未备案商业销售到锁定终端3医院分院以总院名头进货4 医院的院内外药店进货5医联体、医共体共用进货名头6互联网医院无法体现医院名字7药店子公司以总部名头进货
     */
    private Integer monthAppealType;

    private Integer appealType;

    /**
     * 申诉金额
     */
    private BigDecimal appealAmount;


    /**
     * 补传流向的月份
     */
    private Date appealMonth;


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
     * 目标excel地址
     */
    private String targetUrl;

    /**
     * 发起人姓名
     */
    private String empName;

    /**
     * 申诉描述
     */
    private String appealDescribe;

    /**
     * 附件
     */
    private String appendix;
}
