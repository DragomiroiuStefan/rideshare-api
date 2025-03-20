package com.stefandragomiroiu.rideshare_api.locations;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LocationRepository extends ListCrudRepository<Location, Long> {
    List<Location> findByCityContainingIgnoreCase(String city);
}
