package com.yiling.settlement.b2b.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ExportSettlementSimpleInfoPageListRequest extends QueryPageListRequest {

    /**
     * eid
     */
    private Long enterpriseId;

    /**
     * eid
     */
    private Long eid;

    /**
     * 结算单号
     */
    private String code;

    /**
     * 开始生成结算单最小时间
     */
    private Date minTime;

    /**
     * 开始生成结算单最大时间
     */
    private Date maxTime;

    /**
     * 供应商名称
     */
    private String entName;

    /**
     * 结算单类型
     */
    private Integer type;

    /**
     * 结算单状态
     */
    private Integer status;
}
