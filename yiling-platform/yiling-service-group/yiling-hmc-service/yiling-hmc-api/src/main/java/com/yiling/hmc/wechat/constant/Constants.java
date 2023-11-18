package com.yiling.hmc.wechat.constant;

/**
 * @author: gxl
 * @date: 2022/4/26
 */
public interface Constants {
    /**
     *   保单过期提醒短信模板 占位符-被保人姓名-保单过期时间
     */
    String INSURANC_EEXPIRE_TEMPLATE = "%s您好，你的福利计划将于%s过期，小亿管家了解到，您还有未兑付的药品，赶快去兑付吧！";
    /**
     *   保单过期提醒微信通知模板 占位符-保单过期时间
     */
    String INSURANC_EEXPIRE_WX_TEMPLATE = "您好保单将于%s过期，请尽快兑付取药，过期不取将作废！";

    /**
     * 保单过期微信提醒模板id
     */
    String INSURANCE_TEMPLATE_ID = "Bkg65sQ8NZyu21s4stzS9CpCR2J3VQebfSe3CKVfFu0";

    /**
     * 保单详情小程序页面路径
     */
    String INSURANCE_DETAIL_PATH = "pagesSub/my/insured/insuredDetails/index?id=";
}
