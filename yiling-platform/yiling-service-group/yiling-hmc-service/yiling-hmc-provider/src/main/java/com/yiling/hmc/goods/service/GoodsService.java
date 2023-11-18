package com.yiling.hmc.goods.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.enterprise.dto.request.GoodsSaveListRequest;
import com.yiling.hmc.enterprise.dto.request.SyncGoodsSaveRequest;
import com.yiling.hmc.goods.bo.EnterpriseGoodsCountBO;
import com.yiling.hmc.goods.bo.HmcGoodsBO;
import com.yiling.hmc.goods.dto.HmcGoodsDTO;
import com.yiling.hmc.goods.dto.SyncGoodsDTO;
import com.yiling.hmc.goods.dto.request.HmcGoodsPageRequest;
import com.yiling.hmc.goods.dto.request.QueryHmcGoodsRequest;
import com.yiling.hmc.goods.entity.HmcGoodsDO;

/**
 * <p>
 * C端保险药品商家提成设置表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-24
 */
public interface GoodsService extends BaseService<HmcGoodsDO> {

    /**
     * id 查询c端商品
     * @param id
     * @return
     */
    HmcGoodsDTO findById(Long id);

    /**
     * C端保险药品商家提成设置表新增
     *
     * @param request C端保险药品商家提成设置表请求参数
     * @return 成功/失败
     */
    boolean saveGoodsList(GoodsSaveListRequest request);

    /**
     * 根据企业id查询药品信息
     *
     * @param eid 企业id
     * @return C端保险药品商家提成设置信息
     */
    List<HmcGoodsDO> listByEid(Long eid);

    /**
     * 根据企业id查询药品信息
     *
     * @param ids c端商品id
     * @return C端保险药品商家提成设置信息
     */
    List<HmcGoodsBO> batchQueryGoodsInfo(List<Long> ids);

    /**
     * 统计企业商品数量
     * @param eidList
     * @return
     */
    List<EnterpriseGoodsCountBO> countGoodsByEids(List<Long> eidList);

    /**
     * 根据企业商品分页
     * @param request
     * @return
     */
    Page<HmcGoodsBO> pageListByEid(HmcGoodsPageRequest request);

    /**
     * 根据规格id查询商品
     * @param request
     * @return
     */
    List<HmcGoodsDTO> findBySpecificationsId(QueryHmcGoodsRequest request);

    /**
     * 规格id和eid查找商家对应的商品
     * @param request
     * @return
     */
    HmcGoodsDTO findBySpecificationsIdAndEid(QueryHmcGoodsRequest request);

    /**
     * IH同步商品到HMC
     * @param request
     * @return
     */
    List<SyncGoodsDTO> syncGoodsToHmc(SyncGoodsSaveRequest request);
}
