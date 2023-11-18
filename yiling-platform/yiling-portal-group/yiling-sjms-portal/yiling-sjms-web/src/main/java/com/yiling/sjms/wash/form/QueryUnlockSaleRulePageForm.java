package com.yiling.sjms.wash.form;

import java.util.Date;

import cn.hutool.core.date.DateUtil;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUnlockSaleRulePageForm extends QueryPageListForm {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "规则编码")
    private String code;

    @ApiModelProperty(value = "规则名称")
    private String name;

    @ApiModelProperty(value = "状态：0-开启1-关闭")
    private Integer status;

    @ApiModelProperty(value = "开始的最后修改时间")
    private Date startUpdate;

    @ApiModelProperty(value = "结束的最后修改时间")
    private Date endUpdate;

    public Date getStartUpdate() {
         if(startUpdate!=null){
               return DateUtil.beginOfDay(startUpdate);
         }
         return null;
    }

    public Date getEndUpdate() {
        if(endUpdate!=null){
            return DateUtil.endOfDay(endUpdate);
        }
        return null;
    }


}
