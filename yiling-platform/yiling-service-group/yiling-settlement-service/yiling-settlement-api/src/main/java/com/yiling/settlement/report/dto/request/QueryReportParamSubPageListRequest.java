package com.yiling.settlement.report.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022/2/22
 */
@Data
@Accessors(chain = true)
public class QueryReportParamSubPageListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -6848275182808028635L;

    /**
     * 参数id
     */
    private Long paramId;

    /**
     * 参数类型：1-商品类型 2-促销活动 3-阶梯规则 4-会员返利
     */
    private Integer parType;

    /**
     * 商家eid
     */
    private List<Long> eidList;

    /**
     * 门槛金额
     */
    private BigDecimal thresholdAmount;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    private Integer memberSource;

    /**
     * 会员参数的会员id
     */
    private Long memberId;

    /**
     * 操作人
     */
    private List<Long> updateUser;

    /**
     * 开始修改时间
     */
    private Date startUpdateTime;

    /**
     * 结束修改时间
     */
    private Date endUpdateTime;


}