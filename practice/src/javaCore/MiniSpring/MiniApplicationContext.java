package javaCore.MiniSpring;


import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javaCore.MiniSpring.CustomAnnotations.*;


public class MiniApplicationContext {
    private Map<Class<?>, Object> beanMap = new HashMap<>();

    private void scan(String baseStart) throws URISyntaxException {

        String scanPath = baseStart.replace('.', '/');


        URL resource = getClass().getClassLoader().getResource(scanPath);
        File directory = new File(resource.toURI());
        for (File file: directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = baseStart + '.' + file.getName().replace(".class", "");
                try {
                    Class<?> class1 = Class.forName(className);
                    if (class1.isAnnotationPresent(Component.class)) {
                        beanMap.put(class1, null);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void injectDependencies(Object instance) throws IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field :fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                Object dependency = beanMap.get(field.getType());
                field.set(instance, dependency);
            }
        }
    }

    private void instantiateBeans() throws Exception {
        for (Map.Entry<Class<?>, Object> entry:beanMap.entrySet()) {
            Class<?> class1 = entry.getKey();
            Object instance = class1.getDeclaredConstructor().newInstance();
            beanMap.put(class1, instance);
            injectDependencies(instance);
            if (instance instanceof InitializingBean) {
                ((InitializingBean)instance).afterPropertiesSet();
            }
        }
    }

    public <T> T getBean(Class<T> type) {
        return type.cast(beanMap.get(type));
    }

    public MiniApplicationContext(String basePackage) throws Exception {
        scan(basePackage);
        instantiateBeans();
    }


}
