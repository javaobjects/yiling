package com.yiling.user.enterprise.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 授信主体企业DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/08/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MainPartEnterpriseDTO extends BaseDTO {

    @ApiModelProperty("是否显示授信主体：0.否，1.是。只有以岭和工业直属才显示和选择授信主体")
    private Integer showMainPart;

    @ApiModelProperty("选择的授信主体列表")
    private List<EnterpriseDTO> enterpriseList;


}
