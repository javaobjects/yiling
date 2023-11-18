package com.yiling.user.enterprise.service;

import java.util.List;

import com.yiling.user.enterprise.entity.EnterpriseHighQualitySupplierDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 优质商家表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-05-10
 */
public interface EnterpriseHighQualitySupplierService extends BaseService<EnterpriseHighQualitySupplierDO> {

    /**
     * 获取所有优质商家
     *
     * @return
     */
    List<Long> getAllSupplier();

    /**
     * 根据企业ID批量获取优质商家
     *
     * @param eidList
     * @return
     */
    List<Long> getByEidList(List<Long> eidList);

    /**
     * 判断该企业是否为优质商家
     *
     * @param eid
     * @return
     */
    boolean getHighQualitySupplierFlag(Long eid);

    /**
     * 根据省份编码获取优质商家
     *
     * @param provinceCode
     * @return
     */
    List<Long> getByProvinceCode(String provinceCode);
}
