package com.yiling.b2b.admin.shop.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺楼层分页列表查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-20
 */
@Data
public class QueryShopFloorPageForm extends QueryPageListForm {

    /**
     * 楼层名称
     */
    @ApiModelProperty("楼层名称")
    private String name;

    /**
     * 楼层状态：1-启用 2-停用
     */
    @ApiModelProperty("楼层状态：1-启用 2-停用")
    private Integer status;

    /**
     * 开始创建时间
     */
    @ApiModelProperty("开始创建时间")
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @ApiModelProperty("结束创建时间")
    private Date endCreateTime;

}
