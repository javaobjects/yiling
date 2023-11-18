package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/5/26
 */
@Data
public class UpdateFlowGoodsRelationRequest extends BaseRequest {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 以岭商品ID
     */
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    private String ylGoodsSpecifications;

    /**
     * 商品关系标签：0-无标签 1-以岭品 2-非以岭品 3-中药饮片
     */
    private Integer goodsRelationLabel;

    /**
     * ylGoodsId 可修改置空
     *
     * @param ylGoodsId
     */
    public void setYlGoodsId(Long ylGoodsId) {
        if(ObjectUtil.isNull(ylGoodsId)){
            ylGoodsId = 0L;
        }
        this.ylGoodsId = ylGoodsId;
    }
}
