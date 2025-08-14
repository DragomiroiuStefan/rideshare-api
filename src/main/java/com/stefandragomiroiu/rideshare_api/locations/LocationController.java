package com.stefandragomiroiu.rideshare_api.locations;

import com.stefandragomiroiu.rideshare_api.web.validation.Update;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/locations")
class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LocationRepository locationRepository;

    LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public List<Location> findByCityContaining(@RequestParam(required = false) String city) {
        return switch (city) {
            case String value -> locationRepository.findByCityContainingIgnoreCase(value);
            case null -> locationRepository.findAll();
        };
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<Location> findById(@PathVariable Long locationId) {
        return ResponseEntity.of(
                locationRepository.findById(locationId)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create:location')")
    public ResponseEntity<Location> create(@Valid @RequestBody Location location, UriComponentsBuilder uriBuilder) {
        logger.info("Create request for location: {}", location);
        var created = locationRepository.save(location);
        var newUri = uriBuilder.path("/locations/{locationId}").build(created.locationId());
        return ResponseEntity.created(newUri).body(created);
    }

    @PutMapping("/{locationId}")
    @PreAuthorize("hasAuthority('update:location')")
    public ResponseEntity<Location> update(@PathVariable Long locationId, @Validated(Update.class) @RequestBody Location location) {
        logger.info("Update request for location: {}", location);
        if (locationRepository.findById(locationId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Location updated = locationRepository.save(location);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/{locationId}")
    @PreAuthorize("hasAuthority('delete:location')")
    public ResponseEntity<Void> delete(@PathVariable Long locationId) {
        logger.info("Delete request for location with ID: {}", locationId);
        locationRepository.deleteById(locationId);
        return ResponseEntity.ok().build();
    }
}
