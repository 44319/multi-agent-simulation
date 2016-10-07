package org.socialforce.app.impl;

import org.junit.Before;
import org.junit.Test;
import org.socialforce.container.impl.LinkListPool;
import org.socialforce.geom.impl.Segment2D;
import org.socialforce.model.InteractiveEntity;
import org.socialforce.container.Pool;
import org.socialforce.geom.impl.Box2D;
import org.socialforce.geom.impl.Circle2D;
import org.socialforce.geom.impl.Point2D;
import org.socialforce.model.impl.Wall;

import static org.junit.Assert.*;

/**
 * Created by Ledenel on 2016/8/21.
 */
public class LinkListPoolTest {
    Pool<InteractiveEntity> entities;

    Circle2D circle2D;

    @Before
    public void setUp() throws Exception {
        entities = new LinkListPool<>();
        entities.add(new Wall(new Box2D(-1, -1, 2, 2)));
        entities.add(new Wall(new Segment2D(new Point2D(3, 3), new Point2D(4, 4))));
        Circle2D circle2D = new Circle2D();
        this.circle2D = new Circle2D();
        circle2D.setRadius(4);
        this.circle2D.setRadius(4);
        entities.add(new Wall(circle2D));
    }


    @Test
    public void testSelect() throws Exception {
        Iterable<InteractiveEntity> iterable = entities.select(new Point2D(0,0));
        int count = 0;
        for (InteractiveEntity entity : iterable) {
            count++;
        }
        assertEquals(2,count);
        for (InteractiveEntity entity : iterable) {
            count++;
        }
        assertEquals(4,count);
    }

    @Test
    public void intersectSelect() throws Exception{
        entities.add(new Wall(new Segment2D(new Point2D(2,2), new Point2D(20,20))));
        int count = 0;
        for(InteractiveEntity i:entities.select(circle2D)){
            count++;
        }
        assertEquals(3,count);
    }
}