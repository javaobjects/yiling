package com.yiling.user.control.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.control.dto.GoodsControlDTO;
import com.yiling.user.control.dto.request.BatchSaveCustomerControlRequest;
import com.yiling.user.control.dto.request.DeleteGoodsControlRequest;
import com.yiling.user.control.dto.request.QueryCustomerControlPageRequest;
import com.yiling.user.control.dto.request.SaveCustomerControlRequest;
import com.yiling.user.control.dto.request.SaveRegionControlRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/10/21
 */
public interface ControlApi {

    /**
     * 选择类型
     * @param goodsId
     * @param eid
     * @return
     */
     GoodsControlDTO getCustomerTypeInfo(Long goodsId, Long eid);

    /**
     * 选择类型
     * @param goodsId
     * @param eid
     * @return
     */
    GoodsControlDTO getRegionInfo(Long goodsId, Long eid);

    /**
     * 选择类型
     * @param goodsId
     * @param eid
     * @return
     */
    GoodsControlDTO getCustomerInfo(Long goodsId, Long eid);

    /**
     *
     * @param request
     * @return
     */
    Page<Long> getPageCustomerInfo(QueryCustomerControlPageRequest request);

    /**
     * 保存区域类型控销
     * @param request
     * @return
     */
    Boolean saveRegion(SaveRegionControlRequest request);

    /**
     * 保存客户控销
     * @param request
     * @return
     */
    Boolean saveCustomer(SaveCustomerControlRequest request);

    /**
     * 保存客户控销
     * @param request
     * @return
     */
    Boolean batchSaveCustomer(BatchSaveCustomerControlRequest request);

    /**
     * 删除客户控销
     * @param request
     * @return
     */
    Boolean deleteCustomer(DeleteGoodsControlRequest request);

    /**
     * 获取商品对应该客户是否控销
     * @param goodsIds
     * @param eid
     * @param buyerEid
     * @return
     */
    Map<Long,Integer> getGoodsControlByBuyerEidAndGid(List<Long> goodsIds,Long eid,Long buyerEid);
}
