package com.yiling.b2b.app.common.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bAppVajraPositionVO extends BaseVO {

    @ApiModelProperty("企业ID")
    private String eid;

    @ApiModelProperty("金刚位标题名称")
    private String title;

    @ApiModelProperty("金刚位图片地址")
    private String pic;

    @ApiModelProperty("来源1-POP 2-销售助手 3-B2B")
    private Integer source;

    @ApiModelProperty("排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序")
    private Integer sort;

    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer vajraStatus;

    @ApiModelProperty("页面配置1-活动详情H5 2-全部分类 3-搜索结果页 4-商品页 5-店铺页 6-领券中心 7-活动中心 8-会员中心")
    private Integer linkType;

    @ApiModelProperty("活动详情超链接")
    private String activityLinks;

    @ApiModelProperty("搜索结果页关键词")
    private String searchKeywords;

    @ApiModelProperty("商品页商品ID")
    private Long goodsId;

    @ApiModelProperty("店铺页跟供应商ID")
    private Long sellerEid;

    @ApiModelProperty("分类页一级分类")
    private Long primaryCategory;

    @ApiModelProperty("分类页一级分类名称")
    private String primaryCategoryName;

    @ApiModelProperty("分类页二级分类")
    private Long secondaryCategory;

    @ApiModelProperty("分类页二级分类名称")
    private String secondaryCategoryName;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;
}
