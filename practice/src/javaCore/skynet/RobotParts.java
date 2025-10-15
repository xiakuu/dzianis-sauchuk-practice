package javaCore.skynet;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class RobotParts {
    private AtomicInteger heads = new AtomicInteger(0);
    private AtomicInteger torsos = new AtomicInteger(0);
    private AtomicInteger hands = new AtomicInteger(0);
    private AtomicInteger feet = new AtomicInteger(0);

    public void createParts() {
        Random random = new Random();
        for (int days = 0; days < 100; days++) {
            int partsProduce = random.nextInt(11);

            for (int i = 0; i < partsProduce; i++) {
                int partId = random.nextInt(4);
                switch (partId){
                    case 0 -> heads.incrementAndGet();
                    case 1 -> torsos.incrementAndGet();
                    case 2 -> hands.incrementAndGet();
                    case 3 -> feet.incrementAndGet();
                }
            }
        }


    }

    public int[] getParts(){
        return new int[]{heads.get(), torsos.get(), hands.get(), feet.get()};
    }

    public void useParts() {
        heads.decrementAndGet();
        torsos.decrementAndGet();
        hands.addAndGet(-2);
        feet.addAndGet(-2);
    }
}
