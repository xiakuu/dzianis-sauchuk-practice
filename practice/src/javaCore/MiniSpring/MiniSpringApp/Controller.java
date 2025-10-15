package javaCore.MiniSpring.MiniSpringApp;

import javaCore.MiniSpring.CustomAnnotations.*;
import javaCore.MiniSpring.InitializingBean;

@Component
public class Controller implements InitializingBean {
    @Override
    public void afterPropertiesSet() {
        System.out.println("controller init with service");
    }

    @Autowired
    private Service service;

    public void processReq(){
        service.serve();
    }
}
