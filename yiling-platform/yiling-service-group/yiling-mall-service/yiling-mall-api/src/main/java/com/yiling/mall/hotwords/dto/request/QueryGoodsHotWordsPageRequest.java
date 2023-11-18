package com.yiling.mall.hotwords.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsHotWordsPageRequest extends QueryPageListRequest {

    /**
     * 热词名称
     * 热词名称
     */
    private String name;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer state;

    /**
     * 投放开始时间
     */
    private Date startTime;

    /**
     * 投放结束时间
     */
    private Date endTime;

    /**
     * 开始创建时间
     */
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    private Date endCreateTime;

    /**
     * 企业id
     */
    private Long eid;
}
