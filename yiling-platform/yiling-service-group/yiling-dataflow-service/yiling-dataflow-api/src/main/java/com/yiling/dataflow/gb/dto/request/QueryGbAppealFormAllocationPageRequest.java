package com.yiling.dataflow.gb.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGbAppealFormAllocationPageRequest extends QueryPageListRequest {

    /**
     * 团购申诉申请ID
     */
    private Long appealFormId;

    /**
     * 分配类型:1-扣减 2-增加
     */
    private Integer allocationType;

    /**
     * 所属年月
     */
    private String matchMonth;

    /**
     * 团购编号
     */
    private String gbNo;

    /**
     * 团购月份
     */
    private String gbMonth;

    /**
     * 商业编码
     */
    private Long crmId;

    /**
     * 机构名称
     */
    private String enterpriseName;

    /**
     * 标准产品编码
     */
    private Long goodsCode;

    /**
     * 排序字段名
     */
    private String orderByDescField;

}
