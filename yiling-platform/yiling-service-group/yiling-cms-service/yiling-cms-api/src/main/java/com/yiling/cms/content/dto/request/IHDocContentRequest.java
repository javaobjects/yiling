package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加IHDoc端内容
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@Accessors(chain = true)
public class IHDocContentRequest extends BaseRequest{

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * 业务线id
     */
    private Long lineId;

    /**
     * 业务线名称
     */
    private String lineName;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 栏目名称
     */
    private String categoryName;

    /**
     * 栏目id
     */
    private Long categoryId;

    /**
     * 内容权限 1-仅登录，2-需认证
     */
    private Integer contentAuth;

}
