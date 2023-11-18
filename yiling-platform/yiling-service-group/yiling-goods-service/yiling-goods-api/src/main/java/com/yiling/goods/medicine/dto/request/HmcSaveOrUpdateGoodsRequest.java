package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 HmcSaveOrUpdateGoodsRequest
 * @描述
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HmcSaveOrUpdateGoodsRequest  extends BaseRequest {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 供应商ID
     */
    private Long eid;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 供应商名称
     */
    private String ename;

    /**
     * 商品工业品还是商业品
     */
    private Integer enterpriseType;
}
