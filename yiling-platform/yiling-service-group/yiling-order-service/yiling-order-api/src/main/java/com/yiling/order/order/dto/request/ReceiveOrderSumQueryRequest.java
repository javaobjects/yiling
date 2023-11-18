package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/9/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReceiveOrderSumQueryRequest extends BaseRequest {

    private static final long serialVersionUID=7708806573918270883L;

    /**
     * 下单人ID或者商务联系人ID
     */
    private List<Long> userIdList;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 开始下单时间
     */
    private Date startCreateTime;

    /**
     * 结束下单时间
     */
    private Date endCreateTime;

    /**
     * 是否按照，下单时间升序排序，如果不传或者false,默认按照下单时间降序排序
     */
    private Boolean isAscByCreateTime = false;
}
