package com.gaojin.viti;

import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.google.common.collect.ImmutableSet;

import org.gradle.api.Project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Author: gaojin
 * Time: 2019-11-10 20:27
 */

public class VitiTransform extends Transform {

    public static final String FILENAME = "record.txt";

    private Project project;

    public VitiTransform(Project project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return "viti";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        // 输入类型，可以使class文件，也可以是源码文件 ，这是表示输入的class文件
        return new HashSet<QualifiedContent.ContentType>() {{
            add(QualifiedContent.DefaultContentType.CLASSES);
            add(QualifiedContent.DefaultContentType.RESOURCES);
        }};
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        // 作用范围
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        if (transformInvocation.isIncremental()) {
            System.out.println("增量编译");
        } else {
            System.out.println("非增量编译");
        }
        File dest = transformInvocation.getOutputProvider().getContentLocation("gaojin"
                , TransformManager.CONTENT_CLASS
                , ImmutableSet.of(QualifiedContent.Scope.PROJECT)
                , Format.DIRECTORY);
        Log.info("gaojin", dest.getPath());
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(dest));
            Log.info("transformInvocation.getInputs", transformInvocation.getInputs().size());
            for (TransformInput transformInput : transformInvocation.getInputs()) {
                Log.info("transformInput.getJarInputs", transformInput.getJarInputs().size());
                for (JarInput jarInput : transformInput.getJarInputs()) {
                    out.write(jarInput.getFile().getPath() + "\n");
                    JarFile jarFile = new JarFile(jarInput.getFile());
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        out.write("    " + jarEntry.getName() + "\n");
                    }
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
