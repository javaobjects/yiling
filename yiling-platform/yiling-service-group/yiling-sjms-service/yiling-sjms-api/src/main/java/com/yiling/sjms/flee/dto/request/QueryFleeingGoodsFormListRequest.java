package com.yiling.sjms.flee.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFleeingGoodsFormListRequest extends BaseRequest {

    /**
     * form表主键
     */
    private Long formId;

    /**
     * 文件类型 1-申报 2-申报确认
     */
    private Integer importFileType;

    /**
     * 数据检查 1-通过 2-未通过 3-警告
     */
    private Integer checkStatus;

    /**
     * 导入状态：1-导入成功 2-导入失败
     */
    private Integer importStatus;
}
