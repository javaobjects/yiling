package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 添加HMC端内容
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddHmcContentRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 内容列表
     */
    private List<HmcContentRequest> list;

}
