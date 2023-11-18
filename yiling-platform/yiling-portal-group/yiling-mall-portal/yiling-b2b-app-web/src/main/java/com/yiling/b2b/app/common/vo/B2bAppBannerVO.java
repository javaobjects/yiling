package com.yiling.b2b.app.common.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bAppBannerVO extends BaseVO {

    @ApiModelProperty(value = "企业ID")
    private String eid;

    @ApiModelProperty(value = "banner标题")
    private String title;

    @ApiModelProperty(value = "banner图片地址")
    private String pic;

    @ApiModelProperty(value = "来源1-POP 2-销售助手 3-B2B")
    private Integer bannerSource;

    @ApiModelProperty(value = "使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner 3-B2B移动端会员中心Banner")
    private Integer usageScenario;

    @ApiModelProperty(value = "排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序")
    private Integer sort;

    @ApiModelProperty(value = "显示状态：1-启用 2-停用")
    private Integer bannerStatus;

    @ApiModelProperty(value = "有效起始时间")
    private Date startTime;

    @ApiModelProperty(value = "有效结束时间")
    private Date stopTime;

    @ApiModelProperty(value = "页面配置1-活动详情H5 2-全部分类 3-搜索结果页 4-商品页 5-旗舰店页 6-会员中心")
    private Integer linkType;

    @ApiModelProperty(value = "活动详情超链接")
    private String activityLinks;

    @ApiModelProperty(value = "搜索结果页关键词")
    private String searchKeywords;

    @ApiModelProperty(value = "搜索结果页厂家")
    private String goodsManufacturer;

    @ApiModelProperty(value = "商品页商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "旗舰店页的供应商ID")
    private Long sellerEid;

    @ApiModelProperty(value = "分类页一级分类")
    private Long primaryCategory;

    @ApiModelProperty(value = "分类页一级分类名称")
    private String primaryCategoryName;

    @ApiModelProperty(value = "分类页二级分类")
    private Long secondaryCategory;

    @ApiModelProperty(value = "分类页二级分类名称")
    private String secondaryCategoryName;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
