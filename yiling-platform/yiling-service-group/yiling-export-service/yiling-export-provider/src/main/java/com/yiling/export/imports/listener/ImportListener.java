package com.yiling.export.imports.listener;

public interface ImportListener<T>{
   boolean checkObjAllFieldsIsNull(T object);
}
