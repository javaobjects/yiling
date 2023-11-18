package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业推广下载记录表导出 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterprisePromotionDownloadRecordExportBO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 推广方ID
     */
    private Long promoterId;

    /**
     * 推广方名称
     */
    private String promoterName;

    /**
     * 下载时间
     */
    private Date downloadTime;

    /**
     * 下载时间
     */
    private String downloadTimeStr;

    /**
     * 推广方省份名称
     */
    private String provinceName;

    /**
     * 推广方城市名称
     */
    private String cityName;

    /**
     * 推广方区域名称
     */
    private String regionName;

    /**
     * 推广方地址
     */
    private String promoterAddress;

}
