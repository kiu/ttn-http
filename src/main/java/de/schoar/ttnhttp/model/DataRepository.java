package de.schoar.ttnhttp.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<Data, String> {
}
