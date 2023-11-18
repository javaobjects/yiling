package com.yiling.open.webservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.yiling.open.erp.dto.ErpGoodsCustomerPriceDTO;
import com.yiling.open.webservice.json.GoodsCustomerPriceJson;

import cn.hutool.core.util.StrUtil;

/**
 * @author: shuang.zhang
 * @date: 2021/7/28
 */
public class InformTest {

    public static void main(String[] args) throws LoginException, BizException, InvokeNetworkException {
        //IP地址
        String ip = "222.223.229.34";
        //端口
        int port = 6888;
        //用户名
        String userName = "PMS";
        //密码
        String password = "Yiling@123";
        //数据中心编码
        String dcName = "yl01";
        String language = "l2";
        EASLoginContext loginCtx = new EASLoginContext.Builder(ip, port,
                new CommonLogin.Builder(userName, password, dcName, language)
                        .build()).https(false)
                .build();
        OpenApiInfo info = new OpenApiInfo();
        //调用api的方法名
        info.setApi("PopToEasWebserviceFacade-Receivemessage");
        //调用参数，格式是数组形式
        //"XSCK-01-202110-0021","XSCK-01-202110-0020"
        info.setData("[\"1\", \"339942\"]");
        OpenApi openApi = OpenApiFactory.getService(loginCtx);
        //返回数据
        String result = openApi.invoke(info);
        System.out.println(result);
    }

//    public static void main(String[] args) throws LoginException, BizException, InvokeNetworkException {
////        IP地址
//                String ip = "222.223.229.34";
//                //端口
//                int port = 6888;
//                //用户名
//                String userName = "PMS";
//                //密码
//                String password = "Yiling@123";
//                //数据中心编码
//                String dcName = "yl01";
//                String language = "l2";
//        EASLoginContext loginCtx = new EASLoginContext.Builder(ip, port, new CommonLogin.Builder(userName, password, dcName, language).build()).https(false).build();
//        OpenApiInfo info = new OpenApiInfo();
//        //调用api的方法名
//        info.setApi("EasBasicDataWebserviceFacade-pricePolicydetils");
//        //调用参数，格式是数组形式
//        info.setData("[\"01\",\"[010.GJCD]\",\"[02.01.011.003]\",\"1970-01-01 00:00:00\"]");
//        OpenApi openApi = OpenApiFactory.getService(loginCtx);
//        //返回数据
//        String result = openApi.invoke(info);
//        List<GoodsCustomerPriceJson> list = new ArrayList<>();
//        JSONObject jsonObject = JSON.parseObject(result);
//        if (jsonObject.getInteger("resultCode").equals(200)) {
//            list.addAll(JSON.parseArray(jsonObject.getString("body"), GoodsCustomerPriceJson.class));
//        }
//        for (GoodsCustomerPriceJson goodsCustomerPriceJson : list) {
//            if (goodsCustomerPriceJson.getDisprice().compareTo(BigDecimal.ZERO) > 0) {
////                String orgNo = goodsInsn.get(goodsCustomerPriceJson.getMatno());
////                if (StrUtil.isEmpty(orgNo)) {
////                    continue;
////                }
////                String key = goodsCustomerPriceJson.getMatno() + "-" + goodsCustomerPriceJson.getCustno();
////                if (price.contains(key)) {
////                    continue;
////                }
////                ErpGoodsCustomerPriceDTO erpGoodsCustomerPriceDTO = new ErpGoodsCustomerPriceDTO();
////                erpGoodsCustomerPriceDTO.setGcpIdNo(orgNo + goodsCustomerPriceJson.getCustno() + goodsCustomerPriceJson.getMatno());
////                erpGoodsCustomerPriceDTO.setInSn(goodsCustomerPriceJson.getMatno());
////                erpGoodsCustomerPriceDTO.setInnerCode(goodsCustomerPriceJson.getCustno());
////                erpGoodsCustomerPriceDTO.setPrice(goodsCustomerPriceJson.getDisprice().setScale(4, BigDecimal.ROUND_HALF_UP));
////                erpGoodsCustomerPriceDTO.setSuDeptNo(orgNo);
////                erpGoodsCustomerPriceDTO.setSuId(ErpConstants.YILING_EID);
////                erpGoodsCustomerPriceDTO.setOperType(1);
////                dataMap.add(erpGoodsCustomerPriceDTO);
//            }
//        }
//    }

}
