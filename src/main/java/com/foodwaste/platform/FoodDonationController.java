
package com.foodwaste.platform;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("/api/donations")
public class FoodDonationController {

    private final FoodDonationService foodDonationService;

    public FoodDonationController(FoodDonationService foodDonationService) {
        this.foodDonationService = foodDonationService;
    }

    @PostMapping
    public FoodDonation addDonation(@Valid @RequestBody FoodDonation donation) {
        return foodDonationService.addDonation(donation);
    }

    @GetMapping
    public List<FoodDonation> getAllDonations() {
        return foodDonationService.getAllDonations();
    }

    @GetMapping("/{id}")
    public FoodDonation getDonationById(@PathVariable Long id) {
        return foodDonationService.getDonationById(id);
    }

    @PutMapping("/{id}")
    public FoodDonation updateDonation(
            @PathVariable Long id,
            @RequestBody FoodDonation updatedDonation) {
        return foodDonationService.updateDonation(id, updatedDonation);
    }

    @DeleteMapping("/{id}")
    public String deleteDonation(@PathVariable Long id) {
        foodDonationService.deleteDonation(id);
        return "Food donation deleted successfully";
    }

    
@PatchMapping("/{id}/status")
public FoodDonation updateDonationStatus(
        @PathVariable Long id,
        @RequestBody java.util.Map<String, String> request) {

    String status = request.get("status");

    return foodDonationService.updateDonationStatus(id, status);
}

@GetMapping("/status/{status}")
public List<FoodDonation> getDonationsByStatus(
        @PathVariable String status) {
    return foodDonationService.getDonationsByStatus(status);
}


@GetMapping("/search")
public List<FoodDonation> searchDonationsByFoodName(
        @RequestParam String foodName) {
    return foodDonationService.searchDonationsByFoodName(foodName);
}

@GetMapping("/location/{location}")
public List<FoodDonation> searchDonationsByLocation(
        @PathVariable String location) {
    return foodDonationService
            .searchDonationsByLocation(location);
}

@PatchMapping("/{id}/claim")
public FoodDonation claimDonation(@PathVariable Long id) {
    return foodDonationService.claimDonation(id);
}


@PatchMapping("/{id}/pickup")
public FoodDonation confirmPickup(@PathVariable Long id) {
    return foodDonationService.confirmPickup(id);
}


@GetMapping("/summary")
public Map<String, Long> getDonationSummary() {
    return foodDonationService.getDonationSummary();
}


}