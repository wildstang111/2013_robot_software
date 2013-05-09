package com.wildstangs.subjects.base;

import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author Nathan
 */
abstract public class Subject {

    protected String name;
    protected List observers;
    protected ISubjectEnum type;

    protected Subject(String name) {
        this(name, null);
    }

    protected Subject(String name, ISubjectEnum type) {
        this.name = name;
        observers = new List();
        this.type = type;
    }

    protected Subject() {
        this("Subject");
    }

    public void attach(IObserver observer) {
        observers.add(observer);
    }

    public void detach(IObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    protected void notifyObservers() {
        for (int i = 0; i < observers.size(); i++) {
            ((IObserver) observers.get(i)).acceptNotification(this);
        }
    }

    public String getName() {
        return name;
    }

    public ISubjectEnum getType() {
        return type;
    }

    public void setValue(Object value) {
        System.out.println(name + " does not currently support this object for use with setValue.");
    }

    abstract public Object getValueAsObject();
}
