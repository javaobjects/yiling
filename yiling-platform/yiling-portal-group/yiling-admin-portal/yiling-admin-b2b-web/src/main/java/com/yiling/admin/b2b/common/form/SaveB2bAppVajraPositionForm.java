package com.yiling.admin.b2b.common.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 新增修改金刚位信息
 *
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveB2bAppVajraPositionForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "企业ID")
    private String eid;

    @ApiModelProperty(value = "金刚位标题名称")
    @NotBlank(message = "请输入金刚位标题名称")
    private String title;

    @ApiModelProperty(value = "金刚位图片地址")
    @NotBlank(message = "请提交金刚位图片")
    private String pic;

    @ApiModelProperty(value = "排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序")
    @NotNull(message = "请输入权重")
    @Range(message = "权重范围为 {min} 到 {max} 之间", min = 1, max = 200)
    private Integer sort;

    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer vajraStatus;

    @ApiModelProperty(value = "页面配置1-活动详情H5 3-搜索结果页 4-商品页 5-店铺页 6-领券中心 7-活动中心 8-会员中心")
    private Integer linkType;

    @ApiModelProperty(value = "活动详情超链接")
    private String activityLinks;

    @ApiModelProperty(value = "搜索结果页关键词")
    private String searchKeywords;

    @ApiModelProperty(value = "商品页商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "店铺页跟供应商ID")
    private Long sellerEid;

    @ApiModelProperty(value = "备注")
    private String remark;
}
