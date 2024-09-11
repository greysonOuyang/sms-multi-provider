package org.moretea.sms.api.util;

import java.lang.reflect.Constructor;

/**
 * 反射
 * <p>
 * Author: greyson
 * Email: ouyangguanling@ssc-hn.com
 * Date: 2024/8/15
 * Time: 17:16
 */

public class ReflectionUtil {
    /**
     * 根据类名创建对象实例。
     *
     * @param className 要创建的对象的类名（全限定类名）
     * @return 创建的对象实例
     * @throws ClassNotFoundException 如果找不到指定的类
     * @throws NoSuchMethodException 如果类没有无参构造函数
     * @throws IllegalAccessException 如果构造函数不可访问
     * @throws InstantiationException 如果类不能实例化
     * @throws java.lang.reflect.InvocationTargetException 如果构造函数抛出异常
     */
    public static Object createInstance(String className) throws ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException, InstantiationException,
            java.lang.reflect.InvocationTargetException {
        // 加载类
        Class<?> clazz = Class.forName(className);

        // 获取无参构造函数
        Constructor<?> constructor = clazz.getConstructor();

        // 创建对象实例
        return constructor.newInstance();
    }
}
