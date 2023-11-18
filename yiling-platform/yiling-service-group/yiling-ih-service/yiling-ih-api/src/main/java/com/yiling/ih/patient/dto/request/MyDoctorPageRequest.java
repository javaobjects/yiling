package com.yiling.ih.patient.dto.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 我的医生列表
 *
 * @author fan.shen
 * @date 2023/5/16
 */
@Data
public class MyDoctorPageRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 用户id
     */
    private Integer fromUserId;

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
