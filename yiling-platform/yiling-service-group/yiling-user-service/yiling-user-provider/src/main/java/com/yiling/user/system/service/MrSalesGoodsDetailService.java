package com.yiling.user.system.service;

import java.util.List;
import java.util.Map;

import com.yiling.user.system.dto.request.AddOrRemoveMrSalesGoodsRequest;
import com.yiling.user.system.dto.request.UpdateMrSalesGoodsRequest;
import com.yiling.user.system.entity.MrSalesGoodsDetailDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 医药代表可售药品配置信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-06-06
 */
public interface MrSalesGoodsDetailService extends BaseService<MrSalesGoodsDetailDO> {

    /**
     * 批量获取医药代表可售药品配置信息列表
     * @param employeeIds 医药代表员工ID列表
     * @return java.util.Map<java.lang.Long,java.util.List<java.lang.Long>>
     * @author xuan.zhou
     * @date 2022/6/6
     **/
    Map<Long, List<Long>> listByEmployeeIds(List<Long> employeeIds);

    /**
     * 根据员工ID和多个商品ID查询配置信息，返回已添加的商品ID列表
     *
     * @param employeeId 员工ID
     * @param goodsIds 商品ID列表
     * @return java.util.List<com.yiling.user.system.entity.MrSalesGoodsDetailDO>
     * @author xuan.zhou
     * @date 2022/6/6
     **/
    List<Long> listByEmployeeIdAndGoodsIds(Long employeeId, List<Long> goodsIds);

    /**
     * 批量根据商品ID查询绑定的医药代表ID
     *
     * @param goodsIds 商品ID列表
     * @return java.util.List<java.lang.Long>
     * @author xuan.zhou
     * @date 2022/6/9
     **/
    List<Long> listEmoloyeeIdsByGoodsIds(List<Long> goodsIds);

    /**
     * 添加或删除可售商品
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/6/6
     **/
    Boolean addOrRemove(AddOrRemoveMrSalesGoodsRequest request);

    /**
     * 更新医药代表可售商品信息
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/6/7
     **/
    Boolean update(UpdateMrSalesGoodsRequest request);
}
