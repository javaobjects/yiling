package com.yiling.export.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.export.BaseTest;
import com.yiling.export.export.model.FlowSaleExcelModel;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;

/**
 * @author fucheng.bai
 * @date 2023/3/24
 */
public class BigDataExportTest extends BaseTest {

    @DubboReference
    private FlowSaleApi flowSaleApi;

    @Autowired
    private FileService fileService;

    @Test
    public void BigDataTest() {
        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();

        String fileName = "/Users/baifc/Downloads/" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, FlowSaleExcelModel.class).build();
        try {
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();

            int current = 1;
            int size = 2000;
            while (true) {
                request.setCurrent(current);
                request.setSize(size);
//                Page<FlowSaleDTO> page = flowSaleApi.page(request);
//                if (page.getRecords() == null || page.getRecords().size() == 0) {
//                    break;
//                }
//                List<FlowSaleExcelModel> list = PojoUtils.map(page.getRecords(), FlowSaleExcelModel.class);
                List<FlowSaleExcelModel> list = new ArrayList<>();
                excelWriter.write(list, writeSheet);
                if (list.size() < size) {
                    break;
                }
                System.out.println("写入数据：" + current + "页");
                current++;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

    }

    @Test
    public void BigFileUploadTest() throws Exception {
        String fileName = "/Users/baifc/Downloads/1679650450710.xlsx";
        File file = new File(fileName);

        FileInfo fileInfo = fileService.upload(FileUtil.getInputStream(file), "1679650450710.xlsx", FileTypeEnum.FILE_EXPORT_CENTER);
        System.out.println(fileInfo.getKey());
    }




    public static void main(String[] args) throws Exception {
        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();

        String fileName = "/Users/baifc/Downloads/" + System.currentTimeMillis() + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName, FlowSaleExcelModel.class).build();
        try {
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();

            int current = 1;
            int size = 2000;
            while (true) {
                request.setCurrent(current);
                request.setSize(size);
                List<FlowSaleExcelModel> list = new ArrayList<>();
                excelWriter.write(list, writeSheet);
                if (list.size() < size) {
                    break;
                }
                current++;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }



}
