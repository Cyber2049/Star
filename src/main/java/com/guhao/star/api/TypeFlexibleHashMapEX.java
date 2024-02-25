package com.guhao.star.api;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

@SuppressWarnings("serial")
public class TypeFlexibleHashMapEX<A extends TypeFlexibleHashMapEX.TypeKey<?>> extends HashMap<A, Object> {
    final boolean immutable;

    public TypeFlexibleHashMapEX(boolean immutable) {
        this.immutable = immutable;
    }

    @SuppressWarnings("unchecked")
    public <T> T put(TypeFlexibleHashMapEX.TypeKey<T> typeKey, T val) {
        if (this.immutable) {
            throw new UnsupportedOperationException();
        }

        return (T)super.put((A) typeKey, val);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(TypeFlexibleHashMapEX.TypeKey<T> typeKey) {

        ImmutableMap.of();

        return (T)super.get(typeKey);
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(TypeFlexibleHashMapEX.TypeKey<T> typeKey) {
        return (T)super.getOrDefault(typeKey, typeKey.defaultValue());
    }

    public interface TypeKey<T> {
        public T defaultValue();
    }
}