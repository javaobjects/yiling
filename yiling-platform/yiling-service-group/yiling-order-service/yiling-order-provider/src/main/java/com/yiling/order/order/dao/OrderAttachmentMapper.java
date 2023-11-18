package com.yiling.order.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.entity.OrderAttachmentDO;

/**
 * <p>
 * 订单相关附件 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-07-12
 */
@Repository
public interface OrderAttachmentMapper extends BaseMapper<OrderAttachmentDO> {

    int batchInsert(@Param("list")List<OrderAttachmentDO> list);
}
