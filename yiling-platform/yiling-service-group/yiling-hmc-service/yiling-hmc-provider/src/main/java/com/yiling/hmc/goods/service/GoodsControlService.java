package com.yiling.hmc.goods.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.goods.bo.GoodsControlBO;
import com.yiling.hmc.goods.dto.GoodsControlDTO;
import com.yiling.hmc.goods.dto.request.GoodsControlPageRequest;
import com.yiling.hmc.goods.dto.request.GoodsControlSaveRequest;
import com.yiling.hmc.goods.entity.GoodsControlDO;

/**
 * @author shichen
 * @类名 GoodsControlService
 * @描述 管控商品
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
public interface GoodsControlService extends BaseService<GoodsControlDO> {

    /**
     * 标准库id批量获取管控商品
     * @param specificationsIds
     * @return
     */
    List<GoodsControlDTO> batchGetGoodsControlBySpecificationsIds(List<Long> specificationsIds);

    /**
     * 分页查询管控商品
     * @return
     */
    Page<GoodsControlBO> pageList(GoodsControlPageRequest request);

    /**
     * 保存或修改管控商品
     * @param request
     * @return
     */
    Long saveOrUpdateGoodsControl(GoodsControlSaveRequest request);

    /**
     * 规格id查询管控商品
     * @param specificationsId
     * @return
     */
    GoodsControlDTO findGoodsControlBySpecificationsId(Long specificationsId);

    /**
     * 管控id获取管控商品信息
     * @param controlIds
     * @return
     */
    List<GoodsControlBO> getGoodsControlInfoByIds(List<Long> controlIds);

    /**
     * 获取上架商品管控商品分页列表
     * @param request
     * @return
     */
    Page<GoodsControlDTO> queryUpGoodsControlPageList(GoodsControlPageRequest request);

    /**
     * 获取保险商品管控商品分页列表
     * @param request
     * @return
     */
    Page<GoodsControlBO> queryInsuranceGoodsControlPageList(GoodsControlPageRequest request);
}
