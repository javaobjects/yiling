package com.yiling.dataflow.gb.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/14 0014
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGbOrderPageRequest extends QueryPageListRequest {

    /**
     * 是否仅查询主单信息：0-否 1-是
     */
    private Integer mainOrderFlag;

    /**
     * 排序字段
     */
    private String orderByDescField;

    /**
     * 数据处理:1-未处理；2-已处理
     */
    private Integer execStatus;

    /**
     * 团购表单ID
     */
    private Long formId;

    /**
     * 团购编号
     */
    private String gbNo;

    /**
     * 出库商业ID
     */
    private Long crmId;

    /**
     * 出库终端名称
     */
    private String enterpriseName;

    /**
     * 复核状态：1-未复核 2-已复核，字典：gb_form_review_status
     */
    private Integer checkStatus;

    /**
     * 提交时间-开始
     */
    private Date createTimeStart;

    /**
     * 提交时间-结束
     */
    private Date createTimeEnd;

}
