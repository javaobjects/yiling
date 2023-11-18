package com.yiling.user.agreementv2.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利商品组 Request （商品返利规则设置方式为：分类设置，才需要关注此表）
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateGoodsGroupRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 协议时段ID
     */
    private Long segmentId;

    /**
     * 商品组分类：1-普通商品组 2-核心商品组
     */
    private Integer groupCategory;

    /**
     * 商品组名称（只有存在商品组的场景时，才需要传入）
     */
    private String groupName;

    /**
     * 商品组类型：1-全商品主任务 2-子商品组 （商品返利规则设置方式为：分类设置，才需要关注此字段）
     */
    private Integer groupType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 协议返利商品组对应的商品集合（只有存在商品组时才需要传入，商品组类型为：1-全商品主任务时不要设置此值，同个协议所有商品互斥存在）
     */
    private List<AddAgreementRebateGoodsRequest> agreementRebateGoodsList;

    /**
     * 协议返利范围集合
     */
    private List<AddAgreementRebateScopeRequest> agreementRebateScopeList;

}
