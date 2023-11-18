package com.yiling.b2b.app.member.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B移动端-会员省钱计算器VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-10
 */
@Data
@Accessors(chain = true)
@ApiModel("会员省钱计算器VO")
public class MemberFrugalVO {

    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String memberName;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 会员天数
     */
    @ApiModelProperty("会员天数")
    private Integer memberDays;

    /**
     * 节省金额
     */
    @ApiModelProperty("节省金额")
    private BigDecimal frugalAmount;

}
