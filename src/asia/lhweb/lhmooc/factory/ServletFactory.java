package asia.lhweb.lhmooc.factory;


import asia.lhweb.lhmooc.annotation.WebServlet;
import asia.lhweb.lhmooc.http.LhHttpServlet;
import asia.lhweb.lhmooc.utils.XMLParser;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author :罗汉
 * @date : 2024/3/3
 */
public class ServletFactory {
    // 定义属性 servlet容器 存放反射后生成的bean对象  比如controller service  目前放入的都是单例的 多例一般是动态生成
    public static Map<String, LhHttpServlet> servletMap = new ConcurrentHashMap<>();
    private static final ServletFactory instance = new ServletFactory();
    // 定义属性 保存要扫描的servlet包的全路径
    private List<String> servletClassFullPathList = new ArrayList<>();

    private ServletFactory() {
        init();
    }

    /**
     * 初始化方法 在这里进行servlet的扫描
     */
    private void init() {
        // 1 调用工具类得到 需要扫描的路径
        String basePackage = XMLParser.getBasePackage("LhServletConfig.xml");
        // System.out.println("需要扫描的xml配置文件basePackage"+basePackage);
        String[] basePackages = basePackage.split(",");
        // 2 遍历basePackages, 进行扫描
        if (basePackages.length > 0) {// 传入的包要>0
            for (String aPackage : basePackages) {
                // 3 扫描包 讲类的全路径封装到 servletClassFullPathList
                scanPackage(aPackage);
            }
        }
        // 将扫描到的类反射到servletMap容器
        executeInstance();
    }

    /**
     * 执行实例 将扫描到的类，在满足条件的情况下反射到servletMap  这个条件指的是是否有对应注解 例如Controller
     */
    private void executeInstance() {
        if (servletClassFullPathList.size() == 0) {// 没有扫描到这个类 不需要反射对象到servletMap
            return;
        }
        try {
            // 1 遍历
            for (String classFullPath : servletClassFullPathList) {
                Class<?> aClazz = Class.forName(classFullPath);
                // 2 判断是否有注解  有的话就进行反射添加到容器中
                if (aClazz.isAnnotationPresent(WebServlet.class)) {
                    // 3 得到类名首字母小写的beanName
                    // System.out.println("aClazz.getSimpleName()"+aClazz.getSimpleName());//TrainServletV3
                    // System.out.println("aClazz.getName()"+aClazz.getName());//lhweb.asia.LHTomCat.servlet.TrainServletV3
                    // System.out.println("aClazz.getPackage()"+aClazz.getPackage());//package lhweb.asia.LHTomCat.servlet
                    // System.out.println("aClazz.getClass()"+aClazz.getClass());//java.lang.Class
                    /**
                     *
                     * aClazz.getSimpleName().substring(1) 表示从第二个开始到后面全部
                     */
                    String servletName = "";
                    // 获取 WebServlet 注解
                    WebServlet annotation = aClazz.getAnnotation(WebServlet.class);
                    if (annotation != null && annotation.value().length() > 0) {
                        servletName = annotation.value();
                    } else {
                        // 如果注解没有值，则使用类名的小写形式作为 servlet 名称
                        String simpleServletName = aClazz.getSimpleName();
                        servletName = simpleServletName.substring(0, 1).toLowerCase() + simpleServletName.substring(1);
                    }
                    //5 获取无参构造器（共有的）
                    Constructor<?> declaredConstructor = aClazz.getDeclaredConstructor();//获取无参构造器 这里构造器
                    //6 添加对象
                    servletMap.put(servletName, (LhHttpServlet) declaredConstructor.newInstance());
                }
                // System.out.println("executeInstance中的servletMap:---------------------------------------------------" + servletMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 扫描包
     *
     * @param packageStr 包str
     */
    private void scanPackage(String packageStr) {
        // 要扫描的是这个传入的包路径下的文件
        // 此时要拿到的真实路径下的 把lhweb.asia.LHTomCat.servlet换成/把lhweb/asia/LHTomCat/servlet
        // String servletPackageStr = packageStr.replaceAll("\.", "/");
        // String servletPackageStr = packageStr.replace('.', File.separatorChar);
        // URL url = this.getClass().getClassLoader().getResource(servletPackageStr);
        URL url = this.getClass().getClassLoader().getResource(packageStr.replaceAll("\\.", "/"));
        if (url == null) {
            throw new RuntimeException("没有这个servlet,反射出错了");
        }
        System.out.println("url=" + url);
        // System.out.println("--------------------------------");
        // 根据得到的路径，对其扫描，把类的全路径保存在classFullPathList钟
        String path = url.getFile();
        // System.out.println("得到的路径path：" + path);
        // System.out.println("--------------------------------");

        // 根据操作系统不同，处理路径分隔符
        String separator = File.separator;
        if (separator.equals("\\")) {
            // Windows 系统下的处理
            System.out.println("这是windos系统");
            path = path.substring(1); // 去除开头的斜杠
        } else {
            // Linux 系统下的处理
            System.out.println("这是linux系统");
            path = path.replace("file:", ""); // 去除开头的 "file:"
        }

        // System.out.println("最后的路径："+path);
        File dir = new File(path);
        // 遍历目录 dir
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                // 如果是一个目录，需要递归处理
                scanPackage(packageStr + "." + file.getName());
            } else {
                // 这时，扫描到的文件可能是.class文件也有可能是其他文件
                // 就算是.class文件 也要判断是否需要注入到容器的问题
                // 目前先把文件的全路径都保存在集合中，后面在注入对象到容器时再处理
                String classFullPath = packageStr + "." + file.getName().replaceAll(".class", "");
                servletClassFullPathList.add(classFullPath);
            }
        }
    }





    /**
     * 获得实例
     *
     * @return {@link ServletFactory}
     */
    public static ServletFactory getInstance() {
        return instance;
    }

    /**
     * 得到servlet
     *
     * @param url url
     * @return {@link LhHttpServlet}
     */
    public LhHttpServlet getServlet(String url) {
        return servletMap.get(url);
    }

    @Override
    public String toString() {
        return "ServletFactoryV3{" +
                "servletClassFullPathList=" + servletClassFullPathList +
                '}';
    }
}
