package com.yiling.hmc.remind.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 历史记录
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/6/20
 */
@Data
@ToString
@ApiModel(value = "MedsRemindHistoryVO", description = "历史记录")
@Slf4j
public class MedsRemindHistoryVO extends BaseVO {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * 售卖规格id
     */
    @ApiModelProperty(value = "售卖规格id")
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

}
