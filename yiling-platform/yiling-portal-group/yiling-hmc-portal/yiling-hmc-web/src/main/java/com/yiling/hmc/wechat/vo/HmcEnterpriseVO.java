package com.yiling.hmc.wechat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 开通药+险兑付的店铺
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/4/25
 */
@Data
@Accessors(chain = true)
@Builder
@ApiModel("开通药+险兑付的店铺")
@AllArgsConstructor
@NoArgsConstructor
public class HmcEnterpriseVO {

    /**
     * 药店名称
     */
    @ApiModelProperty("药店名称")
    private String name;

    /**
     * 药店地址
     */
    @ApiModelProperty("药店地址")
    private String address;


}
