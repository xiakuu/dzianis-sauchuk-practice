package javaCore.skynet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateRobot {
    public static void main(String[] args) throws InterruptedException {
        RobotParts robotParts = new RobotParts();
        Faction wednesday = new Faction("wednesday", robotParts);
        Faction world = new Faction("world", robotParts);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(robotParts::createParts);
        Thread.sleep(10);
        executorService.execute(wednesday);
        executorService.execute(world);

        executorService.shutdown();


        while (!executorService.isTerminated()) {}

            if(world.getRobots() > wednesday.getRobots()){
            System.out.println("world robots won");
        } else if (wednesday.getRobots() > world.getRobots()){
            System.out.println("wednesday robots won");
        } else{
            System.out.println("tie");
        }




    }
}
