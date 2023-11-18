package com.yiling.dataflow.sale.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.dto.SaleTargetDTO;
import com.yiling.dataflow.sale.dto.request.QuerySaleTargetCheckRequest;
import com.yiling.dataflow.sale.dto.request.QuerySaleTargetPageListRequest;
import com.yiling.dataflow.sale.dto.request.RemoveSaleTargetRequest;
import com.yiling.dataflow.sale.dto.request.SaveSaleTargetRequest;

public interface SaleTargetApi {
    SaleTargetDTO getById(Long id);

    Page<SaleTargetDTO> queryPageList(QuerySaleTargetPageListRequest request);

    boolean removeById(RemoveSaleTargetRequest request);

    int countByName(QuerySaleTargetCheckRequest request);

    Long save(SaveSaleTargetRequest request);
}
