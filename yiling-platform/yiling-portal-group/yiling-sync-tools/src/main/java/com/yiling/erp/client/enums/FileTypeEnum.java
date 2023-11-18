package com.yiling.erp.client.enums;

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
    //库存流向
    ERP_GOODS_BATCH_FLOW_PATH("goodsBatchFlow", "库存流向文件", BucketEnum.PUBLIC),
    // 连锁门店销售流向
    ERP_SHOP_SALE_FLOW("erpShopSaleFlow", "连锁门店销售流向文件", BucketEnum.PUBLIC),
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
