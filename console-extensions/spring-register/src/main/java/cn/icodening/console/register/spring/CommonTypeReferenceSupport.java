package cn.icodening.console.register.spring;

import cn.icodening.console.common.entity.RateLimitEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author icodening
 * @date 2021.06.12
 */
public class CommonTypeReferenceSupport extends TypeReference<List<Object>> {

    private final Class<?> type;

    public CommonTypeReferenceSupport(Class<?> clz) {
        ArrayList clzs = new ArrayList<>(1);
        try {
            clzs.add(clz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        this.type = clzs.getClass();
    }

    @Override
    public Type getType() {
        return type;
    }

    public static void main(String[] args) throws Exception {
        Class aClass = Class.forName(RateLimitEntity.class.getName());
        System.out.println(new ArrayList<RateLimitEntity>().getClass());
        CommonTypeReferenceSupport objectCommonTypeReferenceSupport = new CommonTypeReferenceSupport(aClass);
        System.out.println(objectCommonTypeReferenceSupport.getType());
    }
}
