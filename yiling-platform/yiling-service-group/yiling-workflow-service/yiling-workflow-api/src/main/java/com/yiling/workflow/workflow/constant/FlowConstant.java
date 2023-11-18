package com.yiling.workflow.workflow.constant;

/**
 * @author: gxl
 * @date: 2022/11/29
 */
public interface FlowConstant {
    /**
     * 团购id变量名
     */
    String GB_ID = "gbId";
    /**
     * form表id变量名
     */
    String FORM_ID = "formId";
    /**
     * 自动跳过节点设置属性
     */
    String FLOWABLE_SKIP_EXPRESSION_ENABLED = "_FLOWABLE_SKIP_EXPRESSION_ENABLED";

    String SKIP = "skip";
    /**
     * 市场运营部省区经理变量
     */
    String MARKET_PROVINCE_MANAGER = "marketProvinceManager";
    /**
     * 商务部省区经理变量
     */
    String COMMERCE_PROVINCE_MANAGER = "commerceProvinceManager";
    /**
     * 部门领导变量
     */
    String DEPT_LEADER = "deptLeader";
    /**
     * 部门变量
     */
    String DEPT = "dept";
    /**
     * 销售部
     */
    Long SALE_DEPT = 9739L;

    /**
     * 数字化网络营销
     */
    Long DIGIT_MARKET_DEPT = 11974L;
    /**
     * 呼吸事业部
     */
    Long BREATH_DEPT = 10515L;
    /**
     * 商务部
     */
    Long COMMERCE_DEPT = 10410L;
    /**
     * 市场部
     */
    Long MARKETING_DEPT = 12298L;
    /**
     * 事业八部
     */
    Long BUSINESS_EIGHT_DEPT = 13591L;

    /**
     * esb_organization 商务部org_id
     */
    Long COMMERCE_ORG_ID = 10410L;
    /**
     * esb_organization 区域运营管理部org_id
     */
    Long MARKET_MANAGER_ORG_ID = 12325L;
    /**
     * 呼吸事业部总监
     */
    String RESPIRATORY_BUSINESS_DEPT_DIRECTOR = "respiratoryBusinessDeptDirector";

    /**
     * 商务部副经理
     */
    String COMMERCE_DEPT_DEPUTY_MANAGER = "commerceDepartmentDeputyManager";

    /**
     * 商务部总监
     */
    String  COMMERCE_DEPT_DIRECTOR ="commerceDeptDirector";

    /**
     * 市场部总监
     */
   String  MARKETING_DIRECTOR ="marketingDirector";

    /**
     * 营销公司总经理
     */
    String MARKETING_COMPANY_GENERAL_MANAGER="marketingCompanyGeneralManager";
    /**
     * 公共事务部主任
     */
    String PUBLIC_AFFAIRS_DIRECTOR ="publicAffairsDirector";
    /**
     * 运维经理
     */
    String OPERATIONS_MANAGER = "operationsManager";
    /**
     * 财务
     */
    String FINANCE = "finance";

    String FINANCE_TASK_NAME = "财务审批";

    String USERS = "users";

    /**
     * 商业数据科
     */
    String BUSINESS_DATA_SECTION = "businessDataSection";
//===========================================================================数据审批常量-开始==========================================================
    /**
     * 数据审批-商业审批人
     */
    String DATA_APPROVE_DISTRIBUTOR_USER = "dataApproveDistributorUser";
    /**
     * 数据审批-医疗审批人
     */
    String DATA_APPROVE_HOSPITAL_USER = "dataApproveHospitalUser";
    /**
     * 数据审批-零售审批人
     */
    String DATA_APPROVE_PHARMACY_USER = "dataApprovePharmacyUser";
//===========================================================================数据审批常量-结束===========================================================


    String TOPIC_WF_ACT_FORWARD = "wf_act_forward";
    String TOPIC_WF_ACT_COMMENT = "wf_act_comment";
    /**
     * 驳回短信提醒模板
     */
    String REJECT_SMS_TEMPLATE="{}单据（编号：{}）被驳回，请登录数据洞察系统进行查看。";

    String FORWARD_NODE = "转发";
}
