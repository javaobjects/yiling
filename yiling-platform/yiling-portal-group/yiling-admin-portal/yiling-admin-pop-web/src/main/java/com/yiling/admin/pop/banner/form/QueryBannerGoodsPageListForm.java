package com.yiling.admin.pop.banner.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询banner商品分页列表 Form
 *
 * @author: yuecheng.yue
 * @date: 2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBannerGoodsPageListForm extends QueryPageListForm {

    /**
     * bannerID
     */
    @ApiModelProperty(value = "bannerID")
    private Long bannerId;

}