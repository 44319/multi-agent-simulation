package org.socialforce.model;

import org.socialforce.geom.DistancePhysicalEntity;
import org.socialforce.geom.Point;
import org.socialforce.geom.Velocity;

/**
 * Created by Whatever on 2016/11/15.
 */
public interface AgentDecorator {
    Agent createAgent(Point position, Velocity velocity, DistancePhysicalEntity shape);
}
