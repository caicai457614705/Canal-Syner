package com.faker.canal.enums;

/**
 * Created by faker on 18/2/6.
 */
public enum ChangeType {

    INSERT(1),
    UPDATE(2),
    DELETE(3);

    private int value;

    ChangeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
