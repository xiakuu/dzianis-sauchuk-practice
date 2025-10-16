package javaCore.MiniSpring.MiniSpringApp;

import javaCore.MiniSpring.CustomAnnotations.*;

@Component
public class Service {
    public void serve(){
        System.out.println("service running");
    }
}
