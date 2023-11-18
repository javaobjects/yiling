package com.yiling.open.cms.content.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/11/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIHPatientContentPageVO extends BaseVO {


    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;
    /**
     * 封面
     */
    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("栏目信息")
    private List<IHPatientContentCategoryVO> categoryList;

    @ApiModelProperty("内容排序")
    private String categoryRank;

    /**
     * 点击量
     */
    @ApiModelProperty("栏目点击量")
    private Integer clickNum;

    /**
     * 浏览量
     */
    @ApiModelProperty("浏览量")
    private Integer view;

    /**
     * 点赞量
     */
    @ApiModelProperty("点赞量")
    private Integer likeCount;

    /**
     * 状态 1未发布 2已发布
     */
    @ApiModelProperty("状态 1未发布 2已发布")
    private Integer status;

    /**
     * 医生id
     */
    @ApiModelProperty("医生id")
    private Long doctorId;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date creatTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;
}
