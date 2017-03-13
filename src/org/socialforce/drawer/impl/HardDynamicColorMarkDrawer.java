package org.socialforce.drawer.impl;

import org.socialforce.geom.impl.Point2D;
import org.socialforce.model.Agent;

import java.awt.*;

/**
 * Created by Ledenel on 2017/3/3.
 */
public class HardDynamicColorMarkDrawer extends DynamicColorDrawer {
    public HardDynamicColorMarkDrawer(Graphics2D device) {
        super(device);
    }

    // @Override
    public Color currentColor(Agent agent) {
        if(agent.getPath().getGoal().equals(new Point2D(26.0,2.5))) return Color.red;
        if(agent.getPath().getGoal().equals(new Point2D(33.5,14))) return Color.green;
        if(agent.getPath().getGoal().equals(new Point2D(14.0,21.5))) return Color.blue;
        return Color.BLACK;
        //         if(entity instanceof Agent && ((Agent) entity).getPath().getGoal().equals(new Point2D(33.5,14))) ((AwtDrawer2D)drawer).setColor(Color.green);
        //         if(entity instanceof Agent && ((Agent) entity).getPath().getGoal().equals(new Point2D(14.0,21.5))) ((AwtDrawer2D)drawer).setColor(Color.blue);
    }
}