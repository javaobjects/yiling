package com.yiling.dataflow.sale.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.dto.SaleTargetDTO;
import com.yiling.dataflow.sale.dto.request.QuerySaleTargetPageListRequest;
import com.yiling.dataflow.sale.entity.SaleTargetDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 销售指标表 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
public interface SaleTargetService extends BaseService<SaleTargetDO> {

    Page<SaleTargetDTO> queryPageList(QuerySaleTargetPageListRequest request);
}
