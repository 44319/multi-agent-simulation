package org.socialforce.app.Applications;

import org.socialforce.app.Interpreter;
import org.socialforce.app.Application;
import org.socialforce.app.impl.AgentStepCSVWriter;
import org.socialforce.app.impl.SimpleInterpreter;
import org.socialforce.drawer.impl.EntityDrawer;
import org.socialforce.drawer.impl.EntityDrawerInstaller;
import org.socialforce.drawer.impl.SceneDrawer;
import org.socialforce.geom.impl.*;
import org.socialforce.model.Agent;
import org.socialforce.model.InteractiveEntity;
import org.socialforce.model.impl.*;
import org.socialforce.scene.Scene;
import org.socialforce.scene.SceneLoader;
import org.socialforce.scene.impl.*;
import org.socialforce.strategy.GoalStrategy;
import org.socialforce.strategy.PathFinder;
import org.socialforce.strategy.impl.AStarPathFinder;
import org.socialforce.strategy.impl.NearestGoalStrategy;

import java.awt.*;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Created by Whatever on 2017/3/7.
 */
public class ApplicationForNarrowPattern extends SimpleApplication implements Application {
    BaseAgent template;
    double DoorWidth;
    int density;
    /**
     * start the application immediately.
     */
    @Override
    public void start() {
        setUpScenes();
        for (Iterator<Scene> iterator = scenes.iterator(); iterator.hasNext(); ) {
            AgentStepCSVWriter csvWriter = new AgentStepCSVWriter();
            currentScene = iterator.next();
            currentScene.addSceneListener(csvWriter);
            PathFinder pathFinder = new AStarPathFinder(currentScene, new Circle2D(new Point2D(0,0),0.1),0.05);
            GoalStrategy strategy = new NearestGoalStrategy(currentScene, pathFinder);
            strategy.pathDecision();
            this.initScene(currentScene);
            int timeStamp = 0;
            while (!toSkip()) {
                this.stepNext(currentScene);
                if(timeStamp % 33 == 0){
                    for(Iterator<InteractiveEntity> iter = currentScene.getStaticEntities().selectClass(Monitor.class).iterator(); iter.hasNext();){
                        Monitor monitor = (Monitor)iter.next();
                        double speed = monitor.sayVelocity();
                        double rho = monitor.sayRho();
                        System.out.println(speed+"\t"+rho);
                    }
                }
                timeStamp++;
            }
            csvWriter.writeCSV("output/agent.csv");
            if(onStop()) return;
        }
    }

    @Override
    public boolean toSkip(){
        return Skip || currentScene.getAllAgents().size() < 1;
    }

    /**
     * 需要根据parameter的map来生成一系列scene
     */
    @Override
    public void setUpScenes(){
        template = new BaseAgent(new Circle2D(new Point2D(0,0),0.486/2), new Velocity2D(0,0));
        scenes = new LinkedList<>();
        DoorWidth = 1.2;
        density = 10;
        //setUpT1Scene1();
        //setUpT1Scene2();
 //       setUpT1Scene3();
 //       setUpT1Scene4();
        //setUpT1Scene5();
        setMap();
        setUpA4Map();
        //setUpT1Scene5();
        //setUpT1Scene6();
        //setUpT2Scene();
        //setUpPassageScene();
        //setUpPassageScene2();
        for(Scene scene:scenes){
            scene.setApplication(this);
        }
    }


    /**
     * 正常左右两个门的位置，无额外障碍。
     */
    protected void setUpT1Scene1(){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("T1.s");
        Interpreter interpreter = new SimpleInterpreter();
        interpreter.loadFrom(is);
        SceneLoader loader = interpreter.setLoader().setModel(new SimpleForceModel());
        SimpleParameterPool parameters = new SimpleParameterPool();

        parameters.addValuesAsParameter(new SimpleEntityGenerator()
                .setValue(new Exit(new Box2D(5-DoorWidth/2,-0.5,DoorWidth,2)))
                                        ,new SimpleEntityGenerator()
                .setValue(new Exit(new Box2D(10-DoorWidth,-0.5,DoorWidth,2)))
        );

        parameters.addValuesAsParameter(
                new RandomEntityGenerator2D(density*10,new Box2D(0,0,10,-10)).setValue(template)
        );

        parameters.addValuesAsParameter(new MultipleEntitiesGenerator()
                .addValue(new SafetyRegion(new Box2D(1,10,8,1)))
                .addValue(new Monitor(new Box2D(0,0,10,1)))
        );

        loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }
    }

    /**
     * 门外过道宽窄变化
     */
    protected void setUpT1Scene2(){

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("T1.s");
        Interpreter interpreter = new SimpleInterpreter();
        interpreter.loadFrom(is);
        SceneLoader loader = interpreter.setLoader().setModel(new SimpleForceModel());
        SimpleParameterPool parameters = new SimpleParameterPool();
        parameters.addValuesAsParameter(new MultipleEntitiesGenerator()
                .addValue(new Exit(new Box2D(5-DoorWidth,-0.5,2*DoorWidth,2)))
                .addValue(new SafetyRegion(new Box2D(1,10,8,1)))
                .addValue(new Monitor(new Box2D(0,0,10,1)))
                .addValue(new Monitor(new Box2D(0,3,10,1)))
        );
        parameters.addValuesAsParameter(
                new RandomEntityGenerator2D(density*10,new Box2D(0,0,10,-10)).setValue(template)
        );
        parameters.addValuesAsParameter(
                new MultipleEntitiesGenerator()
                        .addValue(new Wall(new Rectangle2D(new Point2D(3,3),1,3.5,Math.PI/6)))
                        .addValue(new Wall(new Rectangle2D(new Point2D(7,3),1,3.5,-Math.PI/6)))
                ,new MultipleEntitiesGenerator()
                        .addValue(new Wall(new Rectangle2D(new Point2D(3,2),1,3.5,Math.PI/6)))
                        .addValue(new Wall(new Rectangle2D(new Point2D(7,2),1,3.5,-Math.PI/6)))
                ,new MultipleEntitiesGenerator()
                        .addValue(new Wall(new Rectangle2D(new Point2D(3,3),1,3.5,-Math.PI/6)))
                        .addValue(new Wall(new Rectangle2D(new Point2D(7,3),1,3.5,Math.PI/6)))
                        .addValue(new Wall(new Box2D(5-DoorWidth,0,DoorWidth/2,1)))
                        .addValue(new Wall(new Box2D(5+DoorWidth,0,-DoorWidth/2,1)))
                ,new MultipleEntitiesGenerator()
                        .addValue(new Wall(new Box2D(5-DoorWidth,0,DoorWidth/2,1)))
                        .addValue(new Wall(new Box2D(5+DoorWidth,0,-DoorWidth/2,1)))
                        .addValue(new Wall(new Rectangle2D(new Point2D(2,3),1,5,Math.PI/4)))
                        .addValue(new Wall(new Rectangle2D(new Point2D(8,3),1,5,-Math.PI/4)))
                        .addValue(new Wall(new Rectangle2D(new Point2D(2,6),1,5,-Math.PI/4)))
                        .addValue(new Wall(new Rectangle2D(new Point2D(8,6),1,5,Math.PI/4)))
                        //TODO 实现一个Exi切一串墙的功能 .addValue(new Exit(new Box2D(5-2*DoorWidth,-0.5,4*DoorWidth,2)))
        );

        loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }

    }

    /**
     * 厚门，拐角
     */
    protected void setUpT1Scene3(){
        /*
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("T1.s");
        Interpreter interpreter = new SimpleInterpreter();
        interpreter.loadFrom(is);
        SceneLoader loader = interpreter.setLoader();
        ParameterPool parameters = new SimpleParameterPool();
        parameters.addLast(genParameter(new SV_Exit(new Box2D[]{new Box2D(5-DoorWidth/2,-0.05,DoorWidth,1.1),new Box2D(5-DoorWidth/2,0.05,DoorWidth,4)}),
                new SV_Exit(new Box2D[]{new Box2D(10-DoorWidth,-0.05,DoorWidth,1.1),new Box2D(10-DoorWidth,0.05,DoorWidth,4)})));
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*10,new Box2D(0,0,10,-10),template)));
        parameters.addLast(genParameter(new SV_Wall(new Box2D[]{new Box2D(0,0.1,11,3)})));
        parameters.addLast(genParameter(new SV_SafetyRegion(new Box2D(1,10,8,1))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,0,10,1))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,4,10,1))));
        loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }
        */
    }


    /**
     * 两层门
     */
    protected void setUpT1Scene4(){
        /*
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("T1.s");
        Interpreter interpreter = new SimpleInterpreter();
        interpreter.loadFrom(is);
        SceneLoader loader = interpreter.setLoader();
        ParameterPool parameters = new SimpleParameterPool();
        parameters.addLast(genParameter(new SV_Exit(new Box2D[]{new Box2D(5-DoorWidth/2,-0.5,DoorWidth,2),new Box2D(5-DoorWidth/2,-4.5,DoorWidth,2)}),
                new SV_Exit(new Box2D[]{new Box2D(10-DoorWidth,-0.5,DoorWidth,2),new Box2D(10-DoorWidth,-4.5,DoorWidth,2)})));
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*10,new Box2D(0,0,10,-10),template)));
        parameters.addLast(genParameter(new SV_Wall(new Box2D[]{new Box2D(0,-4,5-DoorWidth/2,5),new Box2D(5+DoorWidth/2,-4,5-DoorWidth/2,5)}),new SV_Wall(new Box2D[]{new Box2D(0,-4,10-DoorWidth,5)})));
        parameters.addLast(genParameter(new SV_SafetyRegion(new Box2D(1,10,8,1))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,0,10,1))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,-4,10,1))));
        loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }
        */
    }

    protected void setUpA4Map(){
        SceneLoader loader = new StandardSceneLoader(new SimpleScene(new Box2D(0, 0, 19.21, 10.77)),
                new Wall[]{
                        new Wall(new Box2D(3.41,9.75,9.05,0.53)), //上
                        new Wall(new Box2D(3.41,3.21,9.05,0.53)), //下
                        new Wall(new Box2D(3.41,3.74,0.53,6.01)), //左
                        //以上固定不动
                        new Wall(new Box2D(11.93,7.39,0.53,2.36)),//右上
                        new Wall(new Box2D(11.93,3.74,0.53,2.65)),//右下
                        //上边两个是门
                        new Wall(new Box2D(9.33,6.32,0.58,1.18)),//横向障碍物

                }).setModel(new SimpleForceModel());
        SimpleParameterPool parameters = new SimpleParameterPool();
        parameters.addValuesAsParameter(new MultipleEntitiesGenerator()
                .addValue(new SafetyRegion(new Box2D(14.05,3.81,1,9)))  //不动
                .addValue(new Monitor(new Box2D(11.93,6.3,0.53,1.09)))
        );

        /*parameters.addValuesAsParameter(new MultipleEntitiesGenerator()
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.69,3.65), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.82,4.10), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.41,5.17), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.03,4.72), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.26,4.27), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.81,4.73), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.60,5.24), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.20,5.63), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.06,6.22), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.34,5.80), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.59,6.37), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.54,6.92), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.09,6.88), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.71,7.37), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(5.82,7.61), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.44,7.88), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.94,7.89), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.43,8.14), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.86,8.36), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.99,8.43), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.52,8.52), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.38,9.40), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(5.87,8.96), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.59,9.12), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.73,9.74), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.57,4.02), 0.486/2), new Velocity2D(0,0)))
                .setCommonName("Agent")
        );*/

        parameters.addValuesAsParameter(new MultipleEntitiesGenerator()
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.52,4.01), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.12,4.27), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.36,4.64), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.44,5.19), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(8.34,5.01), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(8.02,5.66), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.31,5.98), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.14,5.59), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(5.55,6.12), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.00,6.61), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.64,6.75), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.36,7.31), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(5.87,7.77), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.68,7.71), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(8.23,7.79), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.48,8.24), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(8.23,8.47), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.44,8.32), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.10,8.65), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.58,8.84), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(5.95,9.08), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.37,9.52), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.66,9.13), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.09,9.27), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(7.34,9.50), 0.486/2), new Velocity2D(0,0)))
                .addValue(new BaseAgent(new Circle2D(new Point2D(6.85,5.76), 0.486/2), new Velocity2D(0,0)))
                .setCommonName("Agent")
        );


                loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }
    }


    /**
     * 门前有柱子
     */
    protected void setUpT1Scene5(){

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("T1.s");
        Interpreter interpreter = new SimpleInterpreter();
        interpreter.loadFrom(is);
        SceneLoader loader = interpreter.setLoader().setModel(new NeuralForceModel());
        SimpleParameterPool parameters = new SimpleParameterPool();
        parameters.addValuesAsParameter(
                new SimpleEntityGenerator()
                    .setValue(new Wall(new Box2D(4,-4,2,2)))
                    .setPriority(10)
                ,new SimpleEntityGenerator()
                    .setValue(new Wall(new Circle2D(new Point2D(5,-3),1)))
                    .setPriority(10)
        );

        parameters.addValuesAsParameter(new MultipleEntitiesGenerator()
                .addValue(new SafetyRegion(new Box2D(1,10,8,1)))
                .addValue(new Monitor(new Box2D(0,0,10,2)))

        );

        parameters.addValuesAsParameter(new SimpleEntityGenerator()
                .setValue(new Exit(new Box2D(5-DoorWidth/2,-0.5,DoorWidth,2)))
                .setPriority(5)
        );

       parameters.addValuesAsParameter(
                new RandomEntityGenerator2D(26,new Box2D(0,-10,10,5))
                        .setValue(template)
                        .setGaussianParameter(1,0.03)
       );

//        parameters.addValuesAsParameter(
//                new MultipleEntitiesGenerator()
//                        .addValue(new BaseAgent(new Circle2D(new Point2D(5,-2), 0.486/2), new Velocity2D(0,0)))
//                        .addValue(new BaseAgent(new Circle2D(new Point2D(2,-2), 0.486/2), new Velocity2D(0,0)))
//                        .addValue(new BaseAgent(new Circle2D(new Point2D(7,-2), 0.486/2), new Velocity2D(0,0)))
//                        .addValue(new BaseAgent(new Circle2D(new Point2D(5,-4), 0.486/2), new Velocity2D(0,0)))
//                        .addValue(new BaseAgent(new Circle2D(new Point2D(2,-4), 0.486/2), new Velocity2D(0,0)))
//                        .addValue(new BaseAgent(new Circle2D(new Point2D(7,-4), 0.486/2), new Velocity2D(0,0)))
//                        .addValue(new BaseAgent(new Circle2D(new Point2D(5,-6), 0.486/2), new Velocity2D(0,0)))
//                        .addValue(new BaseAgent(new Circle2D(new Point2D(2,-6), 0.486/2), new Velocity2D(0,0)))
//        );

        loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }

    }

    public void setMap(){
        double DoorWidth = 1.36;
        SceneLoader loader = new StandardSceneLoader(new SimpleScene(new Box2D(0, 0, 19.21, 10.77)),
                new Wall[]{
                        new Wall(new Box2D(3.41,9.75,9.05,0.53)), //上
                        new Wall(new Box2D(3.41,3.21,9.05,0.53)), //下
                        new Wall(new Box2D(3.41,3.74,0.53,6.01)), //左
                        //以上固定不动
                        new Wall(new Box2D(11.93,7.39,0.53,2.36)),//右上
                        new Wall(new Box2D(11.93,3.74,0.53,2.65)),//右下
                        //上边两个是门
                        new Wall(new Box2D(9.33,6.32,0.58,1.18)),//横向障碍物

                }).setModel(new CSVReaderModel("input/横向障碍物-宽门-无奖励-1.csv", 1.0/30));
        SimpleParameterPool parameters = new SimpleParameterPool();
        parameters.addValuesAsParameter(new MultipleEntitiesGenerator()
                .addValue(new SafetyRegion(new Box2D(14.05,3.81,1,9)))  //不动
                .addValue(new Monitor(new Box2D(11.93,6.3,0.53,1.09)))
        );

        parameters.addValuesAsParameter(
                new RandomEntityGenerator2D(26,new Box2D(3.5,3.5,3,7))
                        .setValue(template)
        );

        loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }
    }


    /**
     * 螺旋形走廊，连续拐角
     */
    protected void setUpT1Scene6(){
        /*
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("T1.s");
        Interpreter interpreter = new SimpleInterpreter();
        interpreter.loadFrom(is);
        SceneLoader loader = interpreter.setLoader();
        ParameterPool parameters = new SimpleParameterPool();
        parameters.addLast(genParameter(new SV_Exit(new Box2D[]{new Box2D(10-(DoorWidth+1),-0.5,(DoorWidth+1),2)})));
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*10,new Box2D(0,0,10,-10),template)));
        parameters.addLast(genParameter(new SV_Wall(new Box2D[]{new Box2D(9-(DoorWidth+1),0,1,-(9-DoorWidth)),new Box2D(new Point2D(DoorWidth+1,DoorWidth+1),new Point2D(8-DoorWidth,DoorWidth+2))})));
        parameters.addLast(genParameter(new SV_SafetyRegion(new Box2D(1,10,8,1))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,0,10,1))));
        loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }
        */
    }

    /**
     * 类似食堂桌子的场景
     */
    protected void setUpT2Scene(){
        /*
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("T2.s");
        Interpreter interpreter = new SimpleInterpreter();
        interpreter.loadFrom(is);
        SceneLoader loader = interpreter.setLoader();
        ParameterPool parameters = new SimpleParameterPool();
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*20,new Box2D(0,0,10,-20),template)));

        parameters.addLast(genParameter(new SV_Exit(new Box2D[]{new Box2D(5-DoorWidth/2,-0.5,DoorWidth,2)}),new SV_Exit(new Box2D[]{new Box2D(10-DoorWidth,-0.5,DoorWidth,2)})));

        parameters.addLast(genParameter(new SV_Wall(new PhysicalEntity[]{new Circle2D(new Point2D(3,-4),2),new Circle2D(new Point2D(7,-4),2)}),new SV_Wall(new PhysicalEntity[]{}),new SV_Wall(new PhysicalEntity[]{new Box2D(1,-10,1,9),new Box2D(3,-10,1,9),new Box2D(5,-10,1,9),new Box2D(7,-10,1,9),new Box2D(9,-10,1,9)})));

        parameters.addLast(genParameter(new SV_SafetyRegion(new Box2D(1,6,8,1))));

        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,0,10,1))));

        loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }
        */
    }


    /**
     * 通道末端有三个房间
     */
    protected void setUpPassageScene(){
        /*
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("passage.s");
        Interpreter interpreter = new SimpleInterpreter();
        interpreter.loadFrom(is);
        SceneLoader loader = interpreter.setLoader();
        ParameterPool parameters = new SimpleParameterPool();
        parameters.addLast(genParameter(new SV_Exit(new Box2D[]{new Box2D(2-DoorWidth/2,-0.5,DoorWidth,2)})));
        parameters.addLast(genParameter(new SV_Exit(new Box2D[]{new Box2D(-1.5,-35-DoorWidth/2,2,DoorWidth)})));
        parameters.addLast(genParameter(new SV_Exit(new Box2D[]{new Box2D(3.5,-35-DoorWidth/2,2,DoorWidth)})));
        parameters.addLast(genParameter(new SV_Exit(new Box2D[]{new Box2D(2-DoorWidth/2,-41.5,DoorWidth,2)})));
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*5,new Box2D(0,0,3,-40),template)));
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*10,new Box2D(-1,-30,-10,-10),template)));
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*10,new Box2D(5,-30,10,-10),template)));
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*10,new Box2D(-3,-41,10,-10),template)));
        parameters.addLast(genParameter(new SV_SafetyRegion(new Box2D(0,6,4,1))));
        parameters.addLast(genParameter(new SV_Wall(new Box2D[]{new Box2D(-1,-30,-10,1),new Box2D(-12,-30,1,-10),new Box2D(-1,-40,-10,-1)})));
        parameters.addLast(genParameter(new SV_Wall(new Box2D[]{new Box2D(5,-30,10,1),new Box2D(15,-30,1,-10),new Box2D(5,-40,10,-1)})));
        parameters.addLast(genParameter(new SV_Wall(new Box2D[]{new Box2D(-3,-41,-1,-10),new Box2D(-3,-52,11,1),new Box2D(7,-41,1,-10)})));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,0,4,1))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(2-DoorWidth/2,-41.5,DoorWidth,2))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(-1.5,-35-DoorWidth/2,2,DoorWidth))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(3.5,-35-DoorWidth/2,2,DoorWidth))));
        loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }
        */
    }

    /**
     * 通道两侧有6个房间
     */
    protected void setUpPassageScene2(){
        /*
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("passage.s");
        Interpreter interpreter = new SimpleInterpreter();
        interpreter.loadFrom(is);
        SceneLoader loader = interpreter.setLoader();
        ParameterPool parameters = new SimpleParameterPool();
        parameters.addLast(genParameter(new SV_Exit(new Box2D[]{new Box2D(2-DoorWidth/2,-0.5,DoorWidth,2)})));
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*5,new Box2D(0,0,3,-40),template)));
        parameters.addLast(genParameter(new SV_SafetyRegion(new Box2D(0,6,4,1))));
        parameters.addLast(genParameter(new SV_Wall(new Box2D[]{new Box2D(0,-5,-10,1),new Box2D(0,-15,-10,1),new Box2D(0,-25,-10,1),new Box2D(0,-35,-10,1)})));
        parameters.addLast(genParameter(new SV_Wall(new Box2D[]{new Box2D(5,-5,10,1),new Box2D(5,-15,10,1),new Box2D(5,-25,10,1),new Box2D(5,-35,10,1)})));
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*30,new Box2D(0,-5,-10,-30),template)));
        parameters.addLast(genParameter(new RandomEntityGenerator2D(density*30,new Box2D(5,-5,10,-30),template)));
        parameters.addLast(genParameter(new SV_Wall(new Box2D[]{new Box2D(-11,0,1,-40),new Box2D(25,0,1,-40)})));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,0,4,1))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,-5,1,-10))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,-15,1,-10))));
        parameters.addLast(genParameter(new SV_Monitor(new Box2D(0,-25,1,-10))));
        loader.readParameterSet(parameters);
        for (Scene s : loader.readScene()){
            scenes.add(s);
        }
        */
    }


    @Override
    public void manageDrawer(SceneDrawer drawer){
        drawer.setBackgroundColor(Color.white);
        EntityDrawerInstaller installer = drawer.getEntityDrawerInstaller();
        installer.unregister(Agent.class);
        installer.unregister(InteractiveEntity.class);
        EntityDrawer agentDrawer = new EntityDrawer(installer.getDevice());
        agentDrawer.setColor(Color.blue);
        installer.registerDrawer(agentDrawer,Agent.class);
        installer.registerDrawer(new EntityDrawer(installer.getDevice()),InteractiveEntity.class);
    }

}

