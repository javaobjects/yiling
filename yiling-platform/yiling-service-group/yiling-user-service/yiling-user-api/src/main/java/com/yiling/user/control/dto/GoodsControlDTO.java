package com.yiling.user.control.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsControlDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private Integer setType;

    private Integer controlType;

    private List<Long> conditionValue;

    private String controlDescribe;

}
