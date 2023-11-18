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
public class EsFlowPurchaseSearchRequest extends QueryPageListRequest implements Serializable {

    private static final long serialVersionUID = 291479953511743718L;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业ID列表
     */
    private List<Long> eids;

    /**
     * 省区编码
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
     * 客户名称
     */
    private String enterpriseName;
    /**
     * 原始商品名称
     */
    private String goodsName;
    /**
     * 原始商品规格
     */
    private String poSpecifications;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    private List<Integer> dataTags;

    /**
     * 删除标识
     */
    private Integer delFlag;
}
