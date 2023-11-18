package com.yiling.sjms.gb.api;

import java.util.List;

import com.yiling.sjms.gb.dto.GoodsInfoDTO;

/**
 * 团购商品信息
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
public interface GbGoodsInfoApi {
    /**
     * 根据表单id获取商品信息
     * @param gbId
     * @return
     */
    List<GoodsInfoDTO> listByGbId(Long gbId);

    /**
     * 根据表单ids获取商品信息
     * @param gbIds
     * @return
     */
    List<GoodsInfoDTO> listByGbIds(List<Long> gbIds);

    /**
     * 根据团购终端商业信息查询商品
     * @param companyIds
     * @return
     */
    List<GoodsInfoDTO> listByCompanyIds(List<Long> companyIds);
}
