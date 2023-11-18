package com.yiling.dataflow.backup.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/12/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DataFlowBackupRequest extends BaseRequest {

    /**
     * 企业id列表
     */
    private List<Long> eidList;
}
