package com.angel.plugin.generator.base;

import lombok.Data;

import java.util.List;

/**
 * @author angel
 * @description 类属性
 * @date 2019/11/4 20:03
 */
@Data
public class ClassProperty {

    /**
     * 包名
     */
    private String packageName;

    /**
     * 类名
     */
    private String className;

    /**
     * 父类包名
     */
    private String parentPackageName;

    /**
     * 父类类名
     */
    private String parentClassName;

    /**
     * 类属性
     */
    private List<FieldProperty> filedProperties;

    /**
     * 注解
     */
    private List<AnnotationProperty> annotationProperties;
}
