package com.yiling.sjms.crm.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 CrmGoodsGroupVO
 * @描述
 * @创建时间 2023/4/12
 * @修改人 shichen
 * @修改时间 2023/4/12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmGoodsGroupVO extends BaseVO {

    /**
     * 产品组编码
     */
    @ApiModelProperty(value = "产品组编码")
    private String code;

    /**
     * 产品组名称
     */
    @ApiModelProperty(value = "产品组名称")
    private String name;

    /**
     * 状态 0 有效 1无效
     */
    @ApiModelProperty(value = "状态 0 有效 1无效")
    private Integer status;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "品类编码")
    private Date updateTime;

    /**
     * 关联商品
     */
    @ApiModelProperty(value = "关联商品")
    private List<CrmGoodsGroupRelationVO> goodsRelationList;

    /**
     * 关联部门
     */
    @ApiModelProperty(value = "关联部门")
    private List<CrmDepartmentGoodsGroupVO> departmentRelationList;
}
