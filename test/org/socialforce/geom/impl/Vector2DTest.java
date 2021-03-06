package org.socialforce.geom.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.socialforce.geom.Vector;

import static org.junit.Assert.*;

/**
 * Created by Ledenel on 2016/8/7.
 */
public class Vector2DTest {
    Vector2D a,b,zero;

    @Before
    public void setUp() throws Exception {
        a = new Vector2D(3,4);
        b = new Vector2D(-3,2);
        zero = new Vector2D(0,0);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void dimension() throws Exception {
        assertEquals(2,a.dimension());

    }

    @Test
    public void basicAdd() throws Exception {
        a.add(b);
        assertEquals(new Vector2D(0,6),a);
    }

    @Test
    public void basicSub() throws Exception {
        a.sub(b);
        assertEquals(new Vector2D(6,2),a);
    }

    @Test (expected = IllegalArgumentException.class)
    public void convertTest() throws Exception{
        Vector notA2DVector=null;
        a.quiteConvert(notA2DVector);
    }

    @Test
    public void basicScale() throws Exception {
        a.scale(10);
        assertNotEquals(new Vector2D(0.3,0.4),a);
        assertEquals(new Vector2D(30,40),a);
    }

    @Test
    public void epsilonEquals() throws Exception {
        Vector2D small = new Vector2D(0.05,-0.1);
        assertTrue("equals within the epsilon 0.1",zero.epsilonEquals(small,0.1));
        assertFalse("not equals within the epsilon 0.05",zero.epsilonEquals(small,0.05));
        assertFalse("not equals within the epsilon 0.07",zero.epsilonEquals(small,0.07));
    }

    @Test
    public void basicDot() throws Exception {
        assertEquals(8,a.dot(b),1e10-7);
    }

    @Test
    public void get() throws Exception {
        double[] atest = {4,3};
        a.get(atest);
        assertEquals(atest[0],3,1e10-7);
        assertEquals(atest[1],4,1e10-7);
    }

    @Test
    public void overflowSet() throws Exception {
        double [] vals = {5.5,6.6,9.9};
        b.set(vals);
        assertEquals(new Vector2D(5.5,6.6), b);
    }

    @Test
    public void notEnoughSet() throws Exception {
        double [] vals = {6.6};
        b.set(vals);
        assertEquals(new Vector2D(6.6,2),b);
    }

    @Test
    public void testClone() throws Exception {
        Vector cloned = b.clone();
        assertFalse("b == b.clone() should be false",b == cloned);
        assertEquals("b.equals(b.clone()) should be true",b,cloned);
    }

    @Test
    public void otherSetTest() throws Exception {
        Vector vector = new Vector2D(-1,-5);
        a.set(vector);
        assertNotEquals(new Vector2D(3,4),a);
        assertEquals(new Vector2D(-1,-5),a);
    }

    @Test
    public void projectTest() throws Exception {
        Vector c = b.clone();
        b.project(a);
        c.clearProject(a);
        assertEquals(0,c.dot(b),1e-7);
    }

    @Test
    public void lenthTest() throws Exception{
        assertEquals(a.length(),5,0.001);
        assertEquals(b.length_sq(),13,0.001);
    }

    @Test
    public void refVectorTest() throws Exception {
        assertEquals(new Vector2D(0.6,0.8),a.getRefVector());
    }


    @Test
    public void SpinTest() throws Exception{
        zero.rotate(1231);
        assertEquals(new Vector2D(0,0),zero);
        Vector2D vector2D = new Vector2D(5,0);
        vector2D.rotate(Math.atan2(4,3));
        assertEquals(a,vector2D);
        a.rotate(Math.PI/2);
        assertEquals(new Vector2D(-4,3),a);

    }

    @Test
    public void getRotateAngle() throws Exception{
        Vector2D vr = new Vector2D(-1,-1);
        Vector2D vb = new Vector2D(1,0);
        System.out.print(Vector2D.getRotateAngle(vr,vb));


    }

}