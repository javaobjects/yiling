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
 * 药店VO
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/28
 */
@Data
@Accessors(chain = true)
@Builder
@ApiModel("药店VO")
@AllArgsConstructor
@NoArgsConstructor
public class MedicineShopVO {

    /**
     * 福利药品列表
     */
    @ApiModelProperty("药店列表")
    private List<MedicineShopDetailVO> shopDetailList;


}
