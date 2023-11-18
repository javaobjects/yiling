package com.yiling.dataflow.gb.dto.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGbAppealFormSaleReportMatchRequest extends BaseRequest {

    /**
     * 团购处理列表ID
     */
    private Long appealFormId;

    /**
     * 原流向ID列表
     */
    private List<Long> flowWashIdList;

    /**
     * 流向匹配条数
     */
    private Long flowMatchNumber;

}
