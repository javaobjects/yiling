package com.yiling.open.ftp.handler;

import java.util.List;

import com.yiling.open.ftp.excel.ErpGoodsBatchFlowExcel;
import com.yiling.open.ftp.excel.ErpPurchaseFlowExcel;
import com.yiling.open.ftp.excel.ErpSaleFlowExcel;

import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.hutool.core.util.StrUtil;

public class ErpGoodsBatchFlowExcelReadHandler implements IReadHandler<ErpGoodsBatchFlowExcel> {

    private List<ErpGoodsBatchFlowExcel> failList;

    private List<ErpGoodsBatchFlowExcel> erpGoodsBatchFlowExcelList;

    public ErpGoodsBatchFlowExcelReadHandler(List<ErpGoodsBatchFlowExcel> failList, List<ErpGoodsBatchFlowExcel> erpGoodsBatchFlowExcelList) {
        this.failList = failList;
        this.erpGoodsBatchFlowExcelList = erpGoodsBatchFlowExcelList;
    }

    @Override
    public void handler(ErpGoodsBatchFlowExcel erpGoodsBatchFlowExcel) {
        if (StrUtil.isEmpty(erpGoodsBatchFlowExcel.getInSn())) {
            erpGoodsBatchFlowExcel.setErrorMsg("商品内码不能为空");
            failList.add(erpGoodsBatchFlowExcel);
            return;
        }
        erpGoodsBatchFlowExcelList.add(erpGoodsBatchFlowExcel);
    }

    @Override
    public void doAfterAll() {

    }
}
