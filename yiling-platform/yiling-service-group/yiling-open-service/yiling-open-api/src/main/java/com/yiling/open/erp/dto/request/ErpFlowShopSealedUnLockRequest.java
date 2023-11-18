package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpFlowShopSealedUnLockRequest extends BaseRequest {

    /**
     * 商业公司id
     */
    private Long rkSuId;

    /**
     * 解封月份-开始
     */
    private String startMonth;

    /**
     * 解封月份-结束
     */
    private String endMonth;

    public ErpFlowShopSealedUnLockRequest(){
        this.rkSuId = 0L;
        this.startMonth = "";
        this.endMonth = "";
    }

    public void buildStartMonth(String month){
        if(StrUtil.isBlank(month)){
            this.startMonth = "";
        }
        DateTime dateTime = DateUtil.beginOfMonth(DateUtil.parse(month, "yyyy-MM"));
        this.startMonth = DateUtil.format(dateTime, "yyyy-MM-dd HH:mm:ss");
    }

    public void buildEndMonth(String month){
        if(StrUtil.isBlank(month)){
            this.endMonth =  "";
        }
        DateTime dateTime = DateUtil.endOfMonth(DateUtil.parse(month, "yyyy-MM"));
        this.endMonth =  DateUtil.format(dateTime, "yyyy-MM-dd HH:mm:ss");
    }
}
