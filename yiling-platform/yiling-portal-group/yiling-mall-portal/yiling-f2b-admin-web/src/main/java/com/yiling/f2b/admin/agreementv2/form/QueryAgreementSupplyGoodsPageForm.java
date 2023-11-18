package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 查询协议商品分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementSupplyGoodsPageForm extends QueryPageListForm {

    /**
     * 协议ID
     */
    @NotNull
    @ApiModelProperty(value = "协议ID", required = true)
    private Long id;

    /**
     * 商品ID
     */
    @Min(1)
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 商品名称
     */
    @Length(max = 50)
    @ApiModelProperty("商品名称")
    private String goodsName;





}
