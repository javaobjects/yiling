package com.yiling.sjms.manor.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据id删除
 * @author: gxl
 * @date: 2023/2/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteManorChangeFormRequest extends BaseRequest {


    private static final long serialVersionUID = -2758527462318824373L;
    private Long id;
}