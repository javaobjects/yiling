package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 添加IHDoc端内容
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIHDocContentRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 内容List
     */
    private List<IHDocContentRequest> contentList;

}
