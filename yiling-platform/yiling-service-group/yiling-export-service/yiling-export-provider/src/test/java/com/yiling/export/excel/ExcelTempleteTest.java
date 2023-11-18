package com.yiling.export.excel;

import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.util.ExportExcelUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.*;

public class ExcelTempleteTest {
    public static void main(String[] args)throws  Exception {
        List<ExportDataDTO> exportDataDTOList=new ArrayList<>();
        ExportDataDTO exportDataDTO=new ExportDataDTO();
        List<Map<String,Object>> oMap=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map map=new HashMap();
            map.put("name","冀学理1123123");
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
        exportDataDTOList.add(exportDataDTO);
        byte[] bytes = ExportExcelUtil.listToExcelByTemplete(exportDataDTOList, ExcelType.HSSF, "templete/excel/test.xlsx");

        File file=new File("C:\\Users\\dynabook\\Desktop\\text.xlsx");
        FileUtils.writeByteArrayToFile(file,bytes);
    }
}
