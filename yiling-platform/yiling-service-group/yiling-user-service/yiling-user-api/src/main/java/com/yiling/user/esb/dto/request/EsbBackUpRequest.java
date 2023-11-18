package com.yiling.user.esb.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;

@Data
public class EsbBackUpRequest extends BaseRequest {
  /**
   * 表前缀
   */
  private   String prefix;
  /**
   *生成几月，0是当前月
   */
  private Integer offsetMonth;
  /**
   * 指定年月格式 yyyymm
   */
  private Integer yearMonthFormat;

}
