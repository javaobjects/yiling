package com.yiling.f2b.admin.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPopGoodsPageListForm extends QueryPageListForm {

    /**
     * 商业主体
     */
    @ApiModelProperty(value = "商业主体")
    private Long eid;

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "批准文号", example = "Z109090")
    private String licenseNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", example = "莲花")
    private String goodsName;

    /**
     * 生产厂家(全模糊搜索）
     */
    @ApiModelProperty(value = "生产厂家", example = "以岭")
    private String manufacturer;

    /**
     * 协议ID
     */
    @ApiModelProperty(value = "协议ID")
    private Long agreementId;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    @ApiModelProperty(value = "专利类型 0-全部 1-非专利 2-专利")
    private Integer isPatent;

}
