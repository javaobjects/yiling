package com.yiling.user.control.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.control.dto.request.DeleteGoodsControlRequest;
import com.yiling.user.control.entity.GoodsControlConditionDO;

/**
 * <p>
 * 商品控销条件表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-21
 */
public interface GoodsControlConditionService extends BaseService<GoodsControlConditionDO> {

    /**
     * 商品Id获取客户类型控销ID集合
     * @param controlId
     * @return
     */
    List<Long> getValueIdByControlId(Long controlId);

    /**
     * 商品Id获取客户类型控销ID集合
     * @param controlIds
     * @return
     */
    Map<Long,List<GoodsControlConditionDO>> getValueIdByControlIds(List<Long> controlIds);

    /**
     * 删除客户控销
     * @param request
     * @return
     */
    Boolean deleteCustomer(DeleteGoodsControlRequest request);

    /**
     * 删除控销
     * @param controlId
     * @return
     */
    Boolean deleteControlCondition(Long controlId,Long operUserId);


}
