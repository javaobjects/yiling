package com.yiling.goods.medicine.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGoodsLineRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * pop所对应的eid集合
     */
    private List<Long> popEidList;

    private Integer goodsStatus;

    private Integer isPatent;

    /**
     * 下架原因
     */
    private Integer outReason;

    /**
     * 批量开通产品线
     */
    private List<SaveGoodsLineRequest> goodsLineList;

}
