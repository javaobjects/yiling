/*
 * @Description:
 * @Author: xuxingwang
 * @Date: 2022-02-22 18:07:53
 * @LastEditTime: 2022-03-01 18:56:36
 * @LastEditors: xuxingwang
 */

const route = [
  {
    path: '/zt_dataReport',
    alwaysShow: true,
    redirect: '/zt_dataReport/zt_report_params',
    name: '',
    meta: {
      title: '数据报表',
      icon: 'icon31',
      identify: 'zt:zt_dataReport'
    },
    children: [
      // 维护报表参数
      {
        path: 'zt_report_params',
        name: 'ZtDataReportList',
        meta: {
          title: '报表参数',
          icon: '',
          identify: 'zt:zt_dataReport:zt_report_params'
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/zt_report_params/index.vue'
          )
      },
      {
        path: 'zt_params_edit_price/:id/:type',
        name: 'ZtParamsEditPrice',
        meta: {
          title: '维护参数-价格',
          icon: '',
          identify: 'zt:zt_dataReport:edit_params_price',
          activeMenu: ['/zt_dataReport/zt_report_params'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/params_edit_price/index.vue'
          ),
        hidden: true
      },
      {
        path: 'zt_params_edit_Type/:id/:type',
        name: 'ZtParamsEditType',
        meta: {
          title: '维护参数-商品类型',
          icon: '',
          identify: 'zt:zt_dataReport:edit_params_type',
          activeMenu: ['*'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/params_goods_type/index.vue'
          ),
        hidden: true
      },
      // 订单返利报表
      {
        path: 'zt_report_table',
        name: 'ZtDataReportTable',
        meta: {
          title: '订单返利报表',
          icon: '',
          identify: 'zt:zt_dataReport:zt_report_table'
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/zt_report_table/index.vue'
          )
      },
      {
        path: 'zt_params_ladder_rule/:id/:type',
        name: 'ZtParamsLadderRule',
        meta: {
          title: '维护参数-阶梯规则设置',
          icon: '',
          identify: 'zt:zt_dataReport:ladder_rule',
          activeMenu: ['/zt_dataReport/zt_report_params'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/params_ladder_rule/index.vue'
          ),
        hidden: true
      },
      {
        path: 'zt_params_ladder_rule_detail/:id/:name/:fatherId/:fatherType',
        name: 'ZtParamsLadderRuleDetail',
        meta: {
          title: '阶梯规则设置-阶梯规则',
          icon: '',
          identify: 'zt:zt_dataReport:ladder_rule_detail',
          activeMenu: ['/zt_dataReport/zt_report_params'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/params_ladder_rule/ladder_rule_detail/index.vue'
          ),
        hidden: true
      },

      {
        path: 'zt_params_sales_promotion/:id/:type',
        name: 'ZtParamsSalesPromotion',
        meta: {
          title: '维护参数-促销活动设置',
          icon: '',
          identify: 'zt:zt_dataReport:sales_promotion',
          activeMenu: ['/zt_dataReport/zt_report_params'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/params_sales_promotion/index.vue'
          ),
        hidden: true
      },
      {
        path: 'zt_params_sales_promotion_detail/:id/:name/:fatherId/:fatherType',
        name: 'ZtParamsSalesPromotionDetail',
        meta: {
          title: '促销活动设置-促销活动',
          icon: '',
          identify: 'zt:zt_dataReport:sales_promotion_detail',
          activeMenu: ['/zt_dataReport/zt_report_params'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/params_sales_promotion/sales_promotion_detail/index.vue'
          ),
        hidden: true
      },
      {
        path: 'zt_params_bussiness_and_yl_corresponding/:id/:type',
        name: 'ZtParamsBussinessAndYLCorresponding',
        meta: {
          title: '维护参数-商家品与以岭品对应关系',
          icon: '',
          identify: 'zt:zt_dataReport:bussiness_and_yl_corresponding',
          activeMenu: ['*'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/params_bussiness_and_yl_corresponding/index.vue'
          ),
        hidden: true
      },
      {
        path: 'zt_params_member_rebate/:id/:type',
        name: 'ZtParamsMemberRebate',
        meta: {
          title: '维护参数-会员返利金额',
          icon: '',
          identify: 'zt:zt_dataReport:member_rebate',
          activeMenu: ['/zt_dataReport/zt_report_params'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/params_member_rebate/index.vue'
          ),
        hidden: true
      },
      // 订单返利总表
      {
        path: 'zt_report_summary_table',
        name: 'ZtDataReportSummaryTable',
        meta: {
          title: '返利总表',
          icon: '',
          identify: 'zt:zt_dataReport:zt_report_summary_table'
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/zt_report_summary_table/index.vue'
          )
      },
      {
        path: 'summary_detail_report_flow/:id/:status?',
        name: 'SummaryDetailReportFlow',
        meta: {
          title: '返利总表-流向返利报表',
          icon: '',
          identify: 'zt:zt_dataReport:summary_detail_report_flow',
          activeMenu: ['/zt_dataReport/zt_report_summary_table'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/zt_report_summary_table/summary_detail_report_flow/index.vue'
          ),
        hidden: true
      },
      {
        path: 'summary_detail_report_b2b/:id/:status?',
        name: 'SummaryDetailReportB2b',
        meta: {
          title: '返利总表-b2b订单返利明细',
          icon: '',
          identify: 'zt:zt_dataReport:summary_detail_report_b2b',
          activeMenu: ['/zt_dataReport/zt_report_summary_table'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/zt_report_summary_table/summary_detail_report_b2b/index.vue'
          ),
        hidden: true
      },
      // 流向返利报表
      {
        path: 'zt_report_flow_table',
        name: 'ZtDataReportFlowTable',
        meta: {
          title: '流向返利报表',
          icon: '',
          identify: 'zt:zt_dataReport:zt_report_flow_table'
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/zt_report_flow_table/index.vue'
          )
      },
      // 进销存中心
      {
        path: 'invoicing_system',
        name: 'InvoicingSystem',
        meta: {
          title: '进销存中心',
          icon: '',
          identify: 'zt:zt_dataReport:invoicing_system'
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/invoicing_system/index.vue'
          )
      },
      // 进销存中心报表-调整库存
      {
        path: 'invoicing_edit_detail/:id',
        name: 'InvoicingEditDetail',
        meta: {
          title: '调整库存',
          icon: '',
          identify: 'zt:zt_dataReport:invoicing_edit_detail',
          activeMenu: ['/zt_dataReport/invoicing_system'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/invoicing_system/invoicing_edit_detail/index.vue'
        ),
        hidden: true
      },
      // 进销存中心报表-已返利库存
      {
        path: 'invoicing_buy_detail/:eid/:goodsInSn/:purchaseChannel/:ylGoodsId',
        name: 'InvoicingBuyDetail',
        meta: {
          title: '已返利库存',
          icon: '',
          identify: 'zt:zt_dataReport:invoicing_buy_detail',
          activeMenu: ['/zt_dataReport/invoicing_system'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/admin/views_zt/zt_dataReport/invoicing_system/invoicing_buy_detail/index.vue'
        ),
        hidden: true
      }
    ]
  }
];

export default route;
