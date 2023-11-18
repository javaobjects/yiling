package com.yiling.sales.assistant.task.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 任务选择的会员和条件
 * @author: ray
 * @date: 2021/12/23
 */
@Data
@Accessors(chain = true)
public class TaskMemberDTO extends BaseDTO {

    private static final long serialVersionUID = -6481649707362145387L;
    /**
     * 会员名称
     */
    private String name;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 有效时长
     */
    private Integer validTime;

    private String playbill;

    private Long memberId;

    private Long memberStageId;

}