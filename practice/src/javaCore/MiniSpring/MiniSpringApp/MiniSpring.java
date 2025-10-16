package javaCore.MiniSpring.MiniSpringApp;

import javaCore.MiniSpring.MiniApplicationContext;

public class MiniSpring {
    public static void main(String[] args) throws Exception {
        MiniApplicationContext context = new MiniApplicationContext("javaCore.MiniSpring.MiniSpringApp");
        Controller controller = context.getBean(Controller.class);
        controller.processReq();
    }
}
