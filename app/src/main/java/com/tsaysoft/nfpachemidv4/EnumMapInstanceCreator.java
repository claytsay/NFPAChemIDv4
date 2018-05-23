package com.tsaysoft.nfpachemidv4;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;
import java.util.EnumMap;

/**
 * A class designed to help solve <code>Gson</code>'s problems with <code>EnumMap</code>s.
 * <p>
 *     However, it doesn't seem to work.
 *     Taken from StackOverflow. See <code>@see</code> for the hyperlink.
 * </p>
 *
 * @see <a href="https://stackoverflow.com/questions/16127904/gson-fromjson-return-linkedhashmap-instead-of-enummap">StackOverFlow: GSON fromJson return LinkedHashMap instead of EnumMap</a>
 *
 * @author Perception
 * @version 00.02.00
 * @deprecated Doesn't seem to work.
 *
 */
public class EnumMapInstanceCreator<K extends Enum<K>, V> implements
        InstanceCreator<EnumMap<K, V>> {

    private final Class<K> enumClazz;

    public EnumMapInstanceCreator(final Class<K> enumClazz) {
        super();
        this.enumClazz = enumClazz;
    }

    @Override
    public EnumMap<K, V> createInstance(final Type type) {
        return new EnumMap<K, V>(enumClazz);
    }
}
