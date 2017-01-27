package org.socialforce.app.gui;

import org.socialforce.scene.Scene;
import org.socialforce.drawer.DrawerInstaller;
import org.socialforce.drawer.impl.SceneDrawerInstaller;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Ledenel on 2016/8/23.
 */
public class SceneShower {
    private JCheckBox visibleCheckBox;
    private JPanel showPanel;
    private JPanel root;
    private JButton changeButton;
    private JLabel totalPeopleLabel;
    private JLabel remainPeopleLabel;
    private JLabel timeLabel;
    private JLabel trappedPeopleLabel;
    private String title;

    private DrawerInstaller drawerInstaller;

    /**
     * show the title of the scene
     * @param title
     */
    public SceneShower(String title) {
        this.title = title;
    }

    /**
     * //////??????
     * @return the root
     */
    public JPanel getRoot() {
        root.setBorder(BorderFactory.createTitledBorder(title));
        return root;

    }

    private SceneBoard board;

    public SceneBoard getBoard() {
        return board;
    }

    public void setBoard(SceneBoard board) {
        this.board = board;
    }

    /**
     *
     */
    private void createUIComponents() {
        root = new JPanel();

        //root.setBorder(BorderFactory.createTitledBorder(title));
        // TODO: place custom component creation code here
        board = new SceneBoard();
        showPanel = board;
    }

    BufferedImage image;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        // FIXME: 2017/1/2 change this image to dynamic-sized(with components.) or using swing's own double-buffered system.
        image = new BufferedImage(1920,1080, BufferedImage.TYPE_INT_ARGB);
        drawerInstaller = new SceneDrawerInstaller((Graphics2D) image.getGraphics(), image.getWidth(), image.getHeight());
        drawerInstaller.addDrawerSupport(scene);
        board.setImage(image);
        board.setScene(scene);
        board.getResizeListener().componentResized(null);
    }

    Scene scene;

    public DrawerInstaller getDrawerInstaller(){
        return drawerInstaller;
    }

}
