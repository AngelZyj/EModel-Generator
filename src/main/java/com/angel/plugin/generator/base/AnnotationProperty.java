package com.angel.plugin.generator.base;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author angel
 * @description 注释的属性
 * @date 2019/11/4 20:22
 */
@Data
public class AnnotationProperty {

    /**
     * 注解所在包名
     */
    private String packageName;

    /**
     * 注解名字
     */
    private String name;

    /**
     * 注解参数
     */
    private Map<String, String> fieldMap;

    /**
     * 注解中额外引入的类，需要写全路径
     */
    private List<String> annotationImportClass;

    public String toAnnotation() {
        StringBuilder builder = new StringBuilder();
        builder.append("@").append(name);
        if (fieldMap != null) {
            builder.append("(");
            for (Map.Entry<String,String> entry: fieldMap.entrySet()) {
                if (entry.getValue().startsWith("{")) {
                    builder.append(entry.getKey()).append(" = ").append(entry.getValue());
                } else {
                    builder.append(entry.getKey()).append(" = \"").append(entry.getValue()).append("\"");
                }
                builder.append(", ");
            }
            builder.delete(builder.length() - 2, builder.length() - 1);
            builder.append(")");
        }
        return builder.toString();
    }
}
