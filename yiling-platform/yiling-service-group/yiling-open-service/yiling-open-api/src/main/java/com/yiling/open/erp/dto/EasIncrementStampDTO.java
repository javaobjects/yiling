package com.yiling.open.erp.dto;

import java.util.Date;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/8/2
 */
@Data
public class EasIncrementStampDTO implements java.io.Serializable{

    private Long id;

    private String taskNo;

    private Long suId;

    private Date stampTime;

    private Date createTime;

    private Date updateTime;

    private Long updateUser;

    private Long createUser;


}
