package de.kieseltaucher.studies.persistence.restaurant.service;

import java.util.Collection;

import de.kieseltaucher.studies.persistence.restaurant.model.ReservationRequest;
import de.kieseltaucher.studies.persistence.restaurant.model.Table;
import de.kieseltaucher.studies.persistence.restaurant.model.TableNumber;

public interface TableDAO {

    Table insert(TableNumber number);
    Collection<Table> findAll();
    boolean insertReservation(TableNumber tableNumber, ReservationRequest reservation);

}
