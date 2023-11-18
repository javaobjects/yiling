package com.yiling.open.cms.content.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

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
public class ContentDetailVO extends BaseVO {



    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;


    /**
     * 点击量
     */
    private Integer pageView;



    /**
     * 发布时间
     */
    private Date publishTime;


    /**
     * 来源
     */
    private String source;

    /**
     * 作者
     */
    private String author;

    /**
     * 内容
     */
    private String content;


    /**
     * 视频oss key
     */
    private String vedioFileUrl;

    private String speaker;



    private Date createTime;


    private String cover;

    private Integer viewLimit;

    @ApiModelProperty(value = "收藏状态：1-收藏 2-取消收藏")
    private Integer collectStatus;

    /**
     * 所属医生id
     */
    @ApiModelProperty(value = "所属医生id")
    private Long docId;

    @ApiModelProperty(value = "内容id")
    private Long contentId;

    @ApiModelProperty(value = "问答数量")
    private Integer qaCount;
}
