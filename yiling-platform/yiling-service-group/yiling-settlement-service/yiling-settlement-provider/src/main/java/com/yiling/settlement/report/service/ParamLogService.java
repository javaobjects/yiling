package com.yiling.settlement.report.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.ylprice.dto.request.AddGoodsYilingPriceLogRequest;
import com.yiling.settlement.report.entity.ParamLogDO;
import com.yiling.settlement.report.entity.ParamSubDO;
import com.yiling.settlement.report.entity.ParamSubGoodsDO;

/**
 * <p>
 * 参数操作日志表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-06-09
 */
public interface ParamLogService extends BaseService<ParamLogDO> {

    /**
     * 新增子参数商品日志
     *
     * @param before
     * @param after
     * @param userId
     */
    void addOarSubGoodsLog(ParamSubGoodsDO before, ParamSubGoodsDO after, Long userId);

    /**
     * 新增商品价格日志
     *
     * @param before
     * @param after
     * @param userId
     */
    void addPriceLog(AddGoodsYilingPriceLogRequest before, AddGoodsYilingPriceLogRequest after, Long userId);

    /**
     * 新增会员价格日志
     *
     * @param before
     * @param after
     * @param userId
     */
    void addMemberLog(ParamSubDO before, ParamSubDO after, Long userId);
}
