package com.yiling.admin.data.center.standard.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsDosageVO extends BaseVO {

    private static final long serialVersionUID = -3337121788331608L;

    /**
     * 剂型名称
     */
    @ApiModelProperty(value = "剂型名称")
    private String gdfName;

}
