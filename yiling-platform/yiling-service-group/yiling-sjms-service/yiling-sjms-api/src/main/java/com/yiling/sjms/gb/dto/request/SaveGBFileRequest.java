package com.yiling.sjms.gb.dto.request;

import java.util.List;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * 上传证据
 */
@Data
public class SaveGBFileRequest extends BaseRequest {
    /**
     * 文件key
     */

    private List<FileInfoRequest> fileKeyList;

    /**
     * 表单ID
     */
    private Long gbId;
}
