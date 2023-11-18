package com.yiling.admin.cms.content.vo;

import java.util.List;

import lombok.Data;

/**
 * @author gaoxinlei
 */
@Data
public class HospitalDeptVO  {


    private Integer id;

    private String label;

    private List<HospitalDeptVO> children;

}
