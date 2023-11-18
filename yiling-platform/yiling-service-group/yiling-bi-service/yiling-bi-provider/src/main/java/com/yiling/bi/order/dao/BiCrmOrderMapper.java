package com.yiling.bi.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.bi.order.entity.BiCrmOrderDO;
import com.yiling.bi.order.entity.BiOrderBackupDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author fucheng.bai
 * @date 2022/9/21
 */
@Repository
public interface BiCrmOrderMapper extends BaseMapper<BiCrmOrderDO> {

    int batchInsert(@Param("list") List<BiCrmOrderDO> biCrmOrderDOList);

    List<Long> getNoMatchedGoodsIdList();
}
