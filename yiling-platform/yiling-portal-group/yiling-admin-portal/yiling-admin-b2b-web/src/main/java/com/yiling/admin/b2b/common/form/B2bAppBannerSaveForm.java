package com.yiling.admin.b2b.common.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import cn.hutool.core.date.DateUtil;
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
public class B2bAppBannerSaveForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "企业ID")
    private String eid;

    @ApiModelProperty(value = "banner标题", required = true)
    @NotBlank(message = "请输入标题名称")
    private String title;

    @ApiModelProperty(value = "banner图片地址", required = true)
    @NotBlank(message = "请提交图片")
    private String pic;

    @ApiModelProperty(value = "使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner 3-B2B移动端会员中心Banner 4-B2B移动端店铺Banner", required = true)
    @NotNull(message = "请选择使用位置")
    private Integer usageScenario;

    @ApiModelProperty(value = "排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序", required = true)
    @NotNull(message = "请输入权重")
    @Range(message = "权重范围为 {min} 到 {max} 之间", min = 1, max = 200)
    private Integer sort;

    @ApiModelProperty(value = "显示状态：1-启用 2-停用", required = true)
    private Integer bannerStatus;

    @ApiModelProperty(value = "有效起始时间", required = true)
    private Date startTime;

    @ApiModelProperty(value = "有效结束时间", required = true)
    private Date stopTime;

    @ApiModelProperty(value = "页面配置1-活动详情H5 3-搜索结果页 4-商品页 5-旗舰店页 6-会员中心")
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

    @ApiModelProperty(value = "备注")
    private String remark;

    public Date getStartTime() {
        return DateUtil.beginOfDay(startTime);
    }

    public Date getStopTime() {
        String stop = DateUtil.format(stopTime, "yyyy-MM-dd 23:59:59");
        return DateUtil.parse(stop);
    }

    @ApiModelProperty("B2B移动端店铺Banner对应的企业")
    private List<B2bAppBannerEnterpriseSaveForm> bannerEnterpriseList;
}
