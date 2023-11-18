package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: ray
 * @date: 2021/9/26
 */
@Data
public class UpdateUserTaskByCostumerRequest implements Serializable {

    private static final long serialVersionUID = 6484030884395220551L;

    private Long customerEid;

    private Long userId;
}