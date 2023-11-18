package com.yiling.sales.assistant.task.dto.request;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/12/17
 */
@Data
@Accessors(chain = true)
public class AddTaskMemberRequest implements Serializable {
    private static final long serialVersionUID = 2337124402666631441L;
    private Long memberId;

    private Long memberStageId;

    private String playbill;
}