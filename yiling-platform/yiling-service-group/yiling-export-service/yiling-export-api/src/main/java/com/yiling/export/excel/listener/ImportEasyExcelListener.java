package com.yiling.export.excel.listener;

public interface ImportEasyExcelListener<T>{
   boolean checkObjAllFieldsIsNull(T object);
}
