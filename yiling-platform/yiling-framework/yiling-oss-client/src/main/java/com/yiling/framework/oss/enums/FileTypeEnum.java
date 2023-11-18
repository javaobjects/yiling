package com.yiling.framework.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型枚举
 * 注意：要求每一种文件定义一种类型，不要共用一个枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/14
 */
@Getter
@AllArgsConstructor
public enum FileTypeEnum {
    //商品图片
    GOODS_PICTURE("goodsPicture", "商品图片", BucketEnum.PUBLIC),
    //企业资质图片
    ENTERPRISE_CERTIFICATE("enterpriseCertificate", "企业资质图片", BucketEnum.PRIVATE),
    //文件下载中心
    FILE_EXPORT_CENTER("fileExportCenter", "文件下载中心", BucketEnum.PRIVATE),
    //文件导入结果
    EXCEL_IMPORT_RESULT("excelImportResult", "文件导入结果", BucketEnum.PRIVATE),
    //banner图片
    BANNER_PICTURE("bannerPicture", "banner图片", BucketEnum.PUBLIC),
    //订单一级商收货回执单信息
    ORDER_RECEIVE_ONE_RECEIPT("orderReceiveOneReceipt","订单收货回执单",BucketEnum.PRIVATE),
    //订单购销合同附件
    ORDER_SALES_CONTRACT("orderContract", "订单购销合同附件", BucketEnum.PRIVATE),
    //富文本编辑器中的图片及附件
    RICH_TEXT_EDITOR_FILE("richTextEditorFile", "富文本编辑器中的图片及附件", BucketEnum.PUBLIC),
    //采购流向
    ERP_PURCHASE_FLOW("erpPurchaseFlow", "采购流向文件", BucketEnum.PUBLIC),
    //销售流向
    ERP_SALE_FLOW("erpSaleFlow", "销售流向文件", BucketEnum.PUBLIC),
    //门店销售流向
    ERP_SHOP_SALE_FLOW("erpShopSaleFlow", "门店销售流向", BucketEnum.PUBLIC),
    //库存流向
    ERP_GOODS_BATCH_FLOW_PATH("goodsBatchFlow", "库存流向文件", BucketEnum.PUBLIC),
    // app安装包的维护文件
    APP_INSTALL_PACKAGE("appInstallPackage", "移动端安装包文件", BucketEnum.PUBLIC),
    // b2b企业收款账户
    B2B_RECEIPT_ACCOUNT("b2bReceiptAccount", "b2b企业收款账户文件", BucketEnum.PUBLIC),
    //会员权益图标
    MEMBER_EQUITY_PICTURE("memberEquityPicture", "会员权益图标", BucketEnum.PUBLIC),
    //赠品库图片
    MARKETING_GOODS_GIFT_PICTURE("marketingGoodsGiftPicture", "赠品库图片", BucketEnum.PUBLIC),
    //店铺logo
    SHOP_LOGO_PICTURE("shopLogoPicture", "店铺Logo", BucketEnum.PUBLIC),

    //会员购买推广分享二维码
    TASK_MEMBER_SHARE("taskMemberShare","会员购买推广分享二维码",BucketEnum.PUBLIC),

    // 用户身份证正面照
    ID_CARD_FRONT_PHOTO("idCardFrontPhoto", "用户身份证正面照", BucketEnum.PRIVATE),
    // 用户身份证反面照
    ID_CARD_BACK_PHOTO("idCardBackPhoto", "用户身份证反面照", BucketEnum.PRIVATE),
    // 协议附件
    AGREEMENT_ATTACHMENT("agreementAttachment", "协议附件", BucketEnum.PUBLIC),

    CONTENT_COVER("contentCover", "内容封面", BucketEnum.PUBLIC),

    HEALTH_BACK_IMAGE("healthBackImage", "健康测评分享背景图", BucketEnum.PUBLIC),

    HEALTH_COVER_IMAGE("healthCoverImage", "健康测评封面", BucketEnum.PUBLIC),

    HEALTH_COVER_ANSWER("healthCoverAnswer", "健康测评答题须知", BucketEnum.PUBLIC),

    HEALTH_EVALUATE("healthEvaluate", "健康测评", BucketEnum.PUBLIC),

    ADVERTISEMENT_PIC("advertisementPic","药加险广告图片",BucketEnum.PUBLIC),

    BARCODE_PIC("barCodePic","C端保单兑付订单条形码",BucketEnum.PUBLIC),

    PRESCRIPTION_PIC("prescriptionPic","C端商家后台上传处方图片",BucketEnum.PUBLIC),

    //es词库文件
    ES_WORD_DIC("es","es词库文件",BucketEnum.PUBLIC),
    // 组合包活动图片
    COMBINATION_PACKAGE_PICTURE("combinationPackagePicture","组合包活动图片",BucketEnum.PUBLIC),
    // 专场活动图片
    SPECIAL_ACTIVITY_PICTURE("specialActivityPicture","专场活动图片",BucketEnum.PUBLIC),

    // 手写签名
    HAND_SIGNATURE_PICTURE("handSignaturePicture","手写签名",BucketEnum.PUBLIC),

    // 订单票据
    ORDER_RECEIPTS("orderReceipts","订单票据",BucketEnum.PUBLIC),

    // 电子保单
    POLICY_FILE("policyFile","电子保单",BucketEnum.PUBLIC),


    // 会议活动封面图
    MEETING_BACKGROUND_PIC("meetingBackgroundPic","会议活动封面图",BucketEnum.PUBLIC),

    //内容管理-视频
    VEDIO_CONTENT("vedioContent","视频内容",BucketEnum.PUBLIC),

    DOCUMENT_FILE_URL("documentFileUrl","文献",BucketEnum.PRIVATE),

    FEEDBACK_PIC("feedbackPic","用户反馈图片",BucketEnum.PUBLIC),
    //问题库图片信息
    QUESTION_RESOURCE_PICTURE("questionResourcePicture","问题库图片视频",BucketEnum.PUBLIC),
    QUESTION_REPLY_RESOURCE_PICTURE("questionReplyResourcePicture","问题回复图片视频",BucketEnum.PUBLIC),

    //健康中心 活动码
    ACTIVITY_DOCTOR_QRCODE("activityDoctorQrcode","活动码",BucketEnum.PUBLIC),

    LOTTERY_ACTIVITY_FILE("lotteryActivityFile","抽奖活动海报",BucketEnum.PUBLIC),

    // 会员背景图、点亮、熄灭图
    MEMBER_BACKGROUND_PICTURE("memberBackgroundPicture","会员背景图",BucketEnum.PUBLIC),
    MEMBER_LIGHT_PICTURE("memberLightPicture","会员点亮熄灭图",BucketEnum.PUBLIC),

    GB_PROCESS_SUBMIT_SUPPORT_PICTURE("gbProcessSubmitSupportPicture","团购提报流程证据文件",BucketEnum.PUBLIC),

    // 契约锁合同
    QIYUESUO_CONTRACT("qiyuesuoContract", "契约锁合同", BucketEnum.PUBLIC),
    //随货同行单
    ACCOMPANYING_BILL_PIC("accompanyingBillPic","随货同行单", BucketEnum.PUBLIC),

    // HMC 活动照片
    HMC_ACTIVITY_PIC("hmcActivityPic", "HMC 活动照片", BucketEnum.PUBLIC),

    // 积分兑换消息图标
    INTEGRAL_MESSAGE_PICTURE("integralMessagePicture","积分兑换消息图标",BucketEnum.PUBLIC),

    // 月流向上传文件
    FLOW_MONTH_UPLOAD_FILE("flowMonthUploadFile","月流向上传",BucketEnum.PRIVATE),
    //补传月流向
    FIX_FLOW_MONTH_UPLOAD_FILE("flowMonthFixFile","补传月流向",BucketEnum.PRIVATE),


    // 销量申诉上传
    SALES_APPEAL_UPLOAD_FILE("SalesAppealUploadFile","销量申诉上传",BucketEnum.PUBLIC),

    // 窜货申报上传
    FLEEING_GOODS_UPLOAD_FILE("FleeingGoodsUploadFile","窜货申报上传",BucketEnum.PRIVATE),

    // 销量申诉确认上传
    SALES_APPEAL_CONFIRM_UPLOAD_FILE("SalesAppealConfirmUploadFile","销量申诉上传",BucketEnum.PUBLIC),

    // 销量申诉附件上传
    SALES_APPEAL_APPENDIX_UPLOAD_FILE("SalesAppealAppendixUploadFile","销量申诉附件上传",BucketEnum.PUBLIC),

    // 窜货申报附件上传
    FLEEING_GOODS_APPENDIX_UPLOAD_FILE("FleeingGoodsAppendixUploadFile","窜货申报附件上传",BucketEnum.PUBLIC),

    // 窜货申报确认上传
    FLEEING_GOODS_CONFIRM_UPLOAD_FILE("FleeingGoodsConfirmUploadFile","窜货申报确认上传",BucketEnum.PRIVATE),

    SALE_TARGET_RESOLVE_FILE("saleTargetResolveFile","销售指标分解模板",BucketEnum.PUBLIC),

    // 团购费用申请上传
    GB_FEE_APPLICATION_UPLOAD_FILE("GbFeeApplicationUploadFile","团购费用申请上传文件",BucketEnum.PUBLIC),

    HMC_DIAGNOSIS("hmcDiagnosis","C端问诊文件",BucketEnum.PRIVATE),

    // B2B开屏位图片
    B2B_OPEN_POSITION("b2bOpenPosition", "B2B开屏位图片", BucketEnum.PUBLIC),

    // 院外药店关系表单附件上传文件
    HOSPITAL_DRUGSTORE_REL_APPENDIX_UPLOAD_FILE("hospitalDrugstoreRelAppendixUploadFile", "院外药店绑定关系表单附件上传文件", BucketEnum.PUBLIC),
    ;

    private String type;
    private String description;
    private BucketEnum bucketEnum;

    public static FileTypeEnum getByType(String type) {
        for (FileTypeEnum e : FileTypeEnum.values()) {
            if (e.getType().equalsIgnoreCase(type)) {
                return e;
            }
        }
        return null;
    }
}
