package com.john.edu.factory;

import com.john.edu.annotation.Autowired;
import com.john.edu.annotation.Transactional;
import com.john.edu.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:wenwei
 * @date:2020/03/05
 * @description:
 */
public class AnnotationActor {

    public   Map<String,Object> map = new HashMap<>();  // 存储对象



    public  boolean isConponentAnnotion( Class<?> aClass){
        try {


            Annotation[] annotations = aClass.getAnnotations();
            for (Annotation annotation : annotations) {
               if (annotations[0].annotationType().getName().equals("com.john.edu.annotation.Service")){
                   Object object = aClass.newInstance();
                   map.put(aClass.getSimpleName(),object);
               }
            }
            injectProperties(aClass);
            injectMethods(aClass);


            Class<? extends Class> aClass1 = aClass.getClass();
            Object object = aClass.newInstance();
            if(object instanceof Autowired){
                return true;
            }
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return false;
    }

    public  void injectProperties(Class<?> aClass){
        try {
            Field[] declaredFields = aClass.getDeclaredFields();

            for (Field declaredField : declaredFields) {
                if(declaredField.isAnnotationPresent(Autowired.class)){
                    //Autowired annotation = declaredField.getAnnotation(Autowired.class);
                    Method[] methods = aClass.getMethods();
                    Object object = aClass.newInstance();
                    for (int i = 0; i < methods.length; i++) {

                        Method method = methods[i];
                        if(method.getName().equalsIgnoreCase("set" + declaredField.getName())) {  // 该方法就是 setAccountDao(AccountDao accountDao)

                               method.invoke(object,map.get(declaredField.getName()));
                        }
                    }
                }
//                annotatedType.
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
    public  void injectMethods(Class<?> aClass){
         ProxyFactory proxyFactory = (ProxyFactory) BeanFactory.getBean("proxyFactory");
        try {
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                boolean annotationPresent = declaredMethod.isAnnotationPresent(Transactional.class);
                if(annotationPresent){
                    String name = StringUtils.toCamelKey(aClass.getSimpleName());
                    Object object = map.get(name);
                    map.put(name,proxyFactory.getJdkProxy(BeanFactory.getBean(name)));

                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }






}
