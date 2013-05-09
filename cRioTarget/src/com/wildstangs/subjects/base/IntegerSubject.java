package com.wildstangs.subjects.base;

/**
 *
 * @author Nathan
 */
public class IntegerSubject extends Subject {

    int currentValue;
    int newValue;
    int previousValue;

    public IntegerSubject() {
        super("IntegerSubject");
    }

    public IntegerSubject(String name) {
        super(name);
    }

    public IntegerSubject(String name, ISubjectEnum type) {
        super(name, type);
    }

    public IntegerSubject(ISubjectEnum type) {
        super(type.toString(), type);
    }

    public void setValue(int newValue) {
        this.newValue = newValue;
    }

    public int getValue() {
        return currentValue;
    }

    public int getPreviousValue() {
        return previousValue;
    }

    public void updateValue() {
        if (newValue != currentValue) {
            previousValue = currentValue;
            currentValue = newValue;
            notifyObservers();
        }
    }

    public void setValue(Object value) {
        if (value instanceof Integer) {
            this.newValue = ((Integer) value).intValue();
        } else {
            super.setValue(value);
        }
    }

    public Object getValueAsObject() {
        return new Integer(currentValue);
    }
}
