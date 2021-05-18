package de.kieseltaucher.studies.persistence.restaurant.model;

import java.util.Objects;

public class TableNumber {

    public static TableNumber of(int value) {
        return new TableNumber(value);
    }

    private final int number;

    private TableNumber(int number) {
        this.number = number;
    }

    public int toInt() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableNumber that = (TableNumber) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }
}
