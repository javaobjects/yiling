package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 查询用户购买数量
 */
@Data
@Accessors(chain = true)
public class BatchQueryUserBuyNumberRequest extends BaseRequest {

    /**
     * 买家eid
     */
    private Long buyerEid;

    /**
     * 商品Id
     */
    private List<Long> goodIds;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

}
