package com.yiling.sjms.flee.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/16 0016
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateFleeingGoodsFormRequest extends BaseRequest {

    private Long id;

    /**
     * 数据检查 1-通过 2-未通过 3-警告
     */
    private Integer checkStatus;

    /**
     * 导入状态：1-导入成功 2-导入失败
     */
    private Integer importStatus;
    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 失败结果下载地址
     */
    private String resultUrl;

    private String failReason;
}
