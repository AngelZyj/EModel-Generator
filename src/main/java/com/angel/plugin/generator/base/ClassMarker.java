package com.angel.plugin.generator.base;

import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class ClassMarker {


    public void make(List<ClassProperty> classProperties,boolean isUseLombok) throws IOException {
        if (CollectionUtils.isNotEmpty(classProperties)) {
            for (ClassProperty classProperty : classProperties) {
                String clazz = make(classProperty,isUseLombok);
                String path = Constants.project+File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator
                        + classProperty.getPackageName().replace(".", File.separator)
                        +File.separator;
                File file = new File(path+classProperty.getClassName()+".java");
                if (!file.exists()) {
                    new File(path).mkdirs();
                    file.createNewFile();
                }

                FileUtils.writeStringToFile(file, clazz, "utf-8");
            }
        }
    }

    public String make(ClassProperty classProperty,boolean isUseLombok) {
        StringBuilder headBuilder = new StringBuilder();
        headBuilder.append("package ").append(classProperty.getPackageName()).append(";\n");
//        headBuilder.append("import java.lang.*;\n");
        headBuilder.append("import java.util.*;\n");

        StringBuilder bodyBuilder = new StringBuilder();
        if (isUseLombok) {
            headBuilder.append("import lombok.Data;\n");
            bodyBuilder.append("@Data\n");
        }

        if (CollectionUtils.isNotEmpty(classProperty.getAnnotationProperties())) {
            for (AnnotationProperty annotation : classProperty.getAnnotationProperties()) {
                headBuilder.append("import ").append(annotation.getPackageName()).append(".").append(annotation.getName()).append(";\n");
                bodyBuilder.append(annotation.toAnnotation());
            }
        }
        bodyBuilder.append("public class ").append(classProperty.getClassName());

        if (Objects.nonNull(classProperty.getParentClassName())) {
            headBuilder.append("import ").append(classProperty.getParentPackageName()).append(".").append(classProperty.getParentClassName()).append(";\n");
            bodyBuilder.append(" extends ").append(classProperty.getParentClassName());
        }

        bodyBuilder.append(" {\n\n");

        if (CollectionUtils.isNotEmpty(classProperty.getFiledProperties())) {
            StringBuilder getSetBuilder = new StringBuilder();
            for (FieldProperty filed : classProperty.getFiledProperties()) {
                List<AnnotationProperty> fieldAnnotations = filed.getAnnotationProperties();
                if (CollectionUtils.isNotEmpty(fieldAnnotations)) {
                    for (AnnotationProperty fieldAnnotation : fieldAnnotations) {
                        if (!fieldAnnotation.getPackageName().startsWith("java.lang") || !fieldAnnotation.getPackageName().startsWith("java.util")) {
                            headBuilder.append("import ").append(fieldAnnotation.getPackageName()).append(".").append(fieldAnnotation.getName()).append(";\n");
                        }
                        if (CollectionUtils.isNotEmpty(fieldAnnotation.getAnnotationImportClass())) {
                            for (String importClass:fieldAnnotation.getAnnotationImportClass()) {
                                headBuilder.append("import ").append(importClass).append(";\n");
                            }
                        }
                    }

                }
                bodyBuilder.append(filed.toField()).append("\n\n");
                if (!isUseLombok) {
                    getSetBuilder.append("\t").append("public ").append(filed.getFieldType()).append(" get")
                            .append(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, filed.getFieldName()))
                            .append("() {\n")
                            .append("\t\t").append("return ").append(filed.getFieldName()).append(";")
                            .append("\n").append("}\n\n");
                }
            }
            bodyBuilder.append(getSetBuilder);
        }
        bodyBuilder.append("}");
        return removeDuplicateString(headBuilder).append("\n").append(bodyBuilder).toString();
    }

    private StringBuilder removeDuplicateString(StringBuilder builder) {
        String[] importStr = builder.toString().split("\n");
        Set<String> importSet = new LinkedHashSet<>(Arrays.asList(importStr));
        StringBuilder res = new StringBuilder();
        importSet.forEach(s -> res.append(s).append("\n"));
        return res;
    }
}
