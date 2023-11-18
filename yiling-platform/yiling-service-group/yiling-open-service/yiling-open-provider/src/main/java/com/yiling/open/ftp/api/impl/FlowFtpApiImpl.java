package com.yiling.open.ftp.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.ftp.api.FlowFtpApi;
import com.yiling.open.ftp.dto.LocalCompareDTO;
import com.yiling.open.ftp.service.FlowFtpClientService;

/**
 * @author: shuang.zhang
 * @date: 2022/3/4
 */
@DubboService
public class FlowFtpApiImpl implements FlowFtpApi {

    @Autowired
    private FlowFtpClientService flowFtpClientService;

    @Override
    public void readFlowExcel() {
        flowFtpClientService.readFlowExcel();
    }

    @Override
    public void localCompare(LocalCompareDTO localCompareDTO) {
        flowFtpClientService.localCompare(localCompareDTO);
    }
}
