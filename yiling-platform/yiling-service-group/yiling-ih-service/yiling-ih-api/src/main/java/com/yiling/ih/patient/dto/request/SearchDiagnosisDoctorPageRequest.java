package com.yiling.ih.patient.dto.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * 找医生列表
 *
 * @author fan.shen
 * @date 2022/8/23
 */
@Data
public class SearchDiagnosisDoctorPageRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 搜索内容
     */
    private String content;

    /**
     * 一级科室id
     */
    private Integer departmentParentId;

    /**
     * 二级科室id
     */
    private Integer departmentId;

    /**
     * 排序
     */
    private String sort;

    /**
     * 服务类型
     */
    private List<String> type;

    /**
     * 职称
     */
    private List<String> profession;

    /**
     * 医院级别
     */
    private String hospitalLevel;

    /**
     * 医院性质
     */
    private List<String> hospitalType;

    /**
     * 每页显示条数，默认10
     */
    private Integer size = 10;

    /**
     * 当前页，默认1
     */
    private Integer current = 1;

    public <T> Page<T> getPage() {
        return new Page<>(this.current, this.size);
    }

}
