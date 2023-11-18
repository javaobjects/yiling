package com.yiling.user.integral.dto.request;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存积分规则基本信息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralRuleBasicRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 规则名称
     */
    @NotEmpty
    @Length(max = 20)
    private String name;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    private Integer platform;

    /**
     * 规则生效时间
     */
    @NotNull
    private Date startTime;

    /**
     * 规则失效时间
     */
    @NotNull
    private Date endTime;

    /**
     * 规则说明
     */
    @Length(max = 50)
    private String description;

    /**
     * 行为ID
     */
    @NotNull
    private Long behaviorId;

}
