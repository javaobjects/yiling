package com.yiling.sjms.wash.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LocationUnlockCustomerVO extends BaseVO {

    @ApiModelProperty(value = "选中的编码列表")
    private List<String> checkCodeList;

    @ApiModelProperty(value = "区域树信息列表")
    private List<LocationUnlockCustomerTreeVO> treeInfoList;

}
