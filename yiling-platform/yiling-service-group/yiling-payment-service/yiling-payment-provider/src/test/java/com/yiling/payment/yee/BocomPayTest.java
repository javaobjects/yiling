package com.yiling.payment.yee;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.egzosn.pay.common.bean.AssistOrder;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.BaseTest;
import com.yiling.payment.channel.bocom.BocomPayService;
import com.yiling.payment.channel.bocom.dto.MPNG020702ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG020705ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG210001ResponseV1;
import com.yiling.payment.channel.service.dto.PayOrderResultDTO;
import com.yiling.payment.channel.service.dto.request.QueryPayOrderRequest;
import com.yiling.payment.channel.service.impl.BocomPayServiceImpl;
import com.yiling.payment.channel.yee.request.YeePayOrder;
import com.yiling.payment.enums.PayChannelEnum;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.api.RefundApi;
import com.yiling.payment.pay.dto.request.CreatePayTradeRequest;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @date: 2023/5/9
 */
@Slf4j
public class BocomPayTest extends BaseTest {
    @Autowired
    private BocomPayService bocomPayService;
    @Autowired
    private PayApi payApi;
    @Autowired
    private RefundApi refundApi;
    @Autowired
    private BocomPayServiceImpl payService;



    @Test
    public void createPreOrderTest() {


        YeePayOrder order = new YeePayOrder(TradeTypeEnum.INQUIRY.getSubject(),TradeTypeEnum.INQUIRY.getBody(),new BigDecimal("0.01"),"TEST_PT202305251519",0l,"192.168.1.10");
        order.setOpenId("oW6Nj5COmSfA9CwYUab5V2XlYOr0");
        order.setAppId("wx4015099f1fca8328");


        Map<String, Object> objectMap = bocomPayService.createPreOrder(order, PaySourceEnum.BOCOM_PAY_WECHAT);

        System.out.println(JSONUtil.toJsonStr(objectMap));
    }


    @Test
    public void createScanOrderTest() {



        YeePayOrder order = new YeePayOrder(TradeTypeEnum.INQUIRY.getSubject(),TradeTypeEnum.INQUIRY.getBody(),new BigDecimal("0.01"),"TEST_PT20230524153914592759",0l,"192.168.1.10");

        order.setOpenId("oW6Nj5COmSfA9CwYUab5V2XlYOr0");
        order.setAppId("wx4015099f1fca8328");


        MPNG210001ResponseV1 mpng210001ResponseV1 = bocomPayService.scanCodePay(order, PaySourceEnum.BOCOM_PAY_WECHAT);

        System.out.println(JSONUtil.toJsonStr(mpng210001ResponseV1));


    }


    @Test
    public void queryOrderTest() {

        AssistOrder order = new AssistOrder();
        order.setOutTradeNo("TEST_PT202305151419");
        order.setTradeNo("010220230515141720000692605Y");

        MPNG020702ResponseV1 responseV1 = bocomPayService.queryOrder(order);

        System.out.println(JSONUtil.toJsonStr(responseV1));
    }

    @Test
    public void closeOrderTest() {
        AssistOrder order = new AssistOrder();
        order.setOutTradeNo("TEST_PT20230515151752982208");
        order.setTradeNo("010220230515151806000692614Y");

        MPNG020705ResponseV1 close = bocomPayService.close(order);

        System.out.println(JSONUtil.toJsonStr(close));

    }

    @Test
    public void createPayTrade() {

        CreatePayTradeRequest tradeRequest = new CreatePayTradeRequest();
        tradeRequest.setPayId("424dd921f51042b195b9c66992a7903c");
       // tradeRequest.setAppId("wx4015099f1fca8328");
        tradeRequest.setOpenId("oW6Nj5COmSfA9CwYUab5V2XlYOr0");
        tradeRequest.setUserIp("192.168.1.10");
        tradeRequest.setPayWay(PayChannelEnum.BOCOMPAY.getCode());
        tradeRequest.setPaySource(PaySourceEnum.BOCOM_PAY_WECHAT.getSource());
        tradeRequest.setTradeSource("hmc");

        Result<Map<String, Object>> payTrade = payApi.createPayTrade(tradeRequest);

        System.out.println(JSONUtil.toJsonStr(payTrade));

    }


    @Test
    public void refundOrderTest() {
        Result<Void> refundByRefundNo = refundApi.refundByRefundNo("TEST_PR20230526173320387080");

        System.out.println(JSONUtil.toJsonStr(refundByRefundNo));

    }

    @Test
    public void refundOrderQuery() {

        QueryPayOrderRequest refundOrder = new QueryPayOrderRequest();
        refundOrder.setMerchantNo("");
        refundOrder.setThirdTradeNo("010220230524173310000694265Y");
        refundOrder.setPayNo("TEST_PT20230524173310341170");
        refundOrder.setThird_fund_no("20230526103102010221470047Y");
        refundOrder.setRefund_no("TEST_PR20230526173320387080");

        PayOrderResultDTO payOrderResultDTO = payService.orderRefundQuery(refundOrder);

        System.out.println("....refundOrderQuery...." + JSONUtil.toJsonStr(payOrderResultDTO));

    }

    @Test
    public void decrypt() {

        TestVO vo = new TestVO();
        vo.setBiz_content("bHm7j3aKuKAjP0xK1rdmcXusttso8QHtOZ/m1E5+HxGgT1AdosMdtsdhKMA1gfHkxrbACjIwLKLpmxMsS14CyXfxHKV/Yxdj3DdYTLXLuEi2+j38a8DZ2X6ToiLlCa2obFFFXryvcBYcHY4hqMnB33lWIDz6UZRer14QLszWWzX0WteSpLL3DZOCGT6FacbXZsaWHPti+bdVV2VI5CK/Ha0muSIzfiRmnHe1fRCNZn3cDeeHODmR/wqjqKeP8XSEyd3KCaVfyVcOjmqwp6kr3cWiILwwg0P2YCJK2OxgGJxGiuFywS4A5CYyvI4p3e/DhBorB3Ep6awfolOOOsw1Qkt/879e70qntM8l1qBqk8QwoZsHHSMCOBiNykVpxKPMP2ipvPg3/SAYA922zsY2viWRn8Y4IzDJbjNlGZTp59Cmzc5UPXaG5rv+40q22KKIV9KAPOQ/aceXXFLznZdvQ2ZjwH/J0ZHjTLLdNQ047dzAwHGi+UiKqFFAYAfYWYf1E8X1+TmRGMLabQucw3yMzDg/Seznt3PhxbYzMAKJlgub35IfgI3kM1Ud+mxJSIOeA0+PXw65OfW9rlhZ56obw2Qs13Fje3Ryhp1hr/jaaz1IArmI/lZNAQ6yjzMy/2jzJqhZeW/WKweIZZTEwdXudViy5+zLaucqbtaVnMzIPWy3MXOQQPEnr06y0cD3SHzVtnByuGuTnW67YzcR44yGj6PXgCzaypT6/3u+l/zQzKFXgK7eW4Le5Axip7xlaoADZrlb48rDr8q9q13sKRpFBx7glH+sCbMeGMhxlB8W3l3ZbUG3J6vQB1JTdPHZKfEh6cOOU9jpTsa2Vzshbvbnl3nY3cPfFnBDvrZgEs02X0axBbQ7fqhgNACcyTRybPVbpGo6zxZEc1yuUH93M91PuQCFWwjgGopQ1ndIkGqzQewDE7TELGHqppBr42UGtSWS9Ujn17KSEdTKkF3w2ELOrA==");
        vo.setMsg_id("75048168c0684dd2be468e7f842d7c2f");
        vo.setTimestamp("2023-05-26 15:23:07");
        vo.setEncrypt_key("Cca9gU65D0IVxD4Py34KV+VDconnd/MSZOT3q6Cvrw6WjvNRNObPcrK7BrXf7U/gDkHQQup1t1w19Zm7Wk+g1q++8vYzuJPfqHnYUvrkP2dg+Qhfru4a5Poj91QjfzAAIrRnWUMyy+vtoRF3+xq3gIb7B0ixf4De5Ag4QbGBUV2jJwpPzZzqutBJUsmGDIw/QPcu2VUHpNZy0i4uQdIS6qPnxlhhH/O8GcgfwVkVnzsH+qd8QLsAMH5WCbyMRK683AfU+7EP535dP4cA1FCeAgdBzyLGifYdFHUTWoXo2A2LDb1AA++O+o+4qeUfqXatCukAqnbxV4OuxFn3/mo4Ig==");
        vo.setSign("p0axPK8NOs0oxgJ50gqZsP4cB5/phPuLulqrYlGgjB9dkuyC4JfgDUQSCINQGCrzo5FB8i5r3jf0mSLFYCFTLXUNqg+lrE8vKstSakQ3pCarGHa1d/Knlo/B3llBAjLUbz1dziKIF3Y7KdL78NwO+rUzuToamMCGW6h+uskAgO5oBZqTqTBIt5W8fxufLhh6TvFOtjNrBeRZkxbqa37rhlX3PS3n313tqaSw+F2uJ18uq/aAHOhr3K/UbbdAE0dI5qVb4cZHS27kmNyDBp6Le3FcI/tzkqSO/X0gXIOp5aoNyLrf4DoFWmb9WScvhkF+yQPgmh6MxpOaoqzjA80vWA==");

        String toJsonStr = vo.toString();




        TestVO vo1 = new TestVO();
        vo1.setBiz_content("j9nVLg7i45oHvnMC4l4JUoK0gERudB+iwD5r77jBlHiDQuIsch3jnl2K8Xhx19ugcpPrMwLcVTxsZLGjM1ueGeE+WpxDtXIvcAEpwPyNrSgOLChdq5iWcpXBNCahPPOu3zp9cjZse29g20cMmpexSMZgodT5D+66DU9LTi0CsbpsZg0HhEM2tLnDUhs+GnS8SoLbqUXFgyhZ8hdEirmJR6g+3OhN9GuZj2+Yy8U4cicBT8T1swWtSvvMnfBaCit8PgJsELy+NhvXOibSt8sB3rubIdB+8/MCzbxnwWLMn7xspx0trQmfviwiUKktdlkNwjqRYGNha8Z2544xdvhTHGEQGe/nr2bFgnVJdh8CmtpuuzaJBB8PCbjRlAhwCVJFNsCq4R+/SFGPU5TuaEDJy6Oqa7ALvywWekTEnDL+LmMZtxj07om3CRFER0FWBLjd");
        vo1.setMsg_id("c37170fb8e5b4d7b9374fae6b16334f9");
        vo1.setTimestamp("2020-10-26 15:11:54");
        vo1.setEncrypt_key("UgjDtW1VUP9pz4hMu9r7/dJ8ciQ3mEMRvdMpMlVLOWC9eF+otpUh2aN9nRQMn0KxXDnM7+ZTXmTDOwgsBsARqWt6R1i3xkFq5Hz8XvZAPpqMniR9AgNLzgWEscsAChjgAuhkmN6XVO3AKeqxGLo82NoDh5gGJ0yzVg2uS3hQRSLhiWkmY5f1nT5M4Hp3h1hPXL9e1bUBY/Zhs1IzWL2r7p5BAaQjUsCgt+CggKl8oa6zXfKiaTG9Z+l7tDa1lLqn5Hbkl83TMLEdebmzjv38X+lJCHO3lwvxKdaHr091nQUxXLH7p5hyoRBEieoyd5jpcwabB/ChTsKvHWiixjf1hw==");
        vo1.setSign("EWqfUedqP2LdPFH7ekT6klCu5VqVEyWMIrOUyRLXhRIqC856qSVrq1LPNKIjpip53cT1J7V5GBCTv77HwXA7jcoYWIUK0GDIAYIDk5LcLOtXcVQKJ9aIqoUOS8NHIYSlx3wt41GoSsVZgWg8o4c5+qh2BxO1QkG/LrvsXSv5hBDssAztfWvwu2bcitSRPBFAuAyBs5ETLjcfSYFP7fPN9g1FG93McuvzPqJVHWEnKW4EfaLXDeI0FUNAgmH9xCcjYw7pbbxZTl3QdNrhSTOrBHnI0XZFNp9woF9YpyaSfKVCmtaL27M2RnLxyOnjffS9qSJP64xzpI9DRrsw0/EQAA==");



        System.out.println(vo1.toString());

        String st = JSONUtil.toJsonStr(vo);

        System.out.println(st + "-----");

        String result = "{\"biz_content\":\"bHm7j3aKuKAjP0xK1rdmcXusttso8QHtOZ/m1E5+HxGgT1AdosMdtsdhKMA1gfHkxrbACjIwLKLpmxMsS14CyXfxHKV/Yxdj3DdYTLXLuEi2+j38a8DZ2X6ToiLlCa2obFFFXryvcBYcHY4hqMnB33lWIDz6UZRer14QLszWWzX0WteSpLL3DZOCGT6FacbXZsaWHPti+bdVV2VI5CK/Ha0muSIzfiRmnHe1fRCNZn3cDeeHODmR/wqjqKeP8XSEyd3KCaVfyVcOjmqwp6kr3cWiILwwg0P2YCJK2OxgGJxGiuFywS4A5CYyvI4p3e/DhBorB3Ep6awfolOOOsw1Qkt/879e70qntM8l1qBqk8QwoZsHHSMCOBiNykVpxKPMP2ipvPg3/SAYA922zsY2viWRn8Y4IzDJbjNlGZTp59Cmzc5UPXaG5rv+40q22KKIV9KAPOQ/aceXXFLznZdvQ2ZjwH/J0ZHjTLLdNQ047dzAwHGi+UiKqFFAYAfYWYf1E8X1+TmRGMLabQucw3yMzDg/Seznt3PhxbYzMAKJlgub35IfgI3kM1Ud+mxJSIOeA0+PXw65OfW9rlhZ56obw2Qs13Fje3Ryhp1hr/jaaz1IArmI/lZNAQ6yjzMy/2jzJqhZeW/WKweIZZTEwdXudViy5+zLaucqbtaVnMzIPWy3MXOQQPEnr06y0cD3SHzVtnByuGuTnW67YzcR44yGj6PXgCzaypT6/3u+l/zQzKFXgK7eW4Le5Axip7xlaoADZrlb48rDr8q9q13sKRpFBx7glH+sCbMeGMhxlB8W3l3ZbUG3J6vQB1JTdPHZKfEh6cOOU9jpTsa2Vzshbvbnl3nY3cPfFnBDvrZgEs02X0axBbQ7fqhgNACcyTRybPVbpGo6zxZEc1yuUH93M91PuQCFWwjgGopQ1ndIkGqzQewDE7TELGHqppBr42UGtSWS9Ujn17KSEdTKkF3w2ELOrA==\",\"msg_id\":\"75048168c0684dd2be468e7f842d7c2f\",\"timestamp\":\"2023-05-26 15:23:07\",\"encrypt_key\":\"Cca9gU65D0IVxD4Py34KV+VDconnd/MSZOT3q6Cvrw6WjvNRNObPcrK7BrXf7U/gDkHQQup1t1w19Zm7Wk+g1q++8vYzuJPfqHnYUvrkP2dg+Qhfru4a5Poj91QjfzAAIrRnWUMyy+vtoRF3+xq3gIb7B0ixf4De5Ag4QbGBUV2jJwpPzZzqutBJUsmGDIw/QPcu2VUHpNZy0i4uQdIS6qPnxlhhH/O8GcgfwVkVnzsH+qd8QLsAMH5WCbyMRK683AfU+7EP535dP4cA1FCeAgdBzyLGifYdFHUTWoXo2A2LDb1AA++O+o+4qeUfqXatCukAqnbxV4OuxFn3/mo4Ig==\",\"sign\":\"p0axPK8NOs0oxgJ50gqZsP4cB5/phPuLulqrYlGgjB9dkuyC4JfgDUQSCINQGCrzo5FB8i5r3jf0mSLFYCFTLXUNqg+lrE8vKstSakQ3pCarGHa1d/Knlo/B3llBAjLUbz1dziKIF3Y7KdL78NwO+rUzuToamMCGW6h+uskAgO5oBZqTqTBIt5W8fxufLhh6TvFOtjNrBeRZkxbqa37rhlX3PS3n313tqaSw+F2uJ18uq/aAHOhr3K/UbbdAE0dI5qVb4cZHS27kmNyDBp6Le3FcI/tzkqSO/X0gXIOp5aoNyLrf4DoFWmb9WScvhkF+yQPgmh6MxpOaoqzjA80vWA==\"}";


        Map<String, Object> decrypt = bocomPayService.decrypt(vo1.toString());

        System.out.println(decrypt + "================");

    }

}
