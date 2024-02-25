package com.guhao.star.client;

import com.guhao.star.api.TypeFlexibleHashMapEX;


public class EntityStateEX {
    public static boolean lockonRotate() {
        return getState(EntityStateEX.LOCKON_ROTATE);
    }

    public static class StateFactor<T> implements TypeFlexibleHashMapEX.TypeKey<T> {
        private String name;
        private T defaultValue;

        public StateFactor(String name, T defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public String toString() {
            return this.name;
        }

        public T defaultValue() {
            return this.defaultValue;
        }
    }
    public static final StateFactor<Boolean> LOCKON_ROTATE = new StateFactor<>("lockonRotate", false);
    static TypeFlexibleHashMapEX<StateFactor<?>> stateMap;
    public static <T> T getState(StateFactor<T> stateFactor) {
        return stateMap.getOrDefault(stateFactor);
    }
}
