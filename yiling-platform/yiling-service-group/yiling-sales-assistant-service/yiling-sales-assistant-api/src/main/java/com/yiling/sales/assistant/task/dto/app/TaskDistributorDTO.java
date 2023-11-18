package com.yiling.sales.assistant.task.dto.app;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 配送商
 * @author: ray
 * @date: 2021/10/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TaskDistributorDTO extends BaseDTO {
    private static final long serialVersionUID = -8430029947505372682L;
    private String name;

}