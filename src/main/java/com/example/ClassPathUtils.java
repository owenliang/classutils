package com.example;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ClassPathUtils {
    /**
     * 在classpath下列举basePackage包下的所有class
     * @param basePackage
     */
    public List<String> listClasses(String basePackage) {
        String[] classPathList = System.getProperty("java.class.path").split(File.pathSeparator);
        List<String> classList = new ArrayList<>();
        for (String classPath : classPathList) {
            File file = new File(classPath);
            if (file.isDirectory()) {
                listDirectory(file, "", classList);
            } else if (file.getName().endsWith(".jar")) {
                listJar(file, classList);
            }
        }
        return classList.stream().filter(name -> name.startsWith(basePackage+".")).collect(Collectors.toList());
    }

    private void listDirectory(File directory, String className, List<String> classList) {
        if (!className.isEmpty()) {
            className = className + ".";
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                listDirectory(file, className+file.getName(), classList);
            } else if (file.getName().endsWith(".class")) {
                classList.add(className+file.getName().substring(0,file.getName().length()-6));
            }
        }
    }

    private void listJar(File file, List<String> classList) {
        try (JarFile jar = new java.util.jar.JarFile(file)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().substring(0, entry.getName().length()-6).replace('/','.');
                    classList.add(className);
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}