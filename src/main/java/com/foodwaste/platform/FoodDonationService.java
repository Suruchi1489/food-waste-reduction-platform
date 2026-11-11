
package com.foodwaste.platform;

import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Map;
import java.util.HashMap;

@Service
public class FoodDonationService {

    private final FoodDonationRepository foodDonationRepository;

    public FoodDonationService(FoodDonationRepository foodDonationRepository) {
        this.foodDonationRepository = foodDonationRepository;
    }

    public FoodDonation addDonation(FoodDonation donation) {
        if (donation.getStatus() == null || donation.getStatus().isBlank()) {
            donation.setStatus("AVAILABLE");
        }

        return foodDonationRepository.save(donation);
    }

    public List<FoodDonation> getAllDonations() {
        return foodDonationRepository.findAll();
    }

    
public FoodDonation getDonationById(Long id) {
    return foodDonationRepository.findById(id)
            .orElseThrow(() -> new DonationNotFoundException(
                    "Food donation not found with id: " + id));
}

    public void deleteDonation(Long id) {
        if (!foodDonationRepository.existsById(id)) {
            throw new RuntimeException(
                    "Food donation not found with id: " + id);
        }

        foodDonationRepository.deleteById(id);
    }

public FoodDonation updateDonation(Long id, FoodDonation updatedDonation) {
    FoodDonation existingDonation = getDonationById(id);

    existingDonation.setFoodName(updatedDonation.getFoodName());
    existingDonation.setQuantity(updatedDonation.getQuantity());
    existingDonation.setDonorName(updatedDonation.getDonorName());
    existingDonation.setPickupLocation(updatedDonation.getPickupLocation());

    if (updatedDonation.getStatus() != null
            && !updatedDonation.getStatus().isBlank()) {
        existingDonation.setStatus(updatedDonation.getStatus());
    }

    return foodDonationRepository.save(existingDonation);
}


public FoodDonation updateDonationStatus(Long id, String status) {

    FoodDonation donation = getDonationById(id);

    if (status == null || status.isBlank()) {
        throw new IllegalArgumentException(
                "Donation status is required");
    }

    String newStatus = status.trim().toUpperCase();

    if (!newStatus.equals("AVAILABLE")
            && !newStatus.equals("CLAIMED")
            && !newStatus.equals("PICKED_UP")) {
        throw new IllegalArgumentException(
                "Invalid status. Use AVAILABLE, CLAIMED, or PICKED_UP");
    }

    donation.setStatus(newStatus);

    return foodDonationRepository.save(donation);
}

public List<FoodDonation> getDonationsByStatus(String status) {
    if (status == null || status.isBlank()) {
        throw new IllegalArgumentException(
                "Donation status is required");
    }

    String searchStatus = status.trim().toUpperCase();

    if (!searchStatus.equals("AVAILABLE")
            && !searchStatus.equals("CLAIMED")
            && !searchStatus.equals("PICKED_UP")) {
        throw new IllegalArgumentException(
                "Invalid status. Use AVAILABLE, CLAIMED, or PICKED_UP");
    }

    return foodDonationRepository.findByStatus(searchStatus);
}

public List<FoodDonation> searchDonationsByFoodName(String foodName) {
    if (foodName == null || foodName.isBlank()) {
        throw new IllegalArgumentException(
                "Food name is required for searching");
    }

    return foodDonationRepository
            .findByFoodNameContainingIgnoreCase(foodName.trim());
}


public List<FoodDonation> searchDonationsByLocation(
        String pickupLocation) {

    if (pickupLocation == null || pickupLocation.isBlank()) {
        throw new IllegalArgumentException(
                "Pickup location is required for searching");
    }

    return foodDonationRepository
            .findByPickupLocationContainingIgnoreCase(
                    pickupLocation.trim());
}
public FoodDonation claimDonation(Long id) {
    FoodDonation donation = getDonationById(id);

    if (!"AVAILABLE".equalsIgnoreCase(donation.getStatus())) {
        throw new IllegalArgumentException(
                "Only AVAILABLE donations can be claimed");
    }

    donation.setStatus("CLAIMED");

    return foodDonationRepository.save(donation);
}


public FoodDonation confirmPickup(Long id) {
    FoodDonation donation = getDonationById(id);

    if (!"CLAIMED".equalsIgnoreCase(donation.getStatus())) {
        throw new IllegalArgumentException(
                "Only CLAIMED donations can be picked up");
    }

    donation.setStatus("PICKED_UP");

    return foodDonationRepository.save(donation);
}



public Map<String, Long> getDonationSummary() {
    long totalDonations = foodDonationRepository.count();

    long availableDonations =
            foodDonationRepository.findByStatus("AVAILABLE").size();

    long claimedDonations =
            foodDonationRepository.findByStatus("CLAIMED").size();

    long pickedUpDonations =
            foodDonationRepository.findByStatus("PICKED_UP").size();

    Map<String, Long> summary = new HashMap<>();

    summary.put("totalDonations", totalDonations);
    summary.put("availableDonations", availableDonations);
    summary.put("claimedDonations", claimedDonations);
    summary.put("pickedUpDonations", pickedUpDonations);

    return summary;
}


}
