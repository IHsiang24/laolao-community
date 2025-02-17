package com.xiangkai.community.alpha.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ClassLoaderExample extends ClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(ClassLoaderExample.class);

    private final String classPath;

    public ClassLoaderExample(String classPath) {
        this.classPath = classPath;
    }

    @Override
    final protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] classData = loadClassData(name);
            if (classData != null) {
                return defineClass(name, classData, 0, classData.length);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return super.findClass(name);
    }

    private byte[] loadClassData(String name) throws IOException {
        String filePath = classPath + File.separatorChar + name.replace('.', File.separatorChar) + ".class";
        File file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                return bos.toByteArray();
            }
        }
        return null;
    }
}
