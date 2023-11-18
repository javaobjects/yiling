package com.yiling.bi.resource.handler;

import java.math.BigDecimal;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.yiling.bi.resource.excel.InputLsflCoverExcel;
import com.yiling.bi.resource.excel.InputLsflRecordExcel;

/**
 * @author: shuang.zhang
 * @date: 2022/9/26
 */
public class UploadCoverResourceReaderHandler implements RowHandler {

    private List<InputLsflCoverExcel> list;

    public UploadCoverResourceReaderHandler(List<InputLsflCoverExcel> list) {
        this.list = list;
    }

    @Override
    public void handle(int sheetIndex, long rowIndex, List<Object> rowList) {
        if (rowIndex != 0&&rowIndex != 1) {
            InputLsflCoverExcel inputLsflCoverExcel = new InputLsflCoverExcel();
            inputLsflCoverExcel.setProvince(String.valueOf(rowList.get(0)!=null?rowList.get(0):""));
            inputLsflCoverExcel.setBzCode(String.valueOf(rowList.get(1)!=null?rowList.get(1):""));
            inputLsflCoverExcel.setBzName(String.valueOf(rowList.get(2)!=null?rowList.get(2):""));
            inputLsflCoverExcel.setCustomerType(String.valueOf(rowList.get(3)!=null?rowList.get(3):""));
            inputLsflCoverExcel.setNkaZb(String.valueOf(rowList.get(4)!=null?rowList.get(4):""));
            inputLsflCoverExcel.setQdType(String.valueOf(rowList.get(5)!=null?rowList.get(5):""));
            inputLsflCoverExcel.setXyType(String.valueOf(rowList.get(6)!=null?rowList.get(6):""));
            inputLsflCoverExcel.setWlType(String.valueOf(rowList.get(7)!=null?rowList.get(7):""));
            inputLsflCoverExcel.setWlBreed(String.valueOf(rowList.get(8)!=null?rowList.get(8):""));
            inputLsflCoverExcel.setStoresNum(tranBigDecimal(rowList.get(9)));
            inputLsflCoverExcel.setQuarter1CoverNum(tranBigDecimal(rowList.get(10)));
            inputLsflCoverExcel.setQuarter1CoverRate(tranBigDecimal(rowList.get(11)));
            inputLsflCoverExcel.setQuarter2CoverNum(tranBigDecimal(rowList.get(12)));
            inputLsflCoverExcel.setQuarter2CoverRate(tranBigDecimal(rowList.get(13)));
            inputLsflCoverExcel.setQuarter3CoverNum(tranBigDecimal(rowList.get(14)));
            inputLsflCoverExcel.setQuarter3CoverRate(tranBigDecimal(rowList.get(15)));
            inputLsflCoverExcel.setQuarter4CoverNum(tranBigDecimal(rowList.get(16)));
            inputLsflCoverExcel.setQuarter4CoverRate(tranBigDecimal(rowList.get(17)));
            list.add(inputLsflCoverExcel);
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
