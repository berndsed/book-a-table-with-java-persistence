package de.kieseltaucher.studies.persistence.restaurant.model;

class TableBuilder {

    private int number = 1;

    Table build() {
        return new Table(TableNumber.of(number));
    }

}
