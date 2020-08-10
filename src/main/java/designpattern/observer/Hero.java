package designpattern.observer;

import java.util.HashMap;
import java.util.Map;

public class Hero extends Subject {
    void move() {
        System.out.println("主角向前移动");
        notifyObservers();
    }

    public static void main(String[] args) {

         Map test = new HashMap<>();
         test.isEmpty();
        //初始化对象
        Hero hero = new Hero();
        Monster monster = new Monster();
        //Trap trap = new Trap();
        Treasure treasure = new Treasure();
        //注册观察者
        hero.attachObserver(monster);
        //hero.attachObserver(trap);
        hero.attachObserver(treasure);
        //移动事件
        hero.move();
    }
}
