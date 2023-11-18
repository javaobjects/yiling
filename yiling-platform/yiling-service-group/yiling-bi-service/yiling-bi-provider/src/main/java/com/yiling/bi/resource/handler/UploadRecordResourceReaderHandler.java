package com.yiling.bi.resource.handler;

import java.math.BigDecimal;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.yiling.bi.resource.excel.InputLsflRecordExcel;

/**
 * @author: shuang.zhang
 * @date: 2022/9/26
 */
public class UploadRecordResourceReaderHandler implements RowHandler {

    private List<InputLsflRecordExcel> list;

    public UploadRecordResourceReaderHandler(List<InputLsflRecordExcel> list) {
        this.list = list;
    }

    @Override
    public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
        if (rowIndex != 0) {
            InputLsflRecordExcel inputLsflRecordExcel = new InputLsflRecordExcel();
            inputLsflRecordExcel.setProvince(String.valueOf(rowList.get(0)!=null?rowList.get(0):""));
            inputLsflRecordExcel.setBzCode(String.valueOf(rowList.get(1)!=null?rowList.get(1):""));
            inputLsflRecordExcel.setBzName(String.valueOf(rowList.get(2)!=null?rowList.get(2):""));
            inputLsflRecordExcel.setCustomerType(String.valueOf(rowList.get(3)!=null?rowList.get(3):""));
            inputLsflRecordExcel.setNkaZb(String.valueOf(rowList.get(4)!=null?rowList.get(4):""));
            inputLsflRecordExcel.setQdType(String.valueOf(rowList.get(5)!=null?rowList.get(5):""));
            inputLsflRecordExcel.setXyType(String.valueOf(rowList.get(6)!=null?rowList.get(6):""));
            inputLsflRecordExcel.setWlType(String.valueOf(rowList.get(7)!=null?rowList.get(7):""));
            inputLsflRecordExcel.setWlBreed(String.valueOf(rowList.get(8)!=null?rowList.get(8):""));
            inputLsflRecordExcel.setWlCode(String.valueOf(rowList.get(9)!=null?rowList.get(9):""));
            inputLsflRecordExcel.setWlName(String.valueOf(rowList.get(10)!=null?rowList.get(10):""));
            inputLsflRecordExcel.setAccountPrice(tranBigDecimal(rowList.get(11)));
            inputLsflRecordExcel.setBasicNum(tranBigDecimal(rowList.get(12)));
            inputLsflRecordExcel.setProjectFg(tranBigDecimal(rowList.get(13)));
            inputLsflRecordExcel.setProjectOther(tranBigDecimal(rowList.get(14)));
            inputLsflRecordExcel.setTargetNum(tranBigDecimal(rowList.get(15)));
            inputLsflRecordExcel.setYearTarget(tranBigDecimal(rowList.get(16)));
            inputLsflRecordExcel.setMonth1Num(tranBigDecimal(rowList.get(17)));
            inputLsflRecordExcel.setMonth2Num(tranBigDecimal(rowList.get(18)));
            inputLsflRecordExcel.setMonth3Num(tranBigDecimal(rowList.get(19)));
            inputLsflRecordExcel.setMonth4Num(tranBigDecimal(rowList.get(20)));
            inputLsflRecordExcel.setMonth5Num(tranBigDecimal(rowList.get(21)));
            inputLsflRecordExcel.setMonth6Num(tranBigDecimal(rowList.get(22)));
            inputLsflRecordExcel.setMonth7Num(tranBigDecimal(rowList.get(23)));
            inputLsflRecordExcel.setMonth8Num(tranBigDecimal(rowList.get(24)));
            inputLsflRecordExcel.setMonth9Num(tranBigDecimal(rowList.get(25)));
            inputLsflRecordExcel.setMonth10Num(tranBigDecimal(rowList.get(26)));
            inputLsflRecordExcel.setMonth11Num(tranBigDecimal(rowList.get(27)));
            inputLsflRecordExcel.setMonth12Num(tranBigDecimal(rowList.get(28)));
            list.add(inputLsflRecordExcel);
        }
    }

    public BigDecimal tranBigDecimal(Object str){
        String big=String.valueOf(str!=null?str:"0");
        if(StrUtil.isEmpty(big)){
            return BigDecimal.ZERO;
        }
        return new BigDecimal(big);
    }
}
