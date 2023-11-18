package com.yiling.search.flow.dto.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @description: GoodsSearchEsRequest <br>
 * @date: 2021/6/10 10:16 <br>
 * @author: fei.wu <br>
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EsFlowGoodsBatchDetailSearchRequest extends QueryPageListRequest implements Serializable {

    private static final long serialVersionUID = 291479953511743718L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业ID列表 eids和provinceCodes或者关系
     */
    private List<Long> eids;

    /**
     * 企业ID列表 eids和provinceCodes或者关系
     */
    private List<String> provinceCodes;

    /**
     * 经销商级别
     */
    private Integer supplierLevel;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    private Long crmEnterpriseId;

    /**
     * 原始商品名称
     */
    private String goodsName;
    /**
     * 原始商品规格
     */
    private String poSpecifications;

    private Integer delFlag;
}
