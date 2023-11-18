package com.yiling.admin.hmc.enterprise.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
public class GoodsListVO extends BaseVO {

    @ApiModelProperty("商家id")
    private Long eid;

    @ApiModelProperty("商家名称")
    private String ename;

    @ApiModelProperty("C端保险药品商家提成设置信息")
    private List<GoodsVO> enterpriseCommissionList;
}
