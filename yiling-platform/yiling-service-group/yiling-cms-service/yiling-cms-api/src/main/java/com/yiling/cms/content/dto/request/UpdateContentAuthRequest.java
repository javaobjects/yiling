package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 更新IHDoc文章权限
 *
 * @author: fan.shen
 * @date: 2023-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateContentAuthRequest extends BaseRequest {

    private static final long serialVersionUID = 8387299024010863751L;

    /**
     * id
     */
    private Long id;

    /**
     * 内容权限 1-仅登录，2-需认证
     */
    private Integer contentAuth;

}