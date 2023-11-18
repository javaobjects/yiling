package com.yiling.open.erp.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * erp对接请求次数阈值信息
 *
 * @author: houjie.sun
 * @date: 2022/3/15
 */
@Data
@Accessors(chain = true)
public class ErpMonitorCountInfoDetailBO implements java.io.Serializable{

    private static final long serialVersionUID = 2803167114857915647L;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 请求关闭次数
     */
    private Integer monitorCount;


    public ErpMonitorCountInfoDetailBO(){
        this.taskNo = "";
        this.monitorCount = 0;
    }

}
