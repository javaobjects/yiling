package com.yiling.open.ftp.handler;

import java.util.List;

import com.yiling.open.ftp.excel.ErpPurchaseFlowExcel;
import com.yiling.open.ftp.excel.ErpSaleFlowExcel;

import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.hutool.core.util.StrUtil;

public class ErpSaleFlowExcelReadHandler implements IReadHandler<ErpSaleFlowExcel> {

    private List<ErpSaleFlowExcel> failList;

    private List<ErpSaleFlowExcel> erpSaleFlowExcelList;

    public ErpSaleFlowExcelReadHandler(List<ErpSaleFlowExcel> failList, List<ErpSaleFlowExcel> erpSaleFlowExcelList) {
        this.failList = failList;
        this.erpSaleFlowExcelList = erpSaleFlowExcelList;
    }

    @Override
    public void handler(ErpSaleFlowExcel erpSaleFlowExcel) {
        if (StrUtil.isEmpty(erpSaleFlowExcel.getSoTime())) {
            erpSaleFlowExcel.setErrorMsg("销售单时间不能为空");
            failList.add(erpSaleFlowExcel);
            return;
        }
        if (StrUtil.isEmpty(erpSaleFlowExcel.getEnterpriseInnerCode())) {
            erpSaleFlowExcel.setErrorMsg("供应商内码不能为空");
            failList.add(erpSaleFlowExcel);
            return;
        }
        if (StrUtil.isEmpty(erpSaleFlowExcel.getGoodsInSn())) {
            erpSaleFlowExcel.setErrorMsg("商品内码不能为空");
            failList.add(erpSaleFlowExcel);
            return;
        }
        erpSaleFlowExcelList.add(erpSaleFlowExcel);
    }

    @Override
    public void doAfterAll() {

    }
}
