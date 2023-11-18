package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业开通平台表 DTO
 *
 * @author xuan.zhou
 * @date 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterprisePlatformDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 是否开通商城：0-否 1-是
     */
    private Integer mallFlag;

    /**
     * 是否开通POP：0-否 1-是
     */
    private Integer popFlag;

    /**
     * 是否开通销售助手：0-否 1-是
     */
    private Integer salesAssistFlag;

    /**
     * 是否开通互联网医院：0-否 1-是
     */
    private Integer internetHospitalFlag;

    /**
     * 是否开通数据中台：0-否 1-是
     */
    private Integer dataCenterFlag;

    /**
     * 是否开通HMC：0-否 1-是
     */
    private Integer hmcFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    public static EnterprisePlatformDTO newEmptyInstance() {
        EnterprisePlatformDTO enterprisePlatformDTO = new EnterprisePlatformDTO();
        enterprisePlatformDTO.setMallFlag(0);
        enterprisePlatformDTO.setPopFlag(0);
        enterprisePlatformDTO.setSalesAssistFlag(0);
        enterprisePlatformDTO.setInternetHospitalFlag(0);
        enterprisePlatformDTO.setDataCenterFlag(0);
        enterprisePlatformDTO.setHmcFlag(0);
        return enterprisePlatformDTO;
    }

}
