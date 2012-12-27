package net.jonasbandi;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class EarScanner {

    public static final String PREFIX_FILTER = "cpscanner";
    private EarPathHelper _earPathHelper;

    public EarScanner() {
        _earPathHelper = new EarPathHelper();
    }

    public EarScanner(EarPathHelper earPathHelper) {
        _earPathHelper = earPathHelper;
    }

    public List<String> getClassesAnnotatedWith(Class annotationClass) throws IOException {
        String applicationXmlResourceUrl = _earPathHelper.getApplicationXmResourcelUrl();
        String earPath = _earPathHelper.getEarPathFromResourceUrl(applicationXmlResourceUrl);
        boolean isExploded = _earPathHelper.isExplodedEar(earPath);

        ArrayList<ClassFile> classFilesContainingAnnotation = null;
        if (!isExploded){
            classFilesContainingAnnotation = scanJarFileForAnnotation(earPath, annotationClass);
        }
        else {
            classFilesContainingAnnotation = scanDirectoryForAnnotation(earPath, annotationClass);
        }

        ArrayList<String> classNames = new ArrayList<String>();
        for (ClassFile classFile : classFilesContainingAnnotation){
            classNames.add(classFile.getName());
        }

        return classNames;
    }

    private ArrayList<ClassFile> scanJarFileForAnnotation(String earPath, Class annotationClass) throws IOException {
        ArrayList<ClassFile> classFilesContainingAnnotation = new ArrayList<ClassFile>();
        System.out.println("Scanning EAR: " + earPath);
        JarFile jarFile = new JarFile(earPath);
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()){
            JarEntry jarEntry = entries.nextElement();
            String[] pathElements = jarEntry.getName().split("/");

            if (!pathElements[0].endsWith(".jar") || !pathElements[0].startsWith(PREFIX_FILTER)){
                System.out.println("Skipping: " + jarEntry.getName());
                continue;
            }

            if (pathElements.length > 1){
                if(jarEntry.getName().endsWith(".class")){
                    System.out.println("Scanning class in exploded JAR: " + jarEntry.getName());
                    DataInputStream dis = new DataInputStream(new BufferedInputStream(jarFile.getInputStream(jarEntry)));
                    ClassFile classFile = new ClassFile(dis);
                    if (checkClassFileContainsAnnotation(classFile, annotationClass))
                    {
                        classFilesContainingAnnotation.add(classFile);
                    }
                }
            }
            else {
                System.out.println("Scanning JAR: " + jarEntry.getName());
                try {
                    InputStream in = new DataInputStream(new BufferedInputStream(jarFile.getInputStream(jarEntry)));
                    JarInputStream jarInputStream = new JarInputStream(in);

                    JarEntry innerJarEntry = null;
                    while((innerJarEntry = jarInputStream.getNextJarEntry()) != null){
                        if (innerJarEntry.getName().endsWith(".class"))
                        {
                            FilterInputStream innerInputStream = new FilterInputStream(jarInputStream) {
                                public void close() throws IOException { /* ignore the close */ }
                            };

                            DataInputStream dis = new DataInputStream(new BufferedInputStream(innerInputStream));
                            ClassFile classFile = new ClassFile(dis);

                            if (checkClassFileContainsAnnotation(classFile, annotationClass))
                            {
                                classFilesContainingAnnotation.add(classFile);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return classFilesContainingAnnotation;
    }

    private ArrayList<ClassFile> scanDirectoryForAnnotation(String earPath, Class annotationClass) throws IOException {
        ArrayList<ClassFile> classFilesContainingAnnotation = new ArrayList<ClassFile>();
        System.out.println("Scanning exploded EAR: " + earPath);
        File earDirectory = new File(earPath);
        for(File child : earDirectory.listFiles()){
            if (child.isDirectory() && child.getName().endsWith(".jar") && child.getName().startsWith(PREFIX_FILTER)){
                System.out.println("Scanning exploded JAR: " + child.getAbsolutePath());
                scanDirectory(earPath, annotationClass, classFilesContainingAnnotation);
            }
        }

        return classFilesContainingAnnotation;
    }

    private void scanDirectory(String earPath, Class annotationClass, ArrayList<ClassFile> collectedClassFiles) throws IOException {
        File earDirectory = new File(earPath);
        for(File child : earDirectory.listFiles()){
            if (child.isDirectory() ){
                System.out.println("Scanning directory: " + child.getAbsolutePath());
                scanDirectory(child.getAbsolutePath(), annotationClass, collectedClassFiles);
            }
            else if (child.getName().endsWith(".class")){
                System.out.println("Scanning class: " + child.getName());
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(child.getAbsolutePath())));
                ClassFile classFile = new ClassFile(dis);

                if (checkClassFileContainsAnnotation(classFile, annotationClass))
                {
                    collectedClassFiles.add(classFile);
                }
            }
        }
    }

    private boolean checkClassFileContainsAnnotation(ClassFile classFile, Class annotationClass) {
        AnnotationsAttribute visible = (AnnotationsAttribute) classFile.getAttribute(AnnotationsAttribute.visibleTag);
        if (visible != null){
            for (Annotation ann : visible.getAnnotations())
            {
                System.out.println("@" + ann.getTypeName());
                if (ann.getTypeName().equals(annotationClass.getName()))
                    return true;
            }
        }
        return false;
    }

    static class EarPathHelper{

        String getApplicationXmResourcelUrl(){
            String resourcePath = getClass().getClassLoader().getResource("META-INF/application.xml").getPath();
            System.out.println("Resource path of application.xml: " + resourcePath);
            return resourcePath;
        }

        String getEarPathFromResourceUrl(String resourceUrl){
            String resourceUrlWithoutProtocol = resourceUrl.substring(resourceUrl.indexOf(":") + 1);
            String pathToEar = resourceUrlWithoutProtocol.substring(0, resourceUrlWithoutProtocol.indexOf(".ear") + 4);
            return pathToEar;
        }

        boolean isExplodedEar(String earPath){
            File earFile = new File(earPath);
            return earFile.isDirectory();
        }
    }
}
