package com.yiling.dataflow.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsBatchListPageRequest extends QueryPageListRequest {

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品批准文号
     */
    private String license;

    /**
     * 订单来源，多选
     */
    private List<String> sourceList;

    /**
     * 订单来源，多选
     */
    private String source;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 商品生产厂家
     */
    private String manufacturer;

    /**
     * 负责的公司
     */
    private List<Long> eidList;

    /**
     * 企业标签id列表
     */
    private List<Long> enterpriseTagIdList;

    /**
     * 企业标签，多选
     */
    private String enterpriseTagId;

    /**
     * 统计库存总数量状态：0-未统计，1-已统计
     */
    private Integer statisticsStatus;

    /**
     * 商品规格ID
     */
    private Long specificationId;

    /**
     * 有无标准库规格关系：0-无, 1-有
     */
    private Integer specificationIdFlag;

    private Integer goodsCrmCodeFlag;
}
