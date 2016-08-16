package org.socialforce.drawer;

import org.socialforce.entity.impl.Circle2D;
import org.socialforce.entity.Point;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by Ledenel on 2016/8/12.
 */
public class SolidCircle2DDrawer extends AwtDrawer2D {
    public Circle2D getCircle() {
        return circle;
    }

    public void setCircle(Circle2D circle) {
        this.circle = circle;
    }

    public SolidCircle2DDrawer(Graphics2D device, Circle2D circle) {
        super(device);
        this.circle = circle;
    }

    Circle2D circle;



    /**
     * render the shape on the @code {Graphics2D} with color built-in.
     *
     * @param g the graphics
     */
    @Override
    public void renderShape(Graphics2D g) {
        Point point = circle.getReferencePoint();
        double left = point.getX() - circle.getRadius();
        double top = point.getY() - circle.getRadius();
        double d = 2 * circle.getRadius();
        g.fill(new Ellipse2D.Double(left,top,d,d));
    }
}