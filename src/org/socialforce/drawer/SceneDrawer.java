package org.socialforce.drawer;

import org.socialforce.app.ProxyedGraphics2D;
import org.socialforce.app.Scene;

/**
 * Created by Ledenel on 2016/8/24.
 */
public class SceneDrawer implements Drawer<ProxyedGraphics2D> {
    /**
     * draw the pattern on the specific device.
     */
    @Override
    public void draw() {
// TODO: 2016/8/24  add draw scene.
    }

    public SceneDrawer(Scene scene) {
        this.scene = scene;
    }

    Scene scene;

    protected int color;

    /**
     * get the primary color of the pattern. <br>
     * the color scheme is ARGB.<br>
     * CAUTION: there is no guarantee that the color is the color drawed on the device.
     * please call draw() before to sync the color you get from the device.
     *
     * @return the ARGB-format color.
     */
    @Override
    public int getColor() {
        return color;
    }

    /**
     * set the primary color of the pattern. <br>
     * the color scheme is ARGB.<br>
     * CAUTION: there is no guarantee that the pattern on the device will changed.
     * please call draw() to ensure that the changes have been applied on the device.
     *
     * @param color the ARGB-format color to set.
     */
    @Override
    public void setColor(int color) {
        this.color = color;
    }

    ProxyedGraphics2D graphics2D;

    /**
     * get the device of the drawer.
     *
     * @return the device which the drawer is using.
     */
    @Override
    public ProxyedGraphics2D getDevice() {
        return graphics2D;
    }

    /**
     * set the device for the drawer.
     *
     * @param device the device for the drawer.
     */
    @Override
    public void setDevice(ProxyedGraphics2D device) {
        this.graphics2D = device;
    }
}
