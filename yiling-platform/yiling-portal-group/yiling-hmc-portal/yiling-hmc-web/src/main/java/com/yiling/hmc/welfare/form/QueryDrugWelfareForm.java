package com.yiling.hmc.welfare.form;

import cn.hutool.core.util.StrUtil;
import com.yiling.framework.common.util.YlStrUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * 用药福利计划详情入参
 *
 * @Description
 * @Author fan.shen
 * @Date 2022-09-27
 */
@Data
@ToString
@ApiModel(value = "用药福利计划详情入参", description = "用药福利计划详情入参")
@Slf4j
public class QueryDrugWelfareForm implements Serializable {

    private static final long serialVersionUID = -7722430332896313642L;


    /**
     * 二维码值 qt:1_sEId:2_sUId:1_wfId:1
     */
    @ApiModelProperty(value = "二维码值")
    private String qr;

    /**
     * 福利计划id
     */
    @ApiModelProperty(value = "福利计划id")
    private Long welfareId;

    /**
     * 员工企业id
     */
    @ApiModelProperty(value = "企业id")
    private Long sellerEId;

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private Long sellerUserId;


    /**
     * 转换二维码参数
     */
    public void convertQrCodeValue() {
        if (StrUtil.isNotBlank(qr)) {
            // qt:1_sEId:2_sUId:1_wfId:1
            Map<String, String> paramMap = YlStrUtils.dealParam(qr);
            if (paramMap.containsKey("wId")) {
                this.welfareId = Long.valueOf(paramMap.get("wId"));
            }
            if (paramMap.containsKey("eId")) {
                this.sellerEId = Long.valueOf(paramMap.get("eId"));
            }
            if (paramMap.containsKey("uId")) {
                this.sellerUserId = Long.valueOf(paramMap.get("uId"));
            }
        }
    }


}
