package cn.icodening.console.server.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Arrays;

/**
 * @author icodening
 * @date 2021.06.09
 */
public class BeanPropertyUtil {

    private BeanPropertyUtil() {
    }

    public static String[] getNullFieldNames(Object src) {
        if (src == null) {
            return new String[0];
        }
        BeanWrapper wrapper = new BeanWrapperImpl(src);
        final PropertyDescriptor[] pds = wrapper.getPropertyDescriptors();
        return Arrays.stream(pds)
                .filter(pd -> wrapper.getPropertyValue(pd.getName()) == null)
                .map(FeatureDescriptor::getName)
                .distinct()
                .toArray(String[]::new);
    }
}
