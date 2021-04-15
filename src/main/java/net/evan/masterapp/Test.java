package net.evan.masterapp;

import net.evan.masterapp.entities.EntitiesManager;

public class Test {

    public Test() {
        Test2 test2 = new Test2();
        System.out.println(Test2.getInstance());
    }

    public static void main(String[] args) {
        new Test();
    }

}

class Test2 {
    private static Test2 instance;

    public Test2() {
        instance = this;
    }

    public static void main(String[] args) {
        new Test2();
    }

    public static Test2 getInstance() {
        return instance;
    }
}
