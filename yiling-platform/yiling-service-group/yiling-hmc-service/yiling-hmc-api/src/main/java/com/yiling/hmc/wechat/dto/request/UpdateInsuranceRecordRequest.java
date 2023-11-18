package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 创建参保记录
 *
 * @author fan.shen
 * @date 2022-03-31
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateInsuranceRecordRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    private Long  id;

    /**
     * 当前终止时间
     */
    private Date currentEndTime;


}
