package org.socialforce.scene.impl;

import org.socialforce.scene.SceneValue;
import org.socialforce.scene.ValueSet;

import java.util.*;

/**
 * Created by Whatever on 2016/12/2.
 * 老老实实用dictionary能死系列
 */
public class SimpleValueSet implements ValueSet {
    protected Set<SceneValue> values = new HashSet<SceneValue>();

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public boolean contains(SceneValue value) {
        return values.contains(value);
    }

    public SceneValue add(SceneValue value) {
        if(values.add(value)) return value;
        else return null;
    }

    public SceneValue remove(SceneValue value) {
        if(values.remove(value)) return value;
        else return null;
    }

    public Iterator<SceneValue> iterator(){
        return values.iterator();
    }

    @Override
    public int getMaxPriority(){
        int maxPriority = Integer.MIN_VALUE;
        for (SceneValue value : values){
            if (value.getPriority()>maxPriority){
                maxPriority = value.getPriority();
            }
        }
        return maxPriority;
    }

    @Override
    public int getMinPriority(){
        int minPriority = Integer.MAX_VALUE;
        for (SceneValue value : values){
            if (value.getPriority()<minPriority){
                minPriority = value.getPriority();
            }
        }
        return minPriority;
    }
}
