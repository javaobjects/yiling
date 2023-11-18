package com.yiling.admin.data.center.enterprise.form;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询企业推广下载记录分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-01
 */
@Data
@Accessors(chain = true)
public class QueryPromotionDownloadRecordPageForm extends QueryPageListForm {

    /**
     * 推广方ID
     */
    @ApiModelProperty("推广方ID")
    private Long promoterId;

    /**
     * 推广方名称
     */
    @Length(max = 50)
    @ApiModelProperty("推广方名称")
    private String promoterName;

    /**
     * 开始下载时间
     */
    @ApiModelProperty("开始下载时间")
    private Date startDownloadTime;

    /**
     * 结束下载时间
     */
    @ApiModelProperty("结束下载时间")
    private Date endDownloadTime;

    /**
     * 推广方省份编码
     */
    @ApiModelProperty("推广方省份编码")
    private String provinceCode;

    /**
     * 推广方城市编码
     */
    @ApiModelProperty("推广方城市编码")
    private String cityCode;

    /**
     * 推广方区域编码
     */
    @ApiModelProperty("推广方区域编码")
    private String regionCode;

}
