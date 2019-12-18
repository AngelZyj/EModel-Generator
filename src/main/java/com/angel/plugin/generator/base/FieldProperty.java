package com.angel.plugin.generator.base;

import lombok.Data;

import java.util.List;

/**
 * @author angel
 * @description 成员属性定义
 * @date 2019/11/4 19:56
 */
@Data
public class FieldProperty {

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 注释
     */
    private String comment;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 注解
     */
    private List<AnnotationProperty> annotationProperties;

    public String toField() {
        StringBuilder builder = new StringBuilder();
        if (annotationProperties != null) {
            for (AnnotationProperty annotation : annotationProperties) {
                builder.append("\t").append(annotation.toAnnotation()).append("\n");
            }
        }
        if (comment != null) {
            builder.append("\t").append("/**").append("\n");
            builder.append("\t").append(" * ").append(comment);
            builder.append("\t").append("*/");
        }
        builder.append("\t").append("private ").append(fieldType).append(" ").append(fieldName);
        if (defaultValue != null) {
            builder.append(" = ").append(defaultValue);
        }
        builder.append(";");
        return builder.toString();
    }
}
