package com.yiling.sjms.gb.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;


/**
 * 保存费用申请信息附件
 */
@Data
public class SaveFeeApplicationInfoRequest extends BaseRequest {
    /**
     * 文件类型：1-团购证据 2-费用申请提交
     */
    private Integer fileType;

    /**
     * 文件KEY
     */
    private String fileKey;

    /**
     * 文件名称
     */
    private String fileName;

}
