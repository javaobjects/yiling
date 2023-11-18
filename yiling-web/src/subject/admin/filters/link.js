let data = {
  // 企业信息导入模板
  // NO_1: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_enterprise_templdate_v1.2_20210917.xlsx',
  NO_1: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_enterprise_template_v1.3_20220629.xlsx',
  // 企业下用户导入模板
  NO_2: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_staff_template_v1.3_20220614.xlsx',
  // 商品管理导入模板
  NO_3: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_goods_templadate_v20210810.xlsx',
  // 基础药品管理导入模版
  NO_4: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_standard_templadate_v20230523.xlsx',
  // 商品管理详情导入模板
  NO_5: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_goods_templadate_v20210531.xlsx',
  // b2b商品管理详情导入模板
  NO_6: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_goods_templadate_v20211124.xls',
  // 营销活动促销商品导入模板
  NO_7: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_promotion_goods_template_v20211126.xls',
  // 以岭健康中心-保险财务账单-财务中心-以岭给商家药品对账-详情-已完成-导入模板
  NO_9: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_enterprise_settlement_template_v20220517.xls',
  // 以岭健康中心-保险财务账单-财务中心-保司结账明细-导入模板
  NO_10: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_insurance_settlement_template_v20220517.xls',
  // 企业下载产品线模板
  NO_11: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_enterprise_platform_template_v1.0_20220629.xlsx',
  // 数据报表参数-促销活动设置
  NO_12: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_report_par_activity_V1.1_20221115.xlsx',
  // 数据报表参数-阶梯规则设置
  NO_13: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_report_par_ladder_V1.1_20221115.xlsx',
  // 数据报表参数-会员返利金额
  NO_14: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_report_par_member_v1.1_20221201.xlsx',
  // 数据报表-返利总表-下载模板
  NO_15: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_report_rebate_template_v1.0_20220823.xls',
  // 数据报表-B2B订单返利表和流向返利表(公用)-下载模板
  NO_16: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_report_order_ident_template_v1.0_20220901.xlsx',
  // 质量监管-商品对应标准库匹配-下载模板
  NO_17: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/improt_merge_specification_template_v20220902.xlsx',
  // b2b-会员管理-会员查询-下载模板
  NO_18: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_open_member_templdate_v1.1_20221125.xlsx',
  // excel导入发券-下载模板
  NO_19: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_coupon_template_v2_20221128.xls'

}

export function template(key) {
  return data[key] || ''
}
