package com.yiling.hmc.content.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 内容
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "goodsContentDetailVO")
public class ContentDetailVO extends BaseVO {



    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题")
    private String subtitle;


    /**
     * 点击量
     */
    @ApiModelProperty(value = "点击量")
    private Integer pageView;



    /**
     * 发布时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private String source;

    /**
     * 作者
     */
    @ApiModelProperty(value = "作者")
    private String author;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;


    /**
     * 视频oss key
     */
    @ApiModelProperty(value = "视频地址")
    private String vedioFileUrl;

    @ApiModelProperty(value = "主讲人")
    private String speaker;

    /**
     * 封面
     */
    @ApiModelProperty(value = "封面")
    private String cover;
}
