package org.socialforce.model.impl;

import org.socialforce.geom.Affection;
import org.socialforce.geom.RotatablePhysicalEntity;
import org.socialforce.geom.Velocity;
import org.socialforce.geom.impl.Ellipse2D;
import org.socialforce.geom.impl.Moment2D;
import org.socialforce.geom.impl.Vector2D;
import org.socialforce.geom.impl.Velocity2D;
import org.socialforce.model.*;

/**
 * Created by Administrator on 2017/9/15 0015.
 */
public class DoorTurnRegulation extends TypeMatchRegulation<DoorTurn,Agent>{


    public DoorTurnRegulation(Class<DoorTurn> doorTurnClass, Class<Agent> agentClass, SimpleForceModel model) {
        super(doorTurnClass, agentClass, model);
    }


    @Override
    public Affection getForce(DoorTurn doorTurn, Agent agent) {
        if ((agent.getPhysicalEntity() instanceof RotatablePhysicalEntity)&&(agent.getPhysicalEntity().intersects(doorTurn.getView()))){
            double angle = doorTurn.getAngle()-((Ellipse2D)agent.getPhysicalEntity()).getAngle();
            if(Math.abs(doorTurn.getAngle()-((Ellipse2D)agent.getPhysicalEntity()).getAngle())<Math.abs(Math.PI+doorTurn.getAngle()-((Ellipse2D)agent.getPhysicalEntity()).getAngle()))
                angle = angle+Math.PI;
            return new Moment2D(angle*400);
            //return new Moment2D( -5.15*angle*angle+9.78*angle-3.09);
        }
        return new Moment2D(0);

    }
}