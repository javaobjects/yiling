package com.yiling.f2b.admin.enterprise.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 授信主体企业VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/08/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MainPartEnterpriseVO extends BaseVO {

    @ApiModelProperty("是否展示要选择授信主体：0.否，1.是。只有以岭和工业直属才显示和选择授信主体")
    private Integer showMainPart;

    @ApiModelProperty("选择的授信主体列表")
    private List<SimpleEnterpriseVO> enterpriseList;


}
