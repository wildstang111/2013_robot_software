package com.wildstangs.subjects.base;

/**
 *
 * @author Nathan
 */
public class DoubleSubject extends Subject {

    double currentValue;
    double newValue;
    double previousValue;
    final static double precision = 0.001;

    public DoubleSubject() {
        super("DoubleSubject");
    }

    public DoubleSubject(String name) {
        super(name);
    }

    public DoubleSubject(String name, ISubjectEnum type) {
        super(name, type);
    }

    public DoubleSubject(ISubjectEnum type) {
        super(type.toString(), type);
    }

    public void setValue(double newValue) {
        this.newValue = newValue;
    }

    public double getValue() {
        return currentValue;
    }

    public double getPreviousValue() {
        return previousValue;
    }

    public void updateValue() {
        double diff = Math.abs(newValue - currentValue);
        if (diff > precision) {
            previousValue = currentValue;
            currentValue = newValue;
            notifyObservers();
        }
    }

    public void setValue(Object value) {
        if (value instanceof Double) {
            this.newValue = ((Double) value).doubleValue();
        } else {
            super.setValue(value);
        }
    }

    public Object getValueAsObject() {
        return new Double(currentValue);
    }
}
