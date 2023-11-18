package com.yiling.f2b.admin.agreement.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利申请商品表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-08-09
 */
@Data
@Accessors(chain = true)
public class ApplyGoodsVO {


    /**
     * 名称
     */
	@ApiModelProperty(value = "名称")
    private String name;

}
