package com.yiling.export.excel;

import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.poi.excel.ExcelUtil;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.util.ExportExcelUtil;
import org.apache.commons.collections.list.AbstractLinkedList;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.export.BaseTest;
import com.yiling.export.excel.service.ExcelTaskRecordService;

import java.util.*;

/**
 * @author: shuang.zhang
 * @date: 2022/11/21
 */
public class ExcelTest extends BaseTest {

    @Autowired
    private ExcelTaskRecordService excelTaskRecordService;

    @Test
    public void test1() {
        excelTaskRecordService.syncImportExcelTask(77L);
    }

    @Test
    public void syncImportExcelTask() {
        excelTaskRecordService.syncImportExcelTask(33L);
    }

    @Test
    public void test3() {
        excelTaskRecordService.syncImportExcelTask(979L);
    }





    @Test
    public  void templeteExcelTest() throws Exception{
        List<ExportDataDTO> exportDataDTOList=new ArrayList<>();
        ExportDataDTO exportDataDTO=new ExportDataDTO();
        List<Map<String,Object>> oMap=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map map=new HashMap();
            map.put("name","冀学理1");
            map.put("age",18);
            map.put("grade",i);
            map.put("remark","垃圾"+i);
            map.put("date1",new Date());
            oMap.add(map);
        }
        exportDataDTO.setData(oMap);
        exportDataDTO.setFristRow(3);
        LinkedList linkedList=new LinkedList<>();
        linkedList.add("name");
        linkedList.add("age");
        linkedList.add("remark");
        linkedList.add("grade");
        linkedList.add("date1");
        exportDataDTO.setTempleteParamList(linkedList);
        ExportExcelUtil.listToExcelByTemplete(exportDataDTOList, ExcelType.HSSF,"templete/excel/crm_supplier_templete.xlsx");
    }
}