package com.yiling.open.ftp.handler;

import java.util.ArrayList;
import java.util.List;

import com.yiling.open.ftp.excel.ErpPurchaseFlowExcel;

import cn.afterturn.easypoi.handler.inter.IExcelModel;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.hutool.core.util.StrUtil;

public class ErpPurchaseFlowExcelReadHandler implements IReadHandler<ErpPurchaseFlowExcel> {

    private List<ErpPurchaseFlowExcel> failList;

    private List<ErpPurchaseFlowExcel> erpPurchaseFlowExcelList;

    public ErpPurchaseFlowExcelReadHandler(List<ErpPurchaseFlowExcel> failList, List<ErpPurchaseFlowExcel> erpPurchaseFlowExcelList) {
        this.failList = failList;
        this.erpPurchaseFlowExcelList = erpPurchaseFlowExcelList;
    }

    @Override
    public void handler(ErpPurchaseFlowExcel erpPurchaseFlowExcel) {
        if (StrUtil.isEmpty(erpPurchaseFlowExcel.getPoTime())) {
            erpPurchaseFlowExcel.setErrorMsg("采购单时间不能为空");
            failList.add(erpPurchaseFlowExcel);
            return;
        }
        if (StrUtil.isEmpty(erpPurchaseFlowExcel.getEnterpriseInnerCode())) {
            erpPurchaseFlowExcel.setErrorMsg("供应商内码不能为空");
            failList.add(erpPurchaseFlowExcel);
            return;
        }
        if (StrUtil.isEmpty(erpPurchaseFlowExcel.getGoodsInSn())) {
            erpPurchaseFlowExcel.setErrorMsg("商品内码不能为空");
            failList.add(erpPurchaseFlowExcel);
            return;
        }
        erpPurchaseFlowExcelList.add(erpPurchaseFlowExcel);
    }

    @Override
    public void doAfterAll() {

    }
}
