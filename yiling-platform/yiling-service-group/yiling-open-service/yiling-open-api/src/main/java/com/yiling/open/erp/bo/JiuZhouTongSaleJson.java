package com.yiling.open.erp.bo;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/3/28
 */
@Data
public class JiuZhouTongSaleJson {

    @JSONField(name = "BillGuid")
    public String billGuid;
    @JSONField(name = "CustNo")
    public String custNo;
    @JSONField(name = "CustID")
    public String custID;
    @JSONField(name = "OrderSource")
    public String orderSource;
    @JSONField(name = "BranchID")
    public String branchId;
    @JSONField(name = "OrderDate")
    public String orderDate;

    @JSONField(name = "ShoppingCartDetList")
    private List<PlatOrderDet> shoppingCartDetList;

    @Data
    public static class PlatOrderDet {
        @JSONField(name = "ProdNo")
        public String prodNo;
        @JSONField(name = "Price")
        public String price;
        @JSONField(name = "OrderSourceId")
        public String orderSourceId;
        @JSONField(name = "BranchID")
        public String branchID;
        @JSONField(name = "ProdID")
        public String prodID;
        @JSONField(name = "Qty")
        public String qty;
    }
}
