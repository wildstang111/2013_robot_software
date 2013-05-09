package com.wildstangs.subjects.base;

/**
 *
 * @author Nathan
 */
public class BooleanSubject extends Subject {

    boolean currentValue;
    boolean newValue;
    boolean previousValue;

    public BooleanSubject() {
        super("BooleanSubject");
    }

    public BooleanSubject(String name) {
        super(name, null);
    }

    public BooleanSubject(String name, ISubjectEnum type) {
        super(name, type);
    }

    public BooleanSubject(ISubjectEnum type) {
        super(type.toString(), type);
    }

    public void setValue(boolean newValue) {
        this.newValue = newValue;
    }

    public boolean getValue() {
        return currentValue;
    }

    public boolean getPreviousValue() {
        return previousValue;
    }

    public void updateValue() {
        if (newValue != currentValue) {
            previousValue = currentValue;
            currentValue = newValue;
            notifyObservers();
//            System.out.println(name + " changed to " + currentValue);
        }

    }

    public void setValue(Object value) {
        if (value instanceof Boolean) {
            this.newValue = ((Boolean) value).booleanValue();
        } else {
            super.setValue(value);
        }
    }

    public Object getValueAsObject() {
        return new Boolean(currentValue);
    }
}
