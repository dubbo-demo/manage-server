package com.myph.manage.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 密码工具类,提供侵犯隐私的直接读取filed的能力.
 * 
 * @since version1.0
 */
public class BeanUtils {

    protected static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    private BeanUtils() {
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException {
        Field field = getDeclaredField(object, fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(Object object, String fieldName, Object value) throws NoSuchFieldException {
        Field field = getDeclaredField(object, fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    public static Field getDeclaredField(Object object, String fieldName) throws NoSuchFieldException {
        Assert.notNull(object);
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 循环向上转型,获取类的DeclaredField.
     */
    @SuppressWarnings("rawtypes")
    public static Field getDeclaredField(Class clazz, String fieldName) throws NoSuchFieldException {
        Assert.notNull(clazz);
        Assert.hasText(fieldName);
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + fieldName);
    }

    /**
     * @Title: getUUID
     * @Description: 获得UUID
     * @return: String
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 对一个bean进行深度复制，所有的属性节点全部会被复制【注意：1、类中的属性和getter/setter方法需要对应；2、 Bean需要有默认的构造方法】
     * 
     * @param source
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T deepCopyBean(T source) {
        if (source == null) {
            return null;
        }
        try {
            if (source instanceof Collection) {
                return (T) deepCopyCollection((Collection) source);
            }
            if (source.getClass().isArray()) {
                return (T) deepCopyArray(source);
            }
            if (source instanceof Map) {
                return (T) deepCopyMap((Map) source);
            }
            if (source instanceof Date) {
                return (T) new Date(((Date) source).getTime());
            }
            if (source instanceof Timestamp) {
                return (T) new Timestamp(((Timestamp) source).getTime());
            }
            // 基本类型直接返回原值
            if (source.getClass().isPrimitive() || source instanceof String || source instanceof Boolean
                    || Number.class.isAssignableFrom(source.getClass())) {
                return source;
            }
            if (source instanceof StringBuilder) {
                return (T) new StringBuilder(source.toString());
            }
            if (source instanceof StringBuffer) {
                return (T) new StringBuffer(source.toString());
            }
            Object dest = source.getClass().newInstance();
            BeanUtilsBean bean = BeanUtilsBean.getInstance();
            PropertyDescriptor[] origDescriptors = bean.getPropertyUtils().getPropertyDescriptors(source);
            for (int i = 0; i < origDescriptors.length; i++) {
                String name = origDescriptors[i].getName();
                if ("class".equals(name)) {
                    continue;
                }

                if (bean.getPropertyUtils().isReadable(source, name) && bean.getPropertyUtils().isWriteable(dest, name)) {
                    try {
                        Object value = deepCopyBean(bean.getPropertyUtils().getSimpleProperty(source, name));
                        bean.getPropertyUtils().setSimpleProperty(dest, name, value);
                    } catch (NoSuchMethodException e) {
                    }
                }
            }
            return (T) dest;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Collection deepCopyCollection(Collection source) throws InstantiationException,
            IllegalAccessException {
        Collection dest = source.getClass().newInstance();
        for (Object o : source) {
            dest.add(deepCopyBean(o));
        }
        return dest;
    }

    private static Object deepCopyArray(Object source) throws InstantiationException, IllegalAccessException,
            ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int length = Array.getLength(source);
        Object dest = Array.newInstance(source.getClass().getComponentType(), length);
        if (length == 0) {
            return dest;
        }
        for (int i = 0; i < length; i++) {
            Array.set(dest, i, deepCopyBean(Array.get(source, i)));
        }
        return dest;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map deepCopyMap(Map source) throws InstantiationException, IllegalAccessException {
        Map dest = source.getClass().newInstance();
        for (Object o : source.entrySet()) {
            Entry e = (Entry) o;
            dest.put(deepCopyBean(e.getKey()), deepCopyBean(e.getValue()));
        }
        return dest;
    }

    /**
     * 
     * @param obj
     * @return
     * @Description:将一个 JavaBean 对象转化为一个 Map
     */
    public static Map<String, Object> transBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        return map;

    }

}
