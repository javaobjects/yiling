package com.yiling.sjms.agency.vo;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseVO;
import com.yiling.sjms.agency.dto.AgencyUnLockRelationShipFormDTO;
import com.yiling.sjms.agency.form.SaveRelationShipForm;

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
public class AgencyUnlockDetailVO extends BaseVO {

    /**
     * id
     */
    private Long id;


    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * crm系统对应客户名称
     */
    @ApiModelProperty("选择机构名称")
    private String name;

    /**
     * crm系统对应客户名称
     */
    @ApiModelProperty("选择机构名称")
    private Long crmEnterpriseId;

    /**
     * 社会信用统一代码
     */
    private String licenseNumber;

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    @ApiModelProperty("供应链角色")
    private Integer supplyChainRole;

    /**
     * 药品经营许可证编号
     */
    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    private String institutionPracticeLicense;

    /**
     *所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;
    /**
     *所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String cityName;
    /**
     *所属区区域名称
     */
    @ApiModelProperty("所属区区域名称")
    private String regionName;
    /**
     *详细地址
     */
    @Length(max = 200)
    @ApiModelProperty("详细地址")
    private String address;
    /**
     * 所属省份编码
     */
    @NotBlank
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @NotBlank
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @NotBlank
    @ApiModelProperty("所属区域编码")
    private String regionCode;
    /**
     * 省区
     */
    private String provincialArea;

    /**
     * 电话
     */
    private String phone;

    /**
     * 变更项目 1-机构名称、2-社会统一信用代码、3-电话、4-所属区域、5-地址
     */
    private String changeItem;

    /**
     * 备注：1-潜力低、2-商业 (终端) 关停/重组、3-商业 (终端) 所属划归、4-供应商停控、5-渠道不畅、6-公司批复锁定/解锁、7-返利点位高、8-位置偏远、9-终端改制/统购分销、10-终端改制/转公共卫生、11-终端改制/专科医院
     */
    @ApiModelProperty("备注")
    private Integer notes;
    /**
     * 变更备注
     */
    @ApiModelProperty("变更备注")
    private String businessRemark;

    /**
     * 解锁三者关系数量
     */
    @ApiModelProperty("选中的数量-解锁三者关系数量")
    private Integer unlockNum;

   /* @ApiModelProperty("关联的三者关系")
    private List<CrmEnterpriseRelationShipListVO> relationShip;*/

    @ApiModelProperty("关联的三者关系")
    List<RelationShipForUnlockVO>relationShip;

}
