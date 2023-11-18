package com.yiling.user.system.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 查询医药代表分页列表 Request
 *
 * @author xuan.zhou
 * @date 2022/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
public class QueryMrPageListRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    @NotNull
    @Min(1)
    private Long eid;

    /**
     * 姓名（模糊查询）
     */
    private String name;

    /**
     * 手机号（模糊查询）
     */
    private String mobile;

    /**
     * 工号（模糊查询）
     */
    private String code;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    private Integer status;

}
