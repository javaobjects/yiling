package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * pei
 * @author: gxl
 * @date: 2022/10/18
 */
@Data
@Accessors(chain = true)
public class TaskGoodsMatchDTO implements java.io.Serializable  {
    private static final long serialVersionUID = 8920852310991782333L;
    private Long goodsId;

    private Long sellSpecificationsId;
}