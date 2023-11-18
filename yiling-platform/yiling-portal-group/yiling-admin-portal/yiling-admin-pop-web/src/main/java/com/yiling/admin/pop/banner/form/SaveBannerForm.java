package com.yiling.admin.pop.banner.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.admin.common.form.GoodsForm;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 创建banner form
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveBannerForm extends BaseForm {


    /**
     * banner标题
     */
    @ApiModelProperty(value = "banner标题")
    @NotEmpty
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
     * 链接url
     */
    @ApiModelProperty(value = "链接url")
    private String linkUrl;

    /**
     * banner图片值
     */
    @ApiModelProperty(value = "banner图片值")
    private String pic;

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
     * 商品集合
     */
    @ApiModelProperty(value = "商品集合")
    private List<GoodsForm> goodsList;
}
