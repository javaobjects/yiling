package com.yiling.user.system.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 注销账号校验返回信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserDeregisterAssistantValidDTO extends BaseDTO {

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 是否有企业：0-否 1-是
     */
    private Integer type;

    /**
     * 不通过原因集合
     */
    private List<String> rejectReasonList;


}
