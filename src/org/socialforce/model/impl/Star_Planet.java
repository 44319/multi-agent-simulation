package org.socialforce.model.impl;

import org.socialforce.geom.*;
import org.socialforce.geom.impl.Circle2D;
import org.socialforce.model.Agent;
import org.socialforce.model.Model;

/**
 * Created by Whatever on 2017/3/2.
 */
public class Star_Planet extends BaseAgent implements Agent {
    /**
     * @param shape
     */
    public Star_Planet(DistancePhysicalEntity shape, Velocity velocity) {
        super(shape,velocity);
        this.view = new Circle2D(shape.getReferencePoint(),100);
    }

    /**
     * 当前this所影响的实体
     * 例如，墙会影响agent(反作用，反推)
     * @param target
     */
    @Override
    public void affect(Agent target) {
        if (this.equals(target)) {
            this.selfAffect();
        }
        else
            target.push(model.interactAffection(this, target));
    }

    @Override
    public void affectAll(Iterable<Agent> affectableAgents) {
        for(Agent agent:affectableAgents){
            affect(agent);
        }
    }


    /**
     * Planet不影响自己
     * @see Agent
     * @see Model
     */
    @Override
    public void selfAffect(){

    }
    /**
     * 获取实体的质量。
     * 通常质量位于形状的参考点上（或者是位于质心上）TODO
     *
     * @return the mass.
     */
    @Override
    public double getMass() {
        double d = physicalEntity.getBounds().getSize().length();
        return d*d*d*8;
    }

    @Override
    public Star_Planet clone() {
        return new Star_Planet(physicalEntity.clone(), physicalEntity.getVelocity().clone());
    }

    /**
     * 获取移动实体的速度。.
     *
     * @return currVelocity。 当前速度
     */
    @Override
    public Velocity getVelocity() {
        return physicalEntity.getVelocity();
    }

    @Override
    public DistancePhysicalEntity getView(){
        return view;
    }

}
