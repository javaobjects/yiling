package com.yiling.user.control.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.control.dto.GoodsControlDTO;
import com.yiling.user.control.dto.request.BatchSaveCustomerControlRequest;
import com.yiling.user.control.dto.request.DeleteGoodsControlRequest;
import com.yiling.user.control.dto.request.QueryCustomerControlPageRequest;
import com.yiling.user.control.dto.request.SaveCustomerControlRequest;
import com.yiling.user.control.dto.request.SaveRegionControlRequest;
import com.yiling.user.control.entity.GoodsControlDO;

/**
 * <p>
 * 商品控销表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-21
 */
public interface GoodsControlService extends BaseService<GoodsControlDO> {


    /**
     * 商品Id获取客户类型控销ID集合
     * @param goodsId
     * @return
     */
    List<Long> getCustomerType(Long goodsId);

    /**
     * 商品Id获取区域控销ID集合
     * @param goodsId
     * @return
     */
    List<Long> getRegionId(Long goodsId);

    /**
     * 商品Id获取客户控销ID集合
     * @param goodsId
     * @return
     */
    List<Long> getCustomerId(Long goodsId);

    GoodsControlDTO getCustomerTypeInfo(Long goodsId, Long eid);

    GoodsControlDTO getRegionInfo(Long goodsId,Long eid);

    GoodsControlDTO getCustomerInfo(Long goodsId,Long eid);

    Page<Long> getPageCustomerInfo(QueryCustomerControlPageRequest request);

    Boolean saveRegion(SaveRegionControlRequest request);

    /**
     * 保存客户控销
     * @param request
     * @return
     */
    Boolean saveCustomer(SaveCustomerControlRequest request);

    Boolean batchSaveCustomer(BatchSaveCustomerControlRequest request);

    Boolean deleteCustomer(DeleteGoodsControlRequest request);

    Map<Long,Integer> getGoodsControlByBuyerEidAndGid(List<Long> goodsIds,Long eid, Long buyerEid);

}
