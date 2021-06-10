package de.kieseltaucher.studies.persistence.restaurant.service;

import de.kieseltaucher.studies.persistence.restaurant.model.ReservationRequest;
import de.kieseltaucher.studies.persistence.restaurant.model.Table;

class ReservationService {

    private final TableDAO tableDAO;

    ReservationService(TableDAO tableDAO) {
        this.tableDAO = tableDAO;
    }

    String listAllTables() {
        final TableStringRenderer renderer = new TableStringRenderer();
        tableDAO.findAll()
            .forEach(renderer::add);
        return renderer.toString();
    }

    String reserve(ReservationRequest request) {
        final TableStringRenderer renderer = new TableStringRenderer();
        for (Table table : tableDAO.findAll()) {
            if (table.reserve(request)) {
                tableDAO.insertReservation(table.getNumber(), request);
                renderer.add(table);
                break;
            }
        }
        return renderer.toString();
    }

    private static class TableStringRenderer {

        private final StringBuilder rendered = new StringBuilder();

        void add(Table table) {
            if (rendered.length() > 0) {
                rendered.append('\n');
            }
            rendered.append(table.renderAsString());
        }

        @Override
        public String toString() {
            return rendered.toString();
        }
    }

}
