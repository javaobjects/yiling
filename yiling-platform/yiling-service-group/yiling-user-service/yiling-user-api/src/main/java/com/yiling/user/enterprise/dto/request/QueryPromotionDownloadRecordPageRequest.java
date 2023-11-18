package com.yiling.user.enterprise.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询企业推广下载记录分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-01
 */
@Data
@Accessors(chain = true)
public class QueryPromotionDownloadRecordPageRequest extends QueryPageListRequest {

    /**
     * 推广方ID
     */
    private Long promoterId;

    /**
     * 推广方名称
     */
    private String promoterName;

    /**
     * 开始下载时间
     */
    private Date startDownloadTime;

    /**
     * 结束下载时间
     */
    private Date endDownloadTime;

    /**
     * 推广方省份编码
     */
    private String provinceCode;

    /**
     * 推广方城市编码
     */
    private String cityCode;

    /**
     * 推广方区域编码
     */
    private String regionCode;

}
