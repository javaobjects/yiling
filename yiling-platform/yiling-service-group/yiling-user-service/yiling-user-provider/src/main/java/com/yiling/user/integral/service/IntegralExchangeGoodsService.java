package com.yiling.user.integral.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralExchangeGoodsItemBO;
import com.yiling.user.integral.dto.request.QueryIntegralExchangeGoodsPageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralExchangeGoodsRequest;
import com.yiling.user.integral.dto.request.UpdateShelfStatusRequest;
import com.yiling.user.integral.entity.IntegralExchangeGoodsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分兑换商品表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
public interface IntegralExchangeGoodsService extends BaseService<IntegralExchangeGoodsDO> {

    /**
     * 积分兑换商品分页列表
     *
     * @param request
     * @return
     */
    Page<IntegralExchangeGoodsItemBO> queryListPage(QueryIntegralExchangeGoodsPageRequest request);

    /**
     * 更新上下架状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateShelfStatusRequest request);

    /**
     * 保存积分兑换商品
     *
     * @param request
     * @return
     */
    boolean saveExchangeGoods(SaveIntegralExchangeGoodsRequest request);
}
