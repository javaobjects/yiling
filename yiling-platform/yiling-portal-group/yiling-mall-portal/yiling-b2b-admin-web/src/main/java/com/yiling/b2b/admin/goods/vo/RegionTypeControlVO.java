package com.yiling.b2b.admin.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RegionTypeControlVO extends BaseVO {

    private CustomerTypeControlVO customerTypeControlVO;

    private RegionControlVO regionControlVO;
}
