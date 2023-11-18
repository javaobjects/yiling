package com.yiling.erp.client.web;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiling.erp.client.common.ErpClientViewConfig;
import com.yiling.erp.client.dao.SysConfigDao;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.StringUtil;
import com.yiling.open.erp.dto.SysConfig;

/**
 * @author shuan
 */
@Controller
@RequestMapping({"/erp"})
public class TabConfigController {

    @Autowired
    private SysConfigDao sysConfigDao;

    @RequestMapping(value = {"/tabView.htm"}, produces = {"text/html;charset=UTF-8"})
    public String tabView(HttpServletRequest request, HttpServletResponse response) {
        return "tabConfig";
    }

    @ResponseBody
    @RequestMapping(value = {"/getTaskConfigInfo.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String getTaskConfigInfo(HttpServletRequest request, HttpServletResponse response) {
        ErpClientViewConfig erpClientViewConfig = new ErpClientViewConfig();
        List<SysConfig> sysConfigList = null;
        try {
            sysConfigList = sysConfigDao.executeQuerySysConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from sys_config");
            if (!CollectionUtils.isEmpty(sysConfigList)) {
                SysConfig sysConfig = sysConfigList.get(0);
                String tabPane = sysConfig.getTabPane();
                if (StringUtil.isEmpty(tabPane)) {
                    erpClientViewConfig.setGoodsPane("1");
                } else {
                    erpClientViewConfig = JSON.parseObject(sysConfig.getTabPane(), ErpClientViewConfig.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString(erpClientViewConfig, SerializerFeature.WriteNullStringAsEmpty);
    }

    @ResponseBody
    @RequestMapping(value = {"/savePane.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String savePane(HttpServletRequest request, HttpServletResponse response) {
        String goodsPane = request.getParameter("goodsPane");
        String goodsBatchPane = request.getParameter("goodsBatchPane");
        String customerPane = request.getParameter("customerPane");
        String customerPricePane = request.getParameter("customerPricePane");
        String groupPricePane = request.getParameter("groupPricePane");
        String orderPane = request.getParameter("orderPane");
        String orderSnPane = request.getParameter("orderSnPane");
        String flowSaleNoPane = request.getParameter("flowSaleNoPane");
        String orderSendPane = request.getParameter("orderSendPane");
        String orderBillPane = request.getParameter("orderBillPane");
        String orderReturnPane = request.getParameter("orderReturnPane");
        String orderAgreementPane = request.getParameter("orderAgreementPane");
        String orderPurchasePane = request.getParameter("orderPurchasePane");
        String flowPurchaseNoPane = request.getParameter("flowPurchaseNoPane");
        String orderPurchaseSendPane = request.getParameter("orderPurchaseSendPane");
        String orderPurchaseFlowPane = request.getParameter("orderPurchaseFlowPane");
        String orderSaleFlowPane = request.getParameter("orderSaleFlowPane");
        String goodsBatchFlowPane = request.getParameter("goodsBatchFlowPane");
        String shopSaleFlowPane = request.getParameter("shopSaleFlowPane");
        String orderPurchaseDeliveryPane = request.getParameter("orderPurchaseDeliveryPane");
        ErpClientViewConfig erpClientViewConfig = new ErpClientViewConfig();
        erpClientViewConfig.setCustomerPricePane(customerPricePane);
        erpClientViewConfig.setGoodsPane(getValidConfigValue(goodsPane));
        erpClientViewConfig.setGoodsBatchPane(getValidConfigValue(goodsBatchPane));
        erpClientViewConfig.setCustomerPane(getValidConfigValue(customerPane));
        erpClientViewConfig.setGroupPricePane(getValidConfigValue(groupPricePane));
        erpClientViewConfig.setFlowSaleNoPane(getValidConfigValue(flowSaleNoPane));
        erpClientViewConfig.setOrderSnPane(getValidConfigValue(orderSnPane));
        erpClientViewConfig.setOrderPane(getValidConfigValue(orderPane));
        erpClientViewConfig.setOrderSendPane(getValidConfigValue(orderSendPane));
        erpClientViewConfig.setOrderBillPane(getValidConfigValue(orderBillPane));
        erpClientViewConfig.setOrderReturnPane(getValidConfigValue(orderReturnPane));
        erpClientViewConfig.setOrderAgreementPane(getValidConfigValue(orderAgreementPane));
        erpClientViewConfig.setOrderPurchasePane(orderPurchasePane);
        erpClientViewConfig.setFlowPurchaseNoPane(flowPurchaseNoPane);
        erpClientViewConfig.setOrderPurchaseSendPane(orderPurchaseSendPane);
        erpClientViewConfig.setOrderPurchaseFlowPane(orderPurchaseFlowPane);
        erpClientViewConfig.setOrderSaleFlowPane(orderSaleFlowPane);
        erpClientViewConfig.setGroupPricePane(groupPricePane);
        erpClientViewConfig.setGoodsBatchFlowPane(goodsBatchFlowPane);
        erpClientViewConfig.setOrderPurchaseDeliveryPane(orderPurchaseDeliveryPane);
        erpClientViewConfig.setShopSaleFlowPane(shopSaleFlowPane);
        try {
            sysConfigDao.updateSysConfigPane(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, JSON.toJSONString(erpClientViewConfig));
//        loger.info("*******************客户端 tab显示配置： (可以勾选  是否显示的tab页)  开始执行：" + this.ecvc.toString());
        } catch (Exception e) {
//            loger.info("*******************客户端 tab显示配置： (可以勾选  是否显示的tab页)  执行error：" + e.getMessage());
            e.printStackTrace();
            return "false";
        }
//        loger.info("*******************客户端 tab显示配置： (可以勾选  是否显示的tab页) 执行 success");
        return "success";
    }

    String getValidConfigValue(String v) {
        if ((StringUtil.isEmpty(v)) || (!"1".equals(v))) {
            return "0";
        }
        return "1";
    }
}
