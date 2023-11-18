package com.yiling.open.webservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.openapi.third.OpenApi;
import com.kingdee.bos.openapi.third.OpenApiFactory;
import com.kingdee.bos.openapi.third.OpenApiInfo;
import com.kingdee.bos.openapi.third.ctx.CommonLogin;
import com.kingdee.bos.openapi.third.exception.BizException;
import com.kingdee.bos.openapi.third.exception.InvokeNetworkException;
import com.kingdee.bos.openapi.third.exception.LoginException;
import com.kingdee.bos.openapi.third.login.EASLoginContext;
import com.yiling.framework.common.util.Constants;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.webservice.json.GoodsCustomerPriceJson;
import com.yiling.open.webservice.json.InventoryJson;

/**
 * @author: shuang.zhang
 * @date: 2021/7/28
 */
public class EasInventoryTest {

    public static void main(String[] args) throws LoginException, BizException, InvokeNetworkException {
        //IP地址
        String ip = "110.249.133.136";
        //端口
        int port = 6890;
        //用户名
        String userName = "pop";
        //密码
        String password = "Yiling@123";
        //数据中心编码
        String dcName = "pop";
        String language = "l2";
        EASLoginContext loginCtx = new EASLoginContext.Builder(ip, port,
                new CommonLogin.Builder(userName, password, dcName, language)
                        .build()).https(false)
                .build();
        OpenApiInfo info = new OpenApiInfo();
        //调用api的方法名
        info.setApi("EasBasicDataWebserviceFacade-InventoryQuery");
        //调用参数，格式是数组形式
        //"XSCK-01-202110-0021","XSCK-01-202110-0020"
        info.setData("[\"[01]\",\"1970-01-01 00:00:00\",\"[02.01.004.001]\"]");
        OpenApi openApi = OpenApiFactory.getService(loginCtx);
        //返回数据
        String result = openApi.invoke(info);
        JSONObject jsonObject = JSON.parseObject(result);
        List<InventoryJson> lists=new ArrayList<>();
        if (jsonObject.getInteger("resultCode").equals(200)) {
            List<InventoryJson> list = JSON.parseArray(jsonObject.getString("body"), InventoryJson.class);
            // 保存去重后的请求数据
            Map<String, BaseErpEntity> dataMap = new LinkedHashMap<>();
            //汇总库存信息
            Map<String, BigDecimal> goodsMap = new HashMap<>();
            for (InventoryJson inventoryJson : list) {
                BigDecimal lockQty = BigDecimal.ZERO;
                if (StrUtil.isNotEmpty(inventoryJson.getLockqty())) {
                    lockQty = new BigDecimal(inventoryJson.getLockqty());
                }
                BigDecimal reservationbaseqty = BigDecimal.ZERO;
                if (StrUtil.isNotEmpty(inventoryJson.getReservationbaseQty())) {
                    reservationbaseqty = new BigDecimal(inventoryJson.getReservationbaseQty());
                }
                BigDecimal qty = new BigDecimal(inventoryJson.getCurstoreQty()).subtract(lockQty).subtract(reservationbaseqty);
                if (qty.compareTo(BigDecimal.ZERO) > 0 && isAvailable(inventoryJson, "01")) {
                    lists.add(inventoryJson);
                    if (goodsMap.containsKey(inventoryJson.getMatNumber())) {
                        BigDecimal number = goodsMap.get(inventoryJson.getMatNumber()).add(qty);
                        goodsMap.put(inventoryJson.getMatNumber(), number);
                    } else {
                        goodsMap.put(inventoryJson.getMatNumber(), qty);
                    }
                }
            }

            for (Map.Entry<String, BigDecimal> entry : goodsMap.entrySet()) {
                ErpGoodsBatchDTO erpGoodsBatch = new ErpGoodsBatchDTO();
                erpGoodsBatch.setGbNumber(entry.getValue());
                erpGoodsBatch.setInSn(entry.getKey());
                erpGoodsBatch.setSuDeptNo("01");
                erpGoodsBatch.setGbIdNo("01-" + entry.getKey());
                erpGoodsBatch.setGbBatchNo("");
                erpGoodsBatch.setSuId(Constants.YILING_EID);
                erpGoodsBatch.setOperType(1);
                dataMap.put(erpGoodsBatch.getErpPrimaryKey(), erpGoodsBatch);
            }
            System.out.println("aa");
        }
        System.out.println(result);
    }

    public static boolean isAvailable(InventoryJson inventoryJson, String suSeptNo) {
        if (suSeptNo.equals("04.01") && inventoryJson.getWareHouseNumber().equals("040103")) {
            return true;
        } else if (suSeptNo.equals("01") && inventoryJson.getWareHouseNumber().equals("0103")) {
            return true;
        }
        return false;
    }
}
