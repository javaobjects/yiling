package com.yiling.goods.medicine.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *  页面发货以后需要重新刷新库存
 *
 * @author: shuang.zhang
 * @date: 2022/1/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsRefreshDTO extends BaseDTO {

    private List<String> inSnList;

    private Long eid;
}
