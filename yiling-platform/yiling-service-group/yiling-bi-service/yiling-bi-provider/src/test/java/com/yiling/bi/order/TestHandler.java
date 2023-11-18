package com.yiling.bi.order;

import java.util.List;

import com.yiling.bi.order.excel.BiCrmOrderExcel;

import cn.afterturn.easypoi.handler.inter.IReadHandler;

/**
 * @author fucheng.bai
 * @date 2023/1/31
 */
public class TestHandler implements IReadHandler<BiCrmOrderExcel> {

    private List<BiCrmOrderExcel> failList;

    private List<BiCrmOrderExcel> erpSaleFlowExcelList;

    public TestHandler(List<BiCrmOrderExcel> failList, List<BiCrmOrderExcel> erpSaleFlowExcelList) {
        this.failList = failList;
        this.erpSaleFlowExcelList = erpSaleFlowExcelList;
    }

    @Override
    public void handler(BiCrmOrderExcel biCrmOrderExcel) {
//        erpSaleFlowExcelList.add(biCrmOrderExcel);
    }

    @Override
    public void doAfterAll() {

    }
}
