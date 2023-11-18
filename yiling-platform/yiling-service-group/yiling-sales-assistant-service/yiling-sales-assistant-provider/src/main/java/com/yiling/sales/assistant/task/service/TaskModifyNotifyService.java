package com.yiling.sales.assistant.task.service;

import com.yiling.sales.assistant.task.entity.MarketTaskDO;

/**
 * @author: gxl
 * @date: 2022/10/18
 */
public interface TaskModifyNotifyService {


    /**
     * 任务开始后 新增任务商品或者配送商给用户弹窗提示
     * @param marketTaskDO
     */
    void addGoodsOrDistributorNotify(MarketTaskDO marketTaskDO);
}
