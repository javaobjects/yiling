package com.yiling.admin.data.center.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业推广下载记录 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterprisePromotionDownloadRecordVO extends BaseVO {

    /**
     * 推广方ID
     */
    @ApiModelProperty("推广方ID")
    private Long promoterId;

    /**
     * 推广方名称
     */
    @ApiModelProperty("推广方名称")
    private String promoterName;

    /**
     * 下载时间
     */
    @ApiModelProperty("下载时间")
    private Date downloadTime;

    /**
     * 推广方省份名称
     */
    @ApiModelProperty("推广方省份名称")
    private String provinceName;

    /**
     * 推广方城市名称
     */
    @ApiModelProperty("推广方城市名称")
    private String cityName;

    /**
     * 推广方区域名称
     */
    @ApiModelProperty("推广方区域名称")
    private String regionName;

}
