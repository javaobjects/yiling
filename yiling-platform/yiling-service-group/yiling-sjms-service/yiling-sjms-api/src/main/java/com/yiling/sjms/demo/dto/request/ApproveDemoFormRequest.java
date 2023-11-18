package com.yiling.sjms.demo.dto.request;

import com.yiling.sjms.form.dto.request.ApproveFormRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xuan.zhou
 * @date: 2023/2/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ApproveDemoFormRequest extends ApproveFormRequest {

    private String name3;
}
