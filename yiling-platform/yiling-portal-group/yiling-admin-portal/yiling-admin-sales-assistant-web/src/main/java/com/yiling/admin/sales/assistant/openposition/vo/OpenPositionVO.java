package com.yiling.admin.sales.assistant.openposition.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-开屏位表 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenPositionVO extends BaseVO {

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String no;

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
    @ApiModelProperty("图片Key")
    private String picture;

    /**
     * 状态：1-未发布 2-已发布
     */
    @ApiModelProperty("状态：1-未发布 2-已发布")
    private Integer status;

    /**
     * 图片url地址
     */
    @ApiModelProperty("图片url地址")
    private String pictureUrl;


}
