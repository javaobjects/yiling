package com.yiling.dataflow.wash.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateUnlockAreaRecordRequest extends BaseRequest {

    private static final long serialVersionUID = -3608349385756289745L;

    /**
     * id
     */
    private Long id;

    /**
     * 非锁客户分类
     */
    private Integer customerClassification;

    /**
     * 品种id
     */
    private Long categoryId;

    /**
     * 品种名称
     */
    private String categoryName;


    /**
     * 销量计入类型：1-销量计入主管 2-销量计入代表
     */
    private Integer type;

    /**
     * 代表岗位代码
     */
    private String representativePostCode;

    /**
     * 代表岗位名称
     */
    private String representativePostName;

    /**
     * 代表工号
     */
    private String representativeCode;

    /**
     * 代表姓名
     */
    private String representativeName;

    /**
     * 主管岗位代码
     */
    private String executivePostCode;

    /**
     * 主管岗位名称
     */
    private String executivePostName;

    /**
     * 主管工号
     */
    private String executiveCode;

    /**
     * 主管姓名
     */
    private String executiveName;

    /**
     * 业务部门
     */
    private String department;

    /**
     * 业务省区
     */
    private String province;

    /**
     * 业务区域
     */
    private String area;

    private List<String> regionCodeList;

    private String remark;
}
