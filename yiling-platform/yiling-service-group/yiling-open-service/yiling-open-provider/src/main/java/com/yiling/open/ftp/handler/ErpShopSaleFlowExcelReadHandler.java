package com.yiling.open.ftp.handler;

import java.util.List;

import com.yiling.open.ftp.excel.ErpSaleFlowExcel;
import com.yiling.open.ftp.excel.ErpShopSaleFlowExcel;

import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.hutool.core.util.StrUtil;

/**
 * @author fucheng.bai
 * @date 2023/3/23
 */
public class ErpShopSaleFlowExcelReadHandler implements IReadHandler<ErpShopSaleFlowExcel> {

    private List<ErpShopSaleFlowExcel> failList;

    private List<ErpShopSaleFlowExcel> erpShopSaleFlowExcelList;

    public ErpShopSaleFlowExcelReadHandler(List<ErpShopSaleFlowExcel> failList, List<ErpShopSaleFlowExcel> erpShopSaleFlowExcelList) {
        this.failList = failList;
        this.erpShopSaleFlowExcelList = erpShopSaleFlowExcelList;
    }

    @Override
    public void handler(ErpShopSaleFlowExcel erpShopSaleFlowExcel) {
        if (StrUtil.isEmpty(erpShopSaleFlowExcel.getSoTime())) {
            erpShopSaleFlowExcel.setErrorMsg("销售单时间不能为空");
            failList.add(erpShopSaleFlowExcel);
            return;
        }
        if (StrUtil.isEmpty(erpShopSaleFlowExcel.getGoodsInSn())) {
            erpShopSaleFlowExcel.setErrorMsg("商品内码不能为空");
            failList.add(erpShopSaleFlowExcel);
            return;
        }
        erpShopSaleFlowExcelList.add(erpShopSaleFlowExcel);
    }

    @Override
    public void doAfterAll() {

    }
}
