package javaCore.skynet;

public class Faction implements Runnable {
    private String factionName;
    private RobotParts robotParts;
    private int robots = 0;


    public Faction(String factionName, RobotParts robotParts) {
        this.factionName = factionName;
        this.robotParts = robotParts;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
                int[] allParts = robotParts.getParts();
                int heads = allParts[0];
                int torsos = allParts[1];
                int hands = allParts[2];
                int feet = allParts[3];

                int maxHeads = Math.min(heads, 5);
                int maxTorsos = Math.min(torsos, 5);
                int maxHands = Math.min(hands, 5);
                int maxFeet = Math.min(feet, 5);

                if (maxHeads > 0 && maxTorsos > 0 && maxHands > 1 && maxFeet > 1) {
                    robots++;
                    robotParts.useParts();

            }
        }
            System.out.println(factionName + " " + robots);

    }

        public int getRobots(){
            return robots;
        }

    }
