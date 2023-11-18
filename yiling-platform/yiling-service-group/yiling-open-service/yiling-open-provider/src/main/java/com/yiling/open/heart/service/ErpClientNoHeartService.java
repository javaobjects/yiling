package com.yiling.open.heart.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.open.heart.dto.request.SaveErpClientNoHeartRequest;
import com.yiling.open.heart.entity.ErpClientNoHeartDO;

import cn.hutool.core.date.DateTime;

/**
 * @author: houjie.sun
 * @date: 2022/10/27
 */
public interface ErpClientNoHeartService extends BaseService<ErpClientNoHeartDO> {

    Boolean saveBatchErpClientNoHeart(List<SaveErpClientNoHeartRequest> requestList, DateTime taskTime);

    Integer deleteByTaskTime(DateTime taskTime);

}
