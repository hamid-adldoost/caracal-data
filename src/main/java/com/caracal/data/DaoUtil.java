package com.caracal.data;

import com.caracal.data.api.DomainDto;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DaoUtil {

    public static <T extends DomainDto> T filterToEntity(Filter<T> filter) {

        try {

            Class<?> c = Class.forName(filter.getEntity().getClass().getName());
            Constructor<?> cons = c.getConstructor();
            Object object = cons.newInstance();

            Field[] fields = c.getClass().getDeclaredFields();

            for (Map.Entry<String, Object> entry : filter.getParams().entrySet())
            {
                Field f = c.getDeclaredField(entry.getKey());
                f.setAccessible(true);
                f.set(object, entry.getValue());
            }
            return (T)object;

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException e) {
            throw new RuntimeException("Dynamic Filter to Entity Exception.. ");
        }
    }

}
