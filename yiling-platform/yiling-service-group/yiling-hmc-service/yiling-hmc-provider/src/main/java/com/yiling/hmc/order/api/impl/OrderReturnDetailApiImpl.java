package com.yiling.hmc.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.api.OrderReturnDetailApi;
import com.yiling.hmc.order.dto.OrderReturnDetailDTO;
import com.yiling.hmc.order.entity.OrderReturnDetailDO;
import com.yiling.hmc.order.service.OrderReturnDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderReturnDetailApiImpl implements OrderReturnDetailApi {

    private final OrderReturnDetailService orderReturnDetailService;

    @Override
    public List<OrderReturnDetailDTO> listByReturnId(Long returnId) {
        List<OrderReturnDetailDO> returnDetailDOList = orderReturnDetailService.listByReturnId(returnId);
        return PojoUtils.map(returnDetailDOList, OrderReturnDetailDTO.class);
    }
}
