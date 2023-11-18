package com.yiling.sjms.wash.vo;

import java.util.ArrayList;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/16
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UnlockAreaRecordLocationTreeVO {

    @ApiModelProperty(value = "选中的编码列表")
    private List<String> checkCodeList;

    @ApiModelProperty(value = "区域树信息列表")
    private List<AreaTreeVO> treeInfoList;


    public UnlockAreaRecordLocationTreeVO(List<AreaTreeVO> treeInfoList) {
        this.checkCodeList = new ArrayList<>();
        this.treeInfoList = treeInfoList;
    }

    @Data
    public static class AreaTreeVO {
        /**
         * 区域编码
         */
        @ApiModelProperty(value = "省/市/区编码")
        private String code;

        /**
         * 区域名称
         */
        @ApiModelProperty(value = "省/市/区名称")
        private String name;

        /**
         * 上级区域编码
         */
        @ApiModelProperty(value = "上级区域编码")
        private String parentCode;

        /**
         * 排序优先级
         */
        @ApiModelProperty(value = "排序优先级")
        private Integer priority;

        /**
         * 岗位/上级主管岗位名称
         */
        @ApiModelProperty(value = "岗位/上级主管岗位名称")
        private String postName;

        /**
         * 代表/上级主管姓名
         */
        @ApiModelProperty(value = "代表/上级主管姓名")
        private String empName;

        /**
         * 省编码
         */
        @ApiModelProperty(value = "省编码")
        private String provinceCode;

        /**
         * 是否禁用（置灰）
         */
        @ApiModelProperty(value = "是否禁用（置灰）")
        private Boolean disabled = false;

        @ApiModelProperty(value = "是否勾选")
        private Boolean checkFlag = false;

        /**
         * 下级区域列表
         */
        @ApiModelProperty(value = "下级区域列表")
        private List<AreaTreeVO> children;
    }
}
