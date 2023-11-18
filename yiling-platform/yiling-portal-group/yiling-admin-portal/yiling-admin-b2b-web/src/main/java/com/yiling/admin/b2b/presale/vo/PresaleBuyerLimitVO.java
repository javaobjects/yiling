package com.yiling.admin.b2b.presale.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 策略满赠客户
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleBuyerLimitVO extends BaseVO {

    @ApiModelProperty("营销活动id")
    private Long marketingPresaleId;

    @ApiModelProperty("企业ID")
    private Long eid;

    @ApiModelProperty("企业名称")
    private String ename;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;
    
    @ApiModelProperty("备注")
    private String remark;


}
