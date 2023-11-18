package com.yiling.admin.sales.assistant.task.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: ray
 * @date: 2021/12/17
 */
@Data
@ApiModel
public class MemberVO {
    private Long memberId;

    private String name;
}