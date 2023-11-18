package com.yiling.user.integral.bo;

import java.io.Serializable;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户签到详情 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserSignDetailBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

    /**
     * 发放规则名称
     */
    private String giveRuleName;

    /**
     * 发放积分
     */
    private Integer integralValue;

}
