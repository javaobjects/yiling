package com.yiling.user.integral.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存签到周期 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSignPeriodRequest extends BaseRequest {

    /**
     * 发放规则ID
     */
    @NotNull
    private Long giveRuleId;

    /**
     * 签到周期
     */
    @NotNull
    @Range(min = 5, max = 31)
    private Integer signPeriod;

    /**
     * 积分周期配置集合
     */
    @NotEmpty
    private List<SaveIntegralPeriodConfigRequest> integralPeriodConfigList;


}
