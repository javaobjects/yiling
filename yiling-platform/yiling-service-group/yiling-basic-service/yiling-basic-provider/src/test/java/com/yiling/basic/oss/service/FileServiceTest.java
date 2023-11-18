package com.yiling.basic.oss.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2023/3/18
 */
@Slf4j
public class FileServiceTest extends BaseTest {

    @Autowired
    FileService fileService;

    @Test
    public void get() {
        FileInfo fileInfo = fileService.get(FileTypeEnum.ACCOMPANYING_BILL_PIC, "dev/accompanyingBillPic/2023/02/01/dc7b4732693d4b9ea855143660de921d.jpg");
        log.info("fileInfo = {}", fileInfo);
    }
}
