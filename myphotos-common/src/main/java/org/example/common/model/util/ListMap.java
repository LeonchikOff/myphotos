package org.example.common.model.util;

import java.util.*;

public class ListMap<Key, Value> {
    private final Map<Key, List<Value>> mapKeyAndListValues = new LinkedHashMap<>();

    public void add(Key key, Value value) {
//        List<Value> listValues = mapKeyAndListValues.computeIfAbsent(key, k -> new ArrayList<>());
        List<Value> listValues = mapKeyAndListValues.get(key);
        if(listValues == null) {
            listValues = new ArrayList<>();
            mapKeyAndListValues.put(key, listValues);
        }
        listValues.add(value);
    }

    public Map<Key, List<Value>> toMapKeyAndListValues() {
        return Collections.unmodifiableMap(mapKeyAndListValues);
    }

}
