package com.yiling.bi.resource.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/12/30
 */
@Data
public class UploadResourceDTO extends BaseDTO {

    private String dataId;

    private byte[] fileStream;
}
