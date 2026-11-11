
package com.foodwaste.platform;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodDonationRepository
        extends JpaRepository<FoodDonation, Long> {

    List<FoodDonation> findByStatus(String status);

    List<FoodDonation> findByFoodNameContainingIgnoreCase(String foodName);

    
    List<FoodDonation> findByPickupLocationContainingIgnoreCase(
        String pickupLocation);
}
