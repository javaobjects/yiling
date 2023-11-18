package com.yiling.sjms.excel.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: shuang.zhang
 * @date: 2021/5/31
 */
@Data
public class SaveExcelTaskRecordForm implements java.io.Serializable  {

    private static final long serialVersionUID = 930343490738509830L;

    @ApiModelProperty(required = true,value = "ossKey")
    private String ossKey;
    /**
     * 所属月份
     */
    @ApiModelProperty(value = "所属年月")
    private String gbDetailMonth;

}
