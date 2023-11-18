package com.yiling.dataflow.wash.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsBatchSafePageRequest extends QueryPageListRequest {

    /**
     * 机构编码
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
     * crm商品编码
     */
    private Long crmGoodsCode;

    /**
     * crm商品编码列表
     */
    private List<Long> crmGoodsCodeList;

    /**
     * 最后操作时间开始
     */
    private Date opTimeStart;

    /**
     * 最后操作时间结束
     */
    private Date opTimeEnd;

}
