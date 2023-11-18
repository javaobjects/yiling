package com.yiling.admin.pop.banner.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * banner分页列表 VO
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BannerVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    /**
     * banner标题
     */
    @ApiModelProperty(value = "banner标题")
    private String title;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 链接类型：1-商品 2：链接
     */
    @ApiModelProperty(value = "链接类型：1-商品 2：链接")
    private Integer linkType;

    /**
     * banner图片值
     */
    @ApiModelProperty(value = "banner图片值")
    private FileInfoVO fileInfo;


    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * banner状态：未开始 进行中 已结束
     */
    @ApiModelProperty(value = "banner状态：1-未开始 2-进行中 3-已结束")
    private Integer bannerStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * link路径
     */
    @ApiModelProperty(value = "link路径")
    private String linkUrl;

    /**
     *图片url
     */
    @ApiModelProperty(value = "图片url")
    private String pic;
}
