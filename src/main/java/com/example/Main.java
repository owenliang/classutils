package com.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClassPathUtils classPathUtils = new ClassPathUtils();
        List<String> classPaths = classPathUtils.listClasses("com.example");
        for (String classPath : classPaths) {
            System.out.println(classPath);
        }
    }
}