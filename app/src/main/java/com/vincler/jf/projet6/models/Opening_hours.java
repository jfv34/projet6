package com.vincler.jf.projet6.models;

public class Opening_hours {

    Day day;
    OpenOrClose openOrClose;
    int hours;
    int minutes;

    public Opening_hours(Day day, OpenOrClose openOrClose, int hours, int minutes) {

        this.day = day;
        this.openOrClose = openOrClose;
        this.hours = hours;
        this.minutes = minutes;
    }
}
