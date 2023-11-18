package com.yiling.hmc.wechat.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 药品福利首页VO
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/28
 */
@Data
@Accessors(chain = true)
@Builder
@ApiModel("药品福利首页VO")
@AllArgsConstructor
@NoArgsConstructor
public class MedicineHomeVO {

    /**
     * 福利药品列表
     */
    @ApiModelProperty("福利药品列表")
    private List<MedicineItemVO> medicineItemList;

    /**
     * 是否展示店铺弹框 true-是，false-否
     */
    @ApiModelProperty("是否展示店铺弹框 true-是，false-否")
    private Boolean showShopDialog;


}
