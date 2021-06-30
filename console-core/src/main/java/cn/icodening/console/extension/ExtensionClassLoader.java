package cn.icodening.console.extension;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author icodening
 * @date 2021.05.22
 */
public class ExtensionClassLoader extends ClassLoader {

    private final List<File> classpath = new LinkedList<>();

    private List<Jar> allJars;

    private final ReentrantLock jarScanLock = new ReentrantLock();

    public ExtensionClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void addPath(File file) {
        classpath.add(file);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        List<Jar> allJars = getAllJars();
        String path = name.replace('.', '/').concat(".class");
        for (Jar jar : allJars) {
            JarEntry entry = jar.jarFile.getJarEntry(path);
            if (entry == null) {
                continue;
            }
            try {
                URL classFileUrl = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + path);
                byte[] data;
                try (final BufferedInputStream is = new BufferedInputStream(
                        classFileUrl.openStream()); final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    int ch;
                    while ((ch = is.read()) != -1) {
                        baos.write(ch);
                    }
                    data = baos.toByteArray();
                }
                Attributes mainAttributes = jar.jarFile.getManifest().getMainAttributes();
                String specTitle = mainAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
                String specVersion = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
                String specVendor = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
                String implTitle = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
                String implVersion = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
                String implVendor = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
                String pkgName = null;
                int pos = name.lastIndexOf('.');
                if (pos != -1) {
                    pkgName = name.substring(0, pos);
                }
                definePackage(pkgName, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, null);
                return defineClass(name, data, 0, data.length);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        throw new ClassNotFoundException("Can't find " + name);
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        List<URL> allResources = new LinkedList<>();
        List<Jar> allJars = getAllJars();
        for (Jar jar : allJars) {
            JarEntry entry = jar.jarFile.getJarEntry(name);
            if (entry != null) {
                allResources.add(new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name));
            }
        }

        final Iterator<URL> iterator = allResources.iterator();
        return new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public URL nextElement() {
                return iterator.next();
            }
        };
    }

    public String getJarPathByClass(String classFilePath) {
        List<ExtensionClassLoader.Jar> allJars = getAllJars();
        for (ExtensionClassLoader.Jar allJar : allJars) {
            JarFile jarFile = allJar.jarFile;
            JarEntry jarEntry = jarFile.getJarEntry(classFilePath);
            if (jarEntry != null) {
                return jarFile.getName();
            }
        }
        return null;
    }

    public List<JarFile> getLoadedJars() {
        return getAllJars()
                .stream()
                .map(jf -> jf.jarFile)
                .collect(Collectors.toList());
    }

    private List<Jar> getAllJars() {
        if (allJars == null) {
            jarScanLock.lock();
            try {
                if (allJars == null) {
                    allJars = doGetJars();
                }
            } finally {
                jarScanLock.unlock();
            }
        }

        return allJars;
    }

    private LinkedList<Jar> doGetJars() {
        LinkedList<Jar> jars = new LinkedList<>();
        for (File path : classpath) {
            if (path.exists() && path.isDirectory()) {
                String[] jarFileNames = path.list((dir, name) -> name.endsWith(".jar"));
                for (String fileName : jarFileNames) {
                    try {
                        File file = new File(path, fileName);
                        Jar jar = new Jar(new JarFile(file), file);
                        jars.add(jar);
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        return jars;
    }

    private static class Jar {
        private final JarFile jarFile;
        private final File sourceFile;

        public Jar(JarFile jarFile, File sourceFile) {
            this.jarFile = jarFile;
            this.sourceFile = sourceFile;
        }

        @Override
        public String toString() {
            return "Jar{" +
                    "jarFile=" + jarFile.getName() +
                    ", sourceFile=" + sourceFile.getName() +
                    '}';
        }
    }

}
