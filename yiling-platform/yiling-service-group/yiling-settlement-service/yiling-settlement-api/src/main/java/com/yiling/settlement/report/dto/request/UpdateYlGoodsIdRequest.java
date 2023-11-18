package com.yiling.settlement.report.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateYlGoodsIdRequest extends BaseRequest {

    /**
     * 商家eid
     */
    private Long eid;

    /**
     * 老的ylGoodsId
     */
    private Long oldId;

    /**
     * 新的的ylGoodsId
     */
    private Long newId;

}
