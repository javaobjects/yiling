package com.yiling.user.member.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-创建权益 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateMemberEquityRequest extends BaseRequest {

    /**
     * 权益名称
     */
    private String name;

    /**
     * 权益类型：1-系统生成 2-自定义
     */
    private Integer type;

    /**
     * 权益图标
     */
    private String icon;

    /**
     * 权益说明
     */
    private String description;

    /**
     * 权益状态：0-关闭 1-开启
     */
    private Integer status;

}
