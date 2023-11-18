package com.yiling.user.shop.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * B2B-店铺列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@ApiModel
public class QueryShopRequest extends QueryPageListRequest {

    /**
     * 客户
     */
    private Long customerEid;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺的企业ID集合
     */
    private List<Long> shopEidList;

    /**
     * 近一个月开始时间
     */
    private Date startRecentTime;

    /**
     * 近一个月结束时间
     */
    private Date endRecentTime;

    /**
     * 当前登录终端的省区
     */
    private String provinceCode;

    /**
     * 优质商家集合
     */
    private List<Long> highQualityEidList;

    /**
     * 建采商家集合
     */
    private List<Long> purchaseEidList;

}
