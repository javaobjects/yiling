package com.yiling.goods.medicine.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomerPriceLimitDTO extends BaseDTO {

    private static final long serialVersionUID = -4672345329942342009L;

    private Long eid;

    private Long customerEid;

    private Integer recommendationFlag;

    private Integer limitFlag;

    private Long updateUser;

    private Date updateTime;
}
