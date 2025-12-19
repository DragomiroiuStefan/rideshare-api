package com.stefandragomiroiu.rideshare_api.vehicles;

import com.stefandragomiroiu.rideshare_api.web.validation.Update;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }


    @GetMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> findById(@PathVariable Long vehicleId) {
        return ResponseEntity.of(
                vehicleRepository.findById(vehicleId)
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('create:vehicle')")
    public ResponseEntity<CreateVehicleResponse> create(
            @RequestPart("vehicle") @Valid Vehicle vehicle,
            @RequestPart("image") MultipartFile imageFile,
            UriComponentsBuilder uriBuilder) {

        logger.info("Create request for vehicle: {}", vehicle);

        if (vehicleRepository.findById(vehicle.plateNumber()).isPresent()) {
            String errorMessage = String.format(VEHICLE_ALREADY_EXISTS_ERROR_MESSAGE, vehicle.getPlateNumber());
            throw new ResourceAlreadyExistsException(errorMessage);
        }
        if (userRepository.findOptionalById(vehicle.getOwner()).isEmpty()) {
            String errorMessage = String.format(USER_NOT_FOUND_ERROR_MESSAGE, vehicle.getOwner());
            throw new ResourceNotFoundException(errorMessage);
        }

        var created = vehicleRepository.save(vehicle);

        // TODO accept just some extensions

        var imageUploadSuccess = saveImage(imageFile, created.plateNumber().toString());

        var response = new CreateVehicleResponse(
                created,
                imageUploadSuccess ? null : "Vehicle image file upload failed. Vehicle created successfully."
        );

        var newUri = uriBuilder.path("/vehicles/{vehicleId}").build(created.plateNumber());
        return ResponseEntity.created(newUri).body(response);
    }

    private boolean saveImage(MultipartFile imageFile, String plateNumber) {
        try {
            // Get file extension
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg"; // Default extension if none found

            String newFileName = plateNumber + fileExtension;

            // Define storage location
            Path uploadDir = Paths.get("uploads/vehicles"); // TODO property
            Files.createDirectories(uploadDir);
            Path destination = uploadDir.resolve(newFileName);

            // Save the file
            Files.copy(imageFile.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            // TODO remove image from Vehicles table

            logger.info("Image file saved successfully for vehicle : {}", plateNumber);
            return true;

        } catch (IOException e) {
            logger.error("Failed to save image file for vehicle: {}", plateNumber, e);
            return false;
        }
    }


    @PutMapping("/{vehicleId}")
    @PreAuthorize("hasAuthority('update:vehicle')")
    public ResponseEntity<Vehicle> update(@PathVariable Long vehicleId, @Validated @RequestBody Vehicle vehicle) {
        logger.info("Update request for vehicle: {}", vehicle);
        if (vehicleRepository.findById(vehicleId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Vehicle updated = vehicleRepository.save(vehicle);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping("/{vehicleId}")
    @PreAuthorize("hasAuthority('delete:vehicle') or #ownerId == authentication.principal.userId")
    public ResponseEntity<Void> delete(@PathVariable Long vehicleId, @PathVariable Long ownerId) {
        logger.info("Delete request for vehicle with ID: {}", vehicleId);
        vehicleRepository.deleteById(vehicleId);
        return ResponseEntity.ok().build();
    }

}
