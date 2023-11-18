package com.yiling.dataflow.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.dto.SaleTargetDTO;
import com.yiling.dataflow.sale.dto.request.QuerySaleTargetPageListRequest;
import com.yiling.dataflow.sale.entity.SaleDepartmentTargetDO;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.sale.dao.SaleTargetMapper;
import com.yiling.dataflow.sale.entity.SaleTargetDO;
import com.yiling.dataflow.sale.service.SaleTargetService;
import com.yiling.framework.common.base.BaseServiceImpl;

/**
 * <p>
 * 销售指标表 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Service
public class SaleTargetServiceImpl extends BaseServiceImpl<SaleTargetMapper, SaleTargetDO> implements SaleTargetService {

    @Override
    public Page<SaleTargetDTO> queryPageList(QuerySaleTargetPageListRequest request) {
        LambdaQueryWrapper<SaleTargetDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SaleTargetDO::getId);
        return PojoUtils.map(page(request.getPage(), wrapper), SaleTargetDTO.class);
    }
}
