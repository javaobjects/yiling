package com.yiling.user.member.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-权益列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMemberEquityRequest extends QueryPageListRequest {

    /**
     * 权益类型：1-系统生成 2-自定义
     */
    private Integer type;

    /**
     * 权益名称
     */
    private String name;

    /**
     * 权益状态：0-关闭 1-开启
     */
    private Integer status;


}
