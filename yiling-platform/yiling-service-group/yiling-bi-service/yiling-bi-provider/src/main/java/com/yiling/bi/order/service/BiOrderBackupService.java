package com.yiling.bi.order.service;

import java.util.Date;
import java.util.List;

import com.yiling.bi.order.entity.BiOrderBackupDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2022/9/19
 */
public interface BiOrderBackupService extends BaseService<BiOrderBackupDO> {

    void backupBiOrderData();

    List<BiOrderBackupDO> getNoMatchedBiOrderBackup(Date endDate, List<Long> b2bGoodsIdList);
}
