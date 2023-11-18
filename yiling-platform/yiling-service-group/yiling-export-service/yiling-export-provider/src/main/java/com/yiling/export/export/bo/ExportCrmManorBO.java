package com.yiling.export.export.bo;

import lombok.Data;

import java.util.Date;

@Data
public class ExportCrmManorBO {
    private Long id ;
    /**
     * 辖区编码
     */
    private String manorNo;

    /**
     * 辖区名称
     */
    private String name;

    /**
     * 机构数量
     */
    private Integer agencyNum;

    /**
     * 品类数量
     */
    private Integer categoryNum;


    /**
     * 更新人
     */
    private Long updateUser;
    private String updateUserName;

    /**
     * 更新日期
     */
    private Date updateTime;
}
