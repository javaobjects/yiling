package com.yiling.hmc.wechat.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/5/17
 */
@Data
@Accessors(chain = true)
public class GoodsCountBO {
    private Long goodsId;

    private Long count;
}