let data = {
  // 企业信息导入模板
  NO_1: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_enterprise_templdate_v202150281720.xlsx',
  // 企业下用户导入模板
  NO_2: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_staff_template_v202105281721.xlsx',
  // 商品管理导入模板
  NO_3: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_goods_templadate_v20210531.xlsx',
  // 营销活动促销商品导入模板
  NO_7: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_promotion_goods_template_v20211126.xls',
  
  // b2b 商品管理 控价管理 下载模版
  NO_8: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_goods_limit_templadate_v20211130.xlsx',
  NO_9: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_goods_price_templadate_v20211130.xlsx',

  // 新版协议导入商品 下载模版
  NO_10: 'https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/template/import_agreementv2_goods_template_v20220316.xls'

}

// 获取模板 url
export function template(key) {
  return data[key] || ''
}
