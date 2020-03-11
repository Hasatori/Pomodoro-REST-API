package com.pomodoro.utils;

public enum SizeUnit {

    B(1),
    KB((long) Math.pow(1024, 1)),
    MB((long) Math.pow(1024, 2)),
    GB((long) Math.pow(1024, 3));

    private final long inByte;

    SizeUnit(long inByte) {
        this.inByte = inByte;
    }

    public long getInByte() {
        return this.inByte;
    }
}
