package com.yiling.bi.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.yiling.bi.BaseTest;
import com.yiling.bi.resource.dto.UploadResourceDTO;
import com.yiling.bi.resource.entity.UploadResourceDO;
import com.yiling.bi.resource.excel.InputLsflRecordExcel;
import com.yiling.bi.resource.handler.UploadRecordResourceReaderHandler;
import com.yiling.bi.resource.service.UploadResourceLogService;
import com.yiling.bi.resource.service.UploadResourceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/9/20
 */
@Slf4j
public class TestServiceUploadResource extends BaseTest {

    @Autowired
    private UploadResourceService uploadResourceService;

    @Autowired
    private UploadResourceLogService uploadResourceLogService;

    @Test
    public void test() {
        UploadResourceDTO uploadResourceDO = uploadResourceService.getUploadResourceByDataId("");
        genFile(uploadResourceDO.getFileStream(), new File("D:\\全国大森林导入测试.xlsx"));
    }

    @Test
    public void test2() {
        uploadResourceLogService.importInputLsflRecord("b9935323-7bc1-48aa-a35f-939f2f95a03d");
    }

    @Test
    public void test1() throws FileNotFoundException {
        InputStream input = new FileInputStream(new File("D:\\备案维度数据导出表(1).xlsx"));
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setHeadRows(1);
        List<InputLsflRecordExcel> list = new ArrayList<>();
        UploadRecordResourceReaderHandler handler = new UploadRecordResourceReaderHandler(list);
        ExcelUtil.readBySax(input, 0, handler);
    }

    /**
     * byte[] 转文件
     *
     * @param data
     * @param file
     */
    public static void genFile(byte[] data, File file) {
        if (!file.exists()) {
            FileUtil.newFile(file.getPath());
        }
        try (FileOutputStream fio = new FileOutputStream(file)) {
            fio.write(data, 0, data.length);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
