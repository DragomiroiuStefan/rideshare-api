package com.stefandragomiroiu.rideshare_api.locations;

import com.stefandragomiroiu.rideshare_api.web.exceptions.ResourceNotFoundException;
import com.stefandragomiroiu.rideshare_api.web.validation.Update;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Location> findAll(@RequestParam(required = false) String city) {
        return switch (city) {
            case String value -> locationRepository.findByCityContainingIgnoreCase(value);
            case null -> locationRepository.findAll();
        };
    }

    @GetMapping("/{locationId}")
    public Location findById(@PathVariable Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException(Location.class, locationId));
    }

    @PostMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Location> create(@Valid @RequestBody Location location, UriComponentsBuilder uriBuilder) {
        logger.info("Create request for location: {}", location);
        var created = locationRepository.save(location);
        var newUri = uriBuilder.path("/locations/{locationId}").build(created.locationId());
        return ResponseEntity.created(newUri).body(created);
    }

    @PutMapping("/{locationId}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Location> update(@PathVariable Long locationId, @Validated(Update.class) @RequestBody Location location) {
        logger.info("Update request for location: {}", location);
        if (locationRepository.findById(locationId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Location updated = locationRepository.save(location);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/{locationId}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long locationId) {
        logger.info("Delete request for location with ID: {}", locationId);
        locationRepository.deleteById(locationId);
        return ResponseEntity.ok().build();
    }
}
