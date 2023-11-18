package com.yiling.sjms.flee.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: xinxuan.jia
 * @date: 2023/6/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveSaleAppealUploadDataFormRequest extends BaseRequest {

    /**
     * 销量申诉表单id集合
     */
    private List<Long> ids;
}
