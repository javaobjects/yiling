package com.yiling.ih.pharmacy.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 获取IH商品信息 Request
 *
 * @author: fan.shen
 * @date: 2023/5/6
 */
@Data
@Accessors(chain = true)
public class GetGoodsInfoRequest implements java.io.Serializable {

    /**
     * idList
     */
    private List<Long> idList;

}
