package com.yiling.sjms.gb.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.entity.GbGoodsInfoDO;

/**
 * <p>
 * 团购商品信息 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
public interface GbGoodsInfoService extends BaseService<GbGoodsInfoDO> {
    /**
     * 通过表单id删除商品信息
     * @param gbId
     * @return
     */
    Integer deleteGoodsByGbId(Long gbId);

    /**
     * 根据表单id获取商品信息
     * @param gbId
     * @return
     */
    List<GbGoodsInfoDO> listByGbId(Long gbId);

    /**
     * 根据表单ids获取商品信息
     * @param gbIds
     * @return
     */
    List<GbGoodsInfoDO> listByGbIds(List<Long> gbIds);

    /**
     * 根据团购终端商业信息查询商品
     * @param companyId
     * @return
     */
    List<GbGoodsInfoDO> listByCompanyId(Long companyId);

    /**
     * 根据团购终端商业信息查询商品
     * @param companyIds
     * @return
     */
    List<GbGoodsInfoDO> listByCompanyIds(List<Long> companyIds);
}
