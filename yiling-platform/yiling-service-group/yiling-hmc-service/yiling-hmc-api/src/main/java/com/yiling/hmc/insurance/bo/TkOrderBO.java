package com.yiling.hmc.insurance.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/7/5
 */
@Data
public class TkOrderBO implements Serializable {

    /**
     * 是否定向赔付   将会使用指定账户进行赔付传“否”
     */
    private Boolean hasPayInfo;

    /**
     * 否包含处方    是
     */
    private Boolean hasRecipel;

    /**
     * 服务入口渠道编码     泰康提供
     */
    private String sourceChannelCode;

    /**
     * 服务入口渠道名称     泰康提供
     */
    private String sourceChannelName;

    /**
     * 产品编码     泰康提供
     */
    private String productCode;

    /**
     * 子保单号     渠道传入泰康保单号
     */
    private String policyNo;

    /**
     * 渠道服务订单号
     */
    private String channelMainId;

    /**
     * 申请人姓名
     */
    private String applyUserName;

    /**
     * 申请人电话
     */
    private String applyUserPhone;

    /**
     * 申请人证件号
     */
    private String applyUserCid;

    /**
     * 申请人证件类型
     * 1 居民身份证
     * 2 护照
     * 3 台湾居民往来大陆通行证
     * 5 军官证
     * 18 港澳居民往来内地通行证
     * 50 士兵证
     * 99 其他
     */
    private Integer applyUserCidType;

    /**
     * 用药人姓名
     */
    private String patientName;

    /**
     * 用药人生日    格式yyyy-MM-dd
     */
    private Date patientBirth;

    /**
     * 用药人证件号
     */
    private String patientCid;

    /**
     * 用药人证件类型
     * 1 居民身份证
     * 2 护照
     * 3 台湾居民往来大陆通行证
     * 5 军官证
     * 18 港澳居民往来内地通行证
     * 50 士兵证
     * 99 其他
     */
    private Integer patientCidType;

    /**
     * 用药人性别    0未知 1男性 2女性
     */
    private String patientGender;

    /**
     * 用药人手机号
     */
    private String patientPhone;

    /**
     * 购药类型    4-福利购药
     */
    private Integer pharmacyType;

    /**
     * 就诊类型
     * 1-线上就诊
     * 2-线下就诊
     */
    private Integer consultationType;

    /**
     * 服务订单状态 0新订单,1审核中,2审核通过,3审核不通过,4资料问题件,11待支付,12支付成功,13支付失败,14支付取消,15支付退款,21待发货,22待收货,23签收成功,24发货失败,25拒收,26退货
     */
    private Integer status;

    /**
     * 服务单总价
     */
    private String marketPrices;

    /**
     * 下单时间    格式yyyy-MM-dd HH:mm:ss
     */
    private Date applyTime;

    /**
     * 处方信息  如果hasRecipel为true则必传
     */
    private TkRecipelInfoBO recipelInfo;

    /**
     * 购药订单集合
     */
    private List<TkOrderDetailBO> orderList;

    /**
     * 文件信息集合
     */
    private List<TkFileBO> fileVoList;
}
