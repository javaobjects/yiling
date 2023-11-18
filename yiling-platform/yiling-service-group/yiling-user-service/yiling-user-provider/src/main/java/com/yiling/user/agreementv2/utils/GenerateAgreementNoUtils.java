package com.yiling.user.agreementv2.utils;

import java.util.Optional;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.user.agreementv2.enums.AgreementTypeEnum;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 生成协议编号工具类
 *
 * @author: lun.yu
 * @date: 2022-03-04
 */
@Slf4j
public class GenerateAgreementNoUtils {

    /**
     * 自动生成协议编号：
     * 		编号规则，字段不可编辑
     * 		十二位协议编号由三部分组成，第一部分为2位协议级别代码，见说明1；第二部分为10位区分码，见说明2。
     * 		说明1：协议级别代码
     * 		YJ：一级协议                    EJ：二级协议
     * 		SY：商业供货协议                 LS：临时协议
     * 	    KA: KA连锁协议	              DL：代理商协议
     * 	    取自协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议
     * 		说明2：区分码
     * 		第3-8位为协议登入年、月份，如201804为2018年4月份登入的协议，第9-12位为同协议级别当月登入的流水号。
     * 		例：YJ2018040051，为2018年4月登入的第51份一级协议。
     */
    public static String generateNo(Integer agreementType, Integer serialNo){
        StringBuilder sb = new StringBuilder();
        // 前两位协议级别
        AgreementTypeEnum agreementTypeEnum = Optional.ofNullable(AgreementTypeEnum.getByCode(agreementType)).orElseThrow(() -> new BusinessException(ResultCode.PARAM_MISS));
        sb.append(agreementTypeEnum.getNo());
        // 获取年月
        int year = DateUtil.thisYear();
        int month = DateUtil.date().monthStartFromOne();
        sb.append(year);
        sb.append(String.format("%02d",month));
        // 流水号
        String format = String.format("%04d", serialNo);
        sb.append(format);
        log.info("协议类型：{}，生成协议编号：{}", agreementType, sb);

        return sb.toString();
    }


}
