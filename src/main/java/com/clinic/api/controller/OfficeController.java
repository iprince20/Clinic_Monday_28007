package com.clinic.api.controller;

import com.clinic.api.model.Office;
import com.clinic.api.service.OfficeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping
    public List<Office> getAllOffices() {
        return officeService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Office> getOfficeById(@PathVariable Long id) {
        return officeService.findById(id);
    }

    @PostMapping
    public Office createOffice(@RequestBody Office office) {
        return officeService.save(office);
    }

    @PutMapping("/{id}")
    public Office updateOffice(@PathVariable Long id, @RequestBody Office officeDetails) {
        return officeService.update(id, officeDetails).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteOffice(@PathVariable Long id) {
        officeService.deleteById(id);
    }
}