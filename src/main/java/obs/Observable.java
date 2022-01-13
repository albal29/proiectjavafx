package obs;

import java.util.ArrayList;

public class Observable {
    private ArrayList<Observer> lst = new ArrayList<>();

    public void addObserver(Observer o){
        lst.add(o);
    }
    public void remObserver(Observer o){
        lst.remove(o);
    }

    public void notifyobservers(){
        lst.forEach(x->x.update());
    }
}
