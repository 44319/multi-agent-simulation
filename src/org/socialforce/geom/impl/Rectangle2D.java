package org.socialforce.geom.impl;

import org.socialforce.drawer.Drawer;
import org.socialforce.geom.Box;
import org.socialforce.geom.Point;
import org.socialforce.geom.Shape;
import org.socialforce.geom.Vector;

/**
 * 一个任意的二维矩形
 * 由中心，长，宽，及逆时针旋转角度定义。
 * 角度采用弧度制
 * Created by Whatever on 2016/10/31.
 */
public class Rectangle2D implements Shape {
    protected Point2D center;
    protected double length,weidth,angle;

    public Rectangle2D(){}

    public Rectangle2D(Point2D center,double length,double weidth,double angle){
        this.center = center;
        this.length = length;
        this.weidth = weidth;
        this.angle = angle;
    }
    /**
     * set the drawer for the drawable.
     *
     * @param drawer the drawer.
     */
    @Override
    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }
        protected Drawer drawer;
    /**
     * get the current drawer the object is using.
     *
     * @return the drawer.
     */
    @Override
    public Drawer getDrawer() {
        return drawer;
    }

    /**
     * 获取该维度实体的尺寸.
     *
     * @return 尺寸.
     */
    @Override
    public int dimension() {
        return 2;
    }

    /**
     * 检查一个点是否属于 <code>Shape</code>.
     * 实现方法是这样的，利用矢量图。以center为圆心，转成BOX2D，然后判断。
     * @param point 将被检查的点.
     * @return 如果该点是该形状上的一部分，就返回真，否则返回假.
     */
    @Override
    public boolean contains(Point point) {
        Rectangle2D rectangle2D = (Rectangle2D) clone();
        Box bound;
        double temp = angle;
        boolean result = false;
        Vector2D distance = (Vector2D) center.distanceTo(point);
        rectangle2D.spin(-temp);
        distance.spin(-temp);
        bound = rectangle2D.getBounds();
        Point2D point2D = new Point2D(center.getX()+distance.values[0],center.getY()+distance.values[1]);
        //result = bound.contains(new Point2D(center.getX()+distance.values[0],center.getY()+distance.values[1]));
        if (bound.getDistance(point2D).length()<10e-7){
            result = true;
        }
        return result;
    }

    /**
     * 获取一个点到这条直线的距离矢量.
     * 如果距离为0，就说明这个点在这个形状上.
     * 空的形状为 Double.NaN .
     * 方向指向目标点
     *
     * @param point 将被检查的点.
     * @return 该距离. 如果这个点到不了这个形状上的话，返回 Double.NaN .
     */
    @Override
    public Vector getDistance(Point point) {
        Segment2D[] bounds;
        Point2D point1,point2,point3,point4;
        Vector distance = new Vector2D(0,0),temp;
        double distanceN = Double.POSITIVE_INFINITY;
        if (contains(point)){return distance;}
        else point1 = new Point2D(center.getX()-length*Math.cos(angle)/2+weidth*Math.sin(angle)/2,center.getY()-length*Math.sin(angle)/2-weidth*Math.cos(angle)/2);
        point2 = new Point2D(center.getX()+length*Math.cos(angle)/2+weidth*Math.sin(angle)/2,center.getY()+length*Math.sin(angle)/2-weidth*Math.cos(angle)/2);
        point3 = new Point2D(center.getX()+length*Math.cos(angle)/2-weidth*Math.sin(angle)/2,center.getY()+length*Math.sin(angle)/2+weidth*Math.cos(angle)/2);
        point4 = new Point2D(center.getX()-length*Math.cos(angle)/2-weidth*Math.sin(angle)/2,center.getY()-length*Math.sin(angle)/2+weidth*Math.cos(angle)/2);
            bounds = new Segment2D[]{new Segment2D(point1,point2),new Segment2D(point2,point3),new Segment2D(point3,point4),new Segment2D(point4,point1)};
        for (int i = 0;i < bounds.length;i++){
            temp = bounds[i].getDistance(point);
            if (temp.length()<distanceN){
                distanceN = temp.length();
                distance = temp;
            }
        }
        return distance;
    }

    /**
     * 获取该形状的参考点.
     * 通常一个参考点是该形状的的中心点.
     * 对于球面/球体，它就是中心.
     *
     * @return 参考点. 如果这个形状是控的话，就返回空.
     */
    @Override
    public Point getReferencePoint() {
        return center;
    }

    /**
     * 获取这个形状的边界.
     * 如果这个形状不能放到一个box里的话，就返回空.
     * 如果这个形状是空的，就返回一个空的形状.
     *
     * @return 代表这个形状的box.
     */
    @Override
    public Box getBounds() {
        Point2D point1,point2;
        point1 = new Point2D(center.getX()-Math.abs(length*Math.cos(angle)/2)-Math.abs(weidth*Math.sin(angle)/2),center.getY()-Math.abs(length*Math.sin(angle)/2)-Math.abs(weidth*Math.cos(angle)/2));
        point2 = new Point2D(center.getX()+Math.abs(length*Math.cos(angle)/2)+Math.abs(weidth*Math.sin(angle)/2),center.getY()+Math.abs(length*Math.sin(angle)/2)+Math.abs(weidth*Math.cos(angle)/2));
        return new Box2D(point1,point2);
    }

    /**
     * 检查这个形状是否与一个hitbox的box相交.
     *
     * @param hitbox 将要被检查的box.
     * @return 如果相交，返回真，否则返回假.
     */
    @Override
    public boolean hits(Box hitbox) {
        Segment2D[] bounds;
        Point2D point1,point2,point3,point4;
        if (!getBounds().hits(hitbox)){return false;}
        else point1 = new Point2D(center.getX()-length*Math.cos(angle)/2+weidth*Math.sin(angle)/2,center.getY()-length*Math.sin(angle)/2-weidth*Math.cos(angle)/2);
        point2 = new Point2D(center.getX()+length*Math.cos(angle)/2+weidth*Math.sin(angle)/2,center.getY()+length*Math.sin(angle)/2-weidth*Math.cos(angle)/2);
        point3 = new Point2D(center.getX()+length*Math.cos(angle)/2-weidth*Math.sin(angle)/2,center.getY()+length*Math.sin(angle)/2+weidth*Math.cos(angle)/2);
        point4 = new Point2D(center.getX()-length*Math.cos(angle)/2-weidth*Math.sin(angle)/2,center.getY()-length*Math.sin(angle)/2+weidth*Math.cos(angle)/2);
        bounds = new Segment2D[]{new Segment2D(point1,point2),new Segment2D(point2,point3),new Segment2D(point3,point4),new Segment2D(point4,point1)};
        for (int i = 0;i < bounds.length;i++){
            if (bounds[i].hits(hitbox)){
                return true;
            }
        }
        if (contains(hitbox.getReferencePoint())){return true;}
        else return false;
    }

    /**
     * 移动这个形状到一个指定的位置.
     * 对于非空形状，参考点是等于位置点.
     * 对于空形状，什么也不做.
     *
     * @param location 指定的位置
     */
    @Override
    public void moveTo(Point location) {
        center = (Point2D) location;
    }

    /**
     * 创建并返回该形状的副本.
     *
     * @return 该形状的副本.
     */
    @Override
    public Shape clone() {
        return new Rectangle2D(center,length,weidth,angle);
    }

    /**
     * 逆时针旋转某个角度
     * @param angle 旋转的角度，为弧度制
     */
    public void spin(double angle){
        this.angle = this.angle +angle;
    }

    public double getAngle(){
        return angle;
    }

    public double[] getScale(){
        return new double[]{length,weidth};
    }
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Rectangle2D){
            Point2D point1,point2,point3,point4,objPoint1,objPoint2,objPoint3,objPoint4;
            point1 = new Point2D(center.getX()-length*Math.cos(angle)/2+weidth*Math.sin(angle)/2,center.getY()-length*Math.sin(angle)/2-weidth*Math.cos(angle)/2);
            point2 = new Point2D(center.getX()+length*Math.cos(angle)/2+weidth*Math.sin(angle)/2,center.getY()+length*Math.sin(angle)/2-weidth*Math.cos(angle)/2);
            point3 = new Point2D(center.getX()+length*Math.cos(angle)/2-weidth*Math.sin(angle)/2,center.getY()+length*Math.sin(angle)/2+weidth*Math.cos(angle)/2);
            point4 = new Point2D(center.getX()-length*Math.cos(angle)/2-weidth*Math.sin(angle)/2,center.getY()-length*Math.sin(angle)/2+weidth*Math.cos(angle)/2);
            Point2D[] point = new Point2D[]{point1,point2,point3,point4};
            objPoint1 = new Point2D(((Rectangle2D) obj).center.getX()-((Rectangle2D) obj).length*Math.cos(((Rectangle2D) obj).angle)/2+((Rectangle2D) obj).weidth*Math.sin(((Rectangle2D) obj).angle)/2,((Rectangle2D) obj).center.getY()-((Rectangle2D) obj).length*Math.sin(((Rectangle2D) obj).angle)/2-((Rectangle2D) obj).weidth*Math.cos(((Rectangle2D) obj).angle)/2);
            objPoint2 = new Point2D(((Rectangle2D) obj).center.getX()+((Rectangle2D) obj).length*Math.cos(((Rectangle2D) obj).angle)/2+((Rectangle2D) obj).weidth*Math.sin(((Rectangle2D) obj).angle)/2,((Rectangle2D) obj).center.getY()+((Rectangle2D) obj).length*Math.sin(((Rectangle2D) obj).angle)/2-((Rectangle2D) obj).weidth*Math.cos(((Rectangle2D) obj).angle)/2);
            objPoint3 = new Point2D(((Rectangle2D) obj).center.getX()+((Rectangle2D) obj).length*Math.cos(((Rectangle2D) obj).angle)/2-((Rectangle2D) obj).weidth*Math.sin(((Rectangle2D) obj).angle)/2,((Rectangle2D) obj).center.getY()+((Rectangle2D) obj).length*Math.sin(((Rectangle2D) obj).angle)/2+((Rectangle2D) obj).weidth*Math.cos(((Rectangle2D) obj).angle)/2);
            objPoint4 = new Point2D(((Rectangle2D) obj).center.getX()-((Rectangle2D) obj).length*Math.cos(((Rectangle2D) obj).angle)/2-((Rectangle2D) obj).weidth*Math.sin(((Rectangle2D) obj).angle)/2,((Rectangle2D) obj).center.getY()-((Rectangle2D) obj).length*Math.sin(((Rectangle2D) obj).angle)/2+((Rectangle2D) obj).weidth*Math.cos(((Rectangle2D) obj).angle)/2);
            Point2D[] objPoint = new Point2D[]{objPoint1,objPoint2,objPoint3,objPoint4};
            for (int i = 0;i < point.length;i++){
                if (!((Rectangle2D) obj).contains(point[i])){
                    return false;
                }
                if (!contains(objPoint[i])){
                    return false;
                }
            }
            return true; //既不大于也不小于，则等于
        }
        else return false;
    }
}