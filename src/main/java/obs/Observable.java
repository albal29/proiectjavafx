package obs;

import java.util.ArrayList;

public class Observable {
    private final ArrayList<Observer> lst = new ArrayList<>();

    public void addObserver(Observer o) {
        lst.add(o);
    }

    public void remObserver(Observer o) {
        lst.remove(o);
    }

    public void notifyobservers() {
        lst.forEach(Observer::update);
    }
}
