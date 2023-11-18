package com.yiling.dataflow.gb.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGbAppealFormRequest extends BaseRequest {

    /**
     * 团购表单ID列表
     */
    private List<Long> gbOrderIdList;

    /**
     * 处理类型：1自动2人工
     */
    private Integer execType;

}
