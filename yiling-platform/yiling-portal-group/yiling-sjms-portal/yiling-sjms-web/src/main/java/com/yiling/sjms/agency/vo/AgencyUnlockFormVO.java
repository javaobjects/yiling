package com.yiling.sjms.agency.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 机构新增修改表单
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-22
 */
@Data
public class AgencyUnlockFormVO extends BaseVO {

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * crm系统对应客户名称
     */
    @ApiModelProperty("机构名称")
    private String name;


    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    @ApiModelProperty("erp供应链角色：1-经销商 2-终端医院 3-终端药店")
    private Integer supplyChainRole;


    /**
     * 解锁三者关系数量
     */
    @ApiModelProperty("解锁三者关系数量")
    private Integer unlockNum;

    /**
     * 备注：1-潜力低、2-商业 (终端) 关停/重组、3-商业 (终端) 所属划归、4-供应商停控、5-渠道不畅、6-公司批复锁定/解锁、7-返利点位高、8-位置偏远、9-终端改制/统购分销、10-终端改制/转公共卫生、11-终端改制/专科医院
     */
    @ApiModelProperty("备注：1-潜力低、2-商业 (终端) 关停/重组、3-商业 (终端) ")
    private Integer notes;

    /**
     * 修改备注
     */
    private String businessRemark;

    @ApiModelProperty("数据归档：1-开启 2-关闭")
    private Integer archiveStatus;

}
