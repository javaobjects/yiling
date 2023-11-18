package com.yiling.f2b.admin.agreementv2.vo;

import java.util.List;

import com.yiling.f2b.admin.agreementv2.form.AddAgreementRebateGoodsForm;
import com.yiling.f2b.admin.agreementv2.form.AddAgreementRebateScopeForm;
import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利商品组 VO （商品返利规则设置方式为：分类设置，才需要关注此表）
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateGoodsGroupVO extends BaseVO {

    /**
     * 商品组名称
     */
    @ApiModelProperty(value = "商品组名称（只有存在商品组的场景时，才存在）")
    private String groupName;

    /**
     * 商品组类型：1-全商品主任务 2-子商品组 （商品返利规则设置方式为：分类设置，才需要关注此字段）
     */
    @ApiModelProperty(value = "商品组类型：1-全商品主任务 2-子商品组 （商品返利规则设置方式为：分类设置，才需要关注此字段）")
    private Integer groupType;

    /**
     * 商品组分类：1-普通商品组 2-核心商品组
     */
    @ApiModelProperty("商品组分类：1-普通商品组 2-核心商品组（商品返利规则设置方式为：分类设置，才需要关注此字段）")
    private Integer groupCategory;

    /**
     * 排序
     */
    @ApiModelProperty("排序（只有存在商品组的场景时，才需要传入）")
    private Integer sort;

    /**
     * 协议返利商品组对应的商品集合
     */
    @ApiModelProperty("协议返利商品组对应的商品集合（只有存在商品组时才需要传入，商品组类型为：1-全商品主任务时不要设置此值，同个协议所有商品互斥存在）")
    private List<AgreementRebateGoodsVO> agreementRebateGoodsList;

    /**
     * 协议返利范围集合
     */
    @ApiModelProperty("协议返利范围集合")
    private List<AgreementRebateScopeVO> agreementRebateScopeList;

}
