package com.yiling.b2b.app.common.form;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-开屏位 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenPositionVO extends BaseVO {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 配置链接
     */
    @ApiModelProperty("配置链接")
    private String link;

    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String picture;

}
