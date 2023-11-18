package com.yiling.settlement.report.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@Data
@Accessors(chain = true)
public class QueryParamSubGoodsPageListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 5693047778898532308L;

    /**
     * 参数id
     */
    private Long paramId;

    /**
     * 子参数id
     */
    private Long paramSubId;

    /**
     * 参数类型：1-商品类型 2-促销活动 3-阶梯规则  4-会员返利
     */
    private Integer parType;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 商家eid
     */
    private List<Long> eidList;

    /**
     * 修改人
     */
    private List<Long> updateUserList;


}