package org.socialforce.app.impl.preset;

import org.socialforce.app.Scene;
import org.socialforce.app.SceneValue;

/**
 * Created by Whatever on 2016/8/31.
 */
public class SVSR_ExitWidth implements SceneValue {
    protected double width;
    public void setWidth(double width){
        this.width = width;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName() {

    }

    @Override
    public Comparable getValue() {
        return null;
    }

    @Override
    public void apply(Scene scene) {

    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
