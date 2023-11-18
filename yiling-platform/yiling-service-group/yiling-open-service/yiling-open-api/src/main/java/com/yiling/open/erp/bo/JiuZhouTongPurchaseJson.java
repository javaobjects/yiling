package com.yiling.open.erp.bo;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/3/28
 */
@Data
public class JiuZhouTongPurchaseJson {

    @JSONField(name="ZytBillId")
    public  String zytBillId;
    @JSONField(name="BranchId")
    public  String branchId;
    @JSONField(name="OpId")
    public  String opId;
    @JSONField(name="OpName")
    public  String opName;
    @JSONField(name="CustId")
    public  String custId;
    @JSONField(name="PurPlanDetList")
    private List<PurPlanDet> purPlanDetList;

    @Data
    public static class PurPlanDet {
        @JSONField(name="ProdId")
        public String prodId;
        @JSONField(name="ProdNo")
        public String prodNo;
        @JSONField(name="Price")
        public String price;
        @JSONField(name="Quantity")
        public String quantity;
    }
}
