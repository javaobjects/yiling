package com.yiling.b2b.admin.goods.vo;

import java.util.List;

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
public class CustomerTypeControlVO extends BaseVO {

    private Integer setType;

    private List<Long> customerTypes;
}
