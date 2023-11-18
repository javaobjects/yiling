package com.yiling.open.erp.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: fei.wu
 * @Email:
 * @CreateDate: 2019-11-09
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum OpenErrorCode implements IErrorCode {
    /**
     * erp同步信息失败
     */
	ERP_SYNC_ERROR(20001, "erp同步信息失败"),
	/**
	 * 缺少方法名参数
	 */
	Missing_Method(10, "缺少方法名参数"),
	/**
	 * 不存在的方法名
	 */
	Invalid_Method(11, "不存在的方法名"),
	/**
	 * 缺少数据格式参数
	 */
	Missing_Format(12, "缺少数据格式参数"),
	/**
	 * 非法数据格式
	 */
	Invalid_Format(13, "非法数据格式"),
	/**
	 * 缺少签名参数
	 */
	Missing_Signature(14, "缺少签名参数"),
	/**
	 * 非法签名
	 */
	Invalid_Signature(15, "非法签名"),
	/**
	 * 缺少SessionKey参数
	 */
	Missing_Access_Token(16, "缺少access_token参数"),
	/**
	 * 无效的SessionKey参数
	 */
	Invalid_Access_Token(17, "无效的access_token参数"),
	/**
	 * 缺少AppKey参数
	 */
	Missing_AppKey(18, "缺少AppKey参数"),
	/**
	 * 非法的AppKey参数
	 */
	Invalid_AppKey(19, "非法的AppKey参数"),
	/**
	 * 缺少时间戳参数
	 */
	Missing_Timestamp(20, "缺少时间戳参数"),
	/**
	 * 非法的时间戳参数
	 */
	Invalid_Timestamp(21, "非法的时间戳参数"),
	/**
	 * 缺少版本参数
	 */
	Missing_Version(22, "缺少版本参数"),
	/**
	 * 非法的版本参数
	 */
	Invalid_Version(23, "非法的版本参数"),
	/**
	 * 不支持的版本号
	 */
	Unsupported_Version(24, "不支持的版本号"),
	/**
	 * 缺少必选参数
	 */
	Missing_Required_Arguments(25, "缺少必选参数"),
	/**
	 * 非法的参数
	 */
	Invalid_Arguments(26, "非法的参数"),
	/**
	 * 请求被禁止
	 */
	Forbidden_Request(27, "请求被禁止"),
	/**
	 * 参数错误
	 */
	Parameter_Parameter(28, "参数错误"),
	/**
	 * 远程服务出错
	 */
	Remote_Service_Error(29, "远程服务出错"),
	/**
	 * 缺少签名方法参数
	 */
	Missing_Sign_Method(30, "缺少签名方法参数"),
	/**
	 * 非法签名方法参数
	 */
	Invalid_Sign_Method(31, "非法签名方法参数"),
	/**
	 * 找不到对应的记
	 */
	Find_Not_Value(32, "找不到对应的记录"),
	/**
	 * 过期的session参数
	 */
	Expire_Session(33, "过期的access_token参数"),
	/**
	 * 过期的session参数
	 */
	Invalid_AppSecret(34, "无效的appSecret"),
	/**
	 * 参数body数据为空
	 */
	Parameter_Body_Null(35, "参数body数据为空"),

	/**
	 * 接口请求太频繁，请求被限制
	 */
	Limit_Request(36, "接口请求太频繁，请求被限制"),
    /**
     * ERP对接企业：企业id不能为空
     */
    ENTERPRISE_ID_NULL(37, "企业id不能为空"),
    /**
     * ERP对接企业：企业信息不存在
     */
    ENTERPRISE_NOT_EXIST(38, "企业信息不存在"),
    /**
     * ERP对接企业：同步状态为已开启，对接级别、流向级别不能为空或未对接
     */
    DEPTH_ERROR(39, "同步状态为已开启，对接级别、流向级别不能同时为“未对接”"),
    /**
     * ERP对接企业：同步状态为已开启，流向级别不能为空或未对接
     */
    FLOW_ERROR(40, "同步状态为已开启，流向级别不能为空或未对接"),
    /**
     * ERP对接企业：erp对接企业信息不存在
     */
    ERP_ENTERPRISE_NOT_EXIST(41, "erp对接企业信息不存在"),
    /**
     * ERP对接企业：父类企业信息不存在
     */
    ENTERPRISE_PARENT_NOT_EXIST(42, "父类企业信息不存在"),
    /**
     * ERP对接企业：此父类企业是分公司，不能作为父类企业使用
     */
    ENTERPRISE_PARENT_ERROR(43, "此父类企业是分公司，不能作为父类企业使用"),
    /**
     * ERP对接企业：企业类型为工业的不能修改，请联系对接负责人
     */
    ENTERPRISE_TYPE_ERROR(44, "企业类型为工业的不能修改，请联系对接负责人"),
    /**
     * ERP监控：监控查询类型错误，统计次数类型不能同时选中
     */
    MONITOR_QUERY_TYPE_ERROR(45, "监控查询类型错误，统计次数类型不能同时选中"),

	/**
	 * 参数body数据为空
	 */
	QUERY_AGREEMENT_APPLY_GOODS(200001, "冲红系统---查询返利申请商品接口调用失败"),

    /**
     *采购流向数据同步错误
     */
    ERP_PURCHASE_FLOW_ERROR(200002, "采购流向数据同步错误"),

    /**
     *采购流向数据同步错误
     */
    ERP_SALE_FLOW_ERROR(200003, "销售流向数据同步错误"),

    /**
     *采购流向数据同步错误
     */
    ERP_GOODS_BATCH_ERROR(200004, "库存流向数据同步错误"),

    /**
     * 解封采购流向数据同步错误
     */
    ERP_FLOW_SEALED_PURCHASE_ERROR(200005, "解封采购流向数据同步错误"),

    /**
     * 解封销售流向数据同步错误
     */
    ERP_FLOW_SEALED_SALE_ERROR(200006, "解封销售流向数据同步错误"),

	/**
	 * 解封连锁纯销流向数据同步错误
	 */
	ERP_FLOW_SEALED_SHOP_SALE_ERROR(200007, "解封连锁纯销流向数据同步错误"),

    /**
     * eas通知接口报错
     */
    EAS_NOTIFICATION_ERROR(35, "eas通知接口报错"),

    /**
     * 本地对比并发锁超时异常
     */
    ERP_LOCALCOMPARE_MULTIT_ERROR(200007, "本地对比并发锁超时异常"),
	;


    private Integer code;
    private String message;
}
