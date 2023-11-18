package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 新增/修改职位信息 Request
 *
 * @author: xuan.zhou
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SavePositionRequest extends BaseRequest {

    /**
     * 职位ID，新增时为空
     */
    private Long id;

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 职位名称
     */
    @NotEmpty
    @Length(max = 50)
    private String name;

    /**
     * 职位描述
     */
    @Length(max = 200)
    private String description;
}
