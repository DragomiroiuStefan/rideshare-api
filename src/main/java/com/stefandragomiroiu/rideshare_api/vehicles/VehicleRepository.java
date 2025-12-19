package com.stefandragomiroiu.rideshare_api.vehicles;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface VehicleRepository extends ListCrudRepository<Vehicle, Long> {
}
