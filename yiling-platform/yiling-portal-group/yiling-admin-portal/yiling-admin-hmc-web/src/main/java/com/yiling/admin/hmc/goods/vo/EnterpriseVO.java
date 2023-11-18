package com.yiling.admin.hmc.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业信息表
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseVO extends BaseVO {


    /**
     * 企业名称
     */
    private String name;



    /**
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty(value = "执业许可证号/社会信用统一代码")
    private String licenseNumber;




}
