package com.yiling.mall.hotwords.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGoodsHotWordsRequest extends BaseRequest {

    /**
     * 热词id
     */
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 热词名称
     */
    private String name;

    /**
     * 状态 1-启用 2-停用
     */
    private Integer state;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 投放开始时间
     */
    private Date startTime;

    /**
     * 投放结束时间
     */
    private Date endTime;
}
