package com.yiling.user.enterprise.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存企业推广下载记录 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-02
 */
@Data
@Accessors(chain = true)
public class SavePromotionDownloadRecordRequest extends BaseRequest {

    /**
     * 推广方ID
     */
    private Long promoterId;

}
