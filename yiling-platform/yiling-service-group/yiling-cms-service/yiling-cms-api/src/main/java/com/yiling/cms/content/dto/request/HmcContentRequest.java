package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加HMC端内容
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@Accessors(chain = true)
public class HmcContentRequest implements java.io.Serializable{

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 内容id
     */
    private Long contentId;

    /**
     * 业务线id
     */
    private Long lineId;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 栏目id
     */
    private Long categoryId;

}
