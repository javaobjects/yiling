package com.yiling.dataflow.wash.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsBatchTransitPageRequest extends QueryPageListRequest {

    /**
     * 所属月份
     */
    private String gbDetailMonth;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 经销商编码列表，查不到数据会抛异常无数据权限
     */
    private List<Long> crmEnterpriseIdList;

    /**
     * 经销商所属省份编码列表
     */
    List<String> crmProvinceCodeList;

    /**
     * 采购渠道机构编码
     */
    private Long supplyCrmEnterpriseId;

    /**
     * crm商品编码
     */
    private Long crmGoodsCode;

    /**
     * crm商品编码列表
     */
    private List<Long> crmGoodsCodeList;

    /**
     * crm商品规格
     */
    private String crmGoodsSpecifications;

    /**
     * 商品批次号
     */
    private String gbBatchNo;

    /**
     * 最后操作时间开始
     */
    private Date opTimeStart;

    /**
     * 最后操作时间结束
     */
    private Date opTimeEnd;

    /**
     * 库存类型：1-在途订单库存 2-终端库存
     */
    private Integer goodsBatchType;

}
