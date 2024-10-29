package org.usco.parkingsystemapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.usco.parkingsystemapi.model.VehicleRecords;
import org.usco.parkingsystemapi.repository.VehicleRecordsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleRecordsService {

    private final VehicleRecordsRepository vehicleRecordsRepository;

    public ResponseEntity<String> createVehicleRecord(String vehiclePlate) {
        Optional<VehicleRecords> checkExistingRecord = vehicleRecordsRepository
                .findByVehiclePlateAndExitTimeIsNull(vehiclePlate);

        if (checkExistingRecord.isPresent()) {
            return ResponseEntity.badRequest().body("Vehicle already parked");
        } else {
            VehicleRecords vehicleRecords = new VehicleRecords();
            vehicleRecords.setVehiclePlate(vehiclePlate);
            vehicleRecords.setEntryTime(LocalDateTime.now());
            vehicleRecordsRepository.save(vehicleRecords);
            return ResponseEntity.status(201).body("Vehicle parked successfully");
        }
    }

    public ResponseEntity<String> exitVehicle(String vehiclePlate) {
        Optional<VehicleRecords> vehicleRecord = vehicleRecordsRepository
                .findByVehiclePlateAndExitTimeIsNull(vehiclePlate);

        if (vehicleRecord.isPresent()) {
            VehicleRecords vehicleRecords = vehicleRecord.get();
            vehicleRecords.setExitTime(LocalDateTime.now());
            vehicleRecordsRepository.save(vehicleRecords);
            return ResponseEntity.ok("Vehicle exited successfully");
        } else {
            return ResponseEntity.badRequest().body("Vehicle not found");
        }
    }

    public ResponseEntity<List<VehicleRecords>> getCurrentlyParkedVehicles() {
        List<VehicleRecords> vehicleRecords = vehicleRecordsRepository.findVehicleRecordsByExitTimeIsNull();
        return ResponseEntity.ok(vehicleRecords);
    }

    public ResponseEntity<List<VehicleRecords>> getVehicleRecords() {
        List<VehicleRecords> vehicleRecords = vehicleRecordsRepository.findAll();
        return ResponseEntity.ok(vehicleRecords);
    }

}
