package com.yiling.framework.common.dubbo.internal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.dubbo.metadata.definition.TypeDefinitionBuilder;
import org.apache.dubbo.metadata.definition.builder.TypeBuilder;
import org.apache.dubbo.metadata.definition.model.TypeDefinition;

import cn.hutool.core.lang.tree.Tree;

import static org.apache.dubbo.common.utils.TypeUtils.getRawClass;
import static org.apache.dubbo.common.utils.TypeUtils.isClass;
import static org.apache.dubbo.common.utils.TypeUtils.isParameterizedType;

public class HuToolTreeTypeBuilder implements TypeBuilder {

    @Override
    public boolean accept(Type type, Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        // 只有Tree才应用HuToolTreeTypeBuilder**
        return Tree.class.isAssignableFrom(clazz);
    }

    @Override
    public TypeDefinition build(Type type, Class<?> clazz, Map<Class<?>, TypeDefinition> typeCache) {
        if (!(type instanceof ParameterizedType)) {
            return new TypeDefinition(clazz.getName());
        }

        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArgs = parameterizedType.getActualTypeArguments();
        int actualTypeArgsLength = actualTypeArgs == null ? 0 : actualTypeArgs.length;

        String mapType = type.toString();

        TypeDefinition typeDefinition = new TypeDefinition(mapType);

        for (int i = 0; i < actualTypeArgsLength; i++) {
            Type actualType = actualTypeArgs[i];
            TypeDefinition item = null;
            Class<?> rawType = getRawClass(actualType);
            if (isParameterizedType(actualType)) {
                // Nested collection or map.
                item = TypeDefinitionBuilder.build(actualType, rawType, typeCache);
            } else if (isClass(actualType)) {
                item = TypeDefinitionBuilder.build(null, rawType, typeCache);
            }
            typeDefinition.getItems().add(item);
        }

        return typeDefinition;
    }
}