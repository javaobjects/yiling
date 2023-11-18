package com.yiling.dataflow.order.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowShopSaleListPageRequest extends QueryPageListRequest{

    /**
     * 总店企业id列表
     */
    private List<Long> eidList;

    /**
     * 门店企业id列表
     */
    private List<Long> shopEidList;

    /**
     * 总店商业代码（商家eid）
     */
    private Long eid;

    /**
     * 门店商业代码（商家eid）
     */
    private Long shopEid;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 商品批准文号
     */
    private String license;

    /**
     * 销售时间-开始
     */
    private Date startTime;

    /**
     * 销售时间-结束
     */
    private Date endTime;

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
     * 企业标签id列表
     */
    private List<Long> enterpriseTagIdList;

    /**
     * 企业标签，多选
     */
    private String enterpriseTagId;

    /**
     * 商品规格ID
     */
    private Long specificationId;

    /**
     * 主键id列表
     */
    private List<Long> idList;

    /**
     * 有无标准库规格关系：0-无, 1-有
     */
    private Integer specificationIdFlag;

    /**
     * 月份
     */
    private String month;

    /**
     * 查询时间类型： 0-6个月以内 1-6个月以前
     */
    private Integer timeType;

    /**
     * 查询年份
     */
    private Integer selectYear;

    /**
     * 是否逻辑删除，0-未删除 1-已删除 2-全部
     */
    private Integer deleteFlag;

}
