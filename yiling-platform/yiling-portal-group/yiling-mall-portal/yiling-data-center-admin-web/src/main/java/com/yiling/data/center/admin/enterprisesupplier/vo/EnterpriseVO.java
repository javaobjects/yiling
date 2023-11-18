package com.yiling.data.center.admin.enterprisesupplier.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseVO extends BaseVO {

    /**
     * 企业名称
     */
    private String name;

}
