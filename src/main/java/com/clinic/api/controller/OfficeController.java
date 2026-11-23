package com.clinic.api.controller;

import com.clinic.api.model.Office;
import com.clinic.api.service.OfficeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/offices")
@CrossOrigin(origins = {
        "http://localhost:5500", "http://127.0.0.1:5500",
        "http://localhost:5173", "http://127.0.0.1:5173",
        "http://localhost:4173", "http://127.0.0.1:4173"
})
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping
    public List<Office> getAllOffices() {
        return officeService.findAll();
    }

    @GetMapping("/busiest")
    public Object getBusiestOffice() {
        List<Object[]> offices = officeService.findBusiestOffice();
        if (offices.isEmpty()) {
            return "No appointments yet";
        }
        return offices.get(0);
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