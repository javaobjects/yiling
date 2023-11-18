package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGoodsPicRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * id
     */
    private Long   id;
    /**
     * 商品Id
     */
    private Long   goodsId;
    /**
     * 商品图片值
     */
    private String pic;

    /**
     * 图片排序
     */
    private Integer picOrder;

    /**
     * 是否商品默认图片（0否1是）
     */
    private Integer picDefault;

}
