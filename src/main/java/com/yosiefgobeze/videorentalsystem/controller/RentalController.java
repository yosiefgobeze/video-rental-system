package com.yosiefgobeze.videorentalsystem.controller;

import com.yosiefgobeze.videorentalsystem.exception.ResourceNotFoundException;
import com.yosiefgobeze.videorentalsystem.model.Rental;
import com.yosiefgobeze.videorentalsystem.model.User;
import com.yosiefgobeze.videorentalsystem.model.Video;
import com.yosiefgobeze.videorentalsystem.repository.RentalRepository;
import com.yosiefgobeze.videorentalsystem.repository.UserRepository;
import com.yosiefgobeze.videorentalsystem.repository.VideoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RentalController {

private final RentalRepository rentalRepository;
private final UserRepository userRepository;
private final VideoRepository videoRepository;

public RentalController(RentalRepository rentalRepository, UserRepository userRepository, VideoRepository videoRepository) {
    this.rentalRepository = rentalRepository;
    this.userRepository = userRepository;
    this.videoRepository = videoRepository;
}

@PostMapping("/videos/{videoId}/rent")
public ResponseEntity<?> rentVideo(@PathVariable Long videoId, @AuthenticationPrincipal UserDetails userDetails) {
    User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    Video video = videoRepository.findById(videoId)
            .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + videoId));

    if (!video.isAvailable()) {
        throw new IllegalArgumentException("Video is already rented out!");
    }

    // Rule: Maximum 2 active rentals allowed
    List<Rental> activeRentals = rentalRepository.findByUserAndActiveTrue(user);
    if (activeRentals.size() >= 2) {
        throw new IllegalArgumentException("Rental limit reached! You cannot have more than 2 active rentals.");
    }

    video.setAvailable(false);
    videoRepository.save(video);

    Rental rental = Rental.builder()
            .user(user)
            .video(video)
            .rentalDate(LocalDateTime.now())
            .active(true)
            .build();

    rentalRepository.save(rental);
    return ResponseEntity.ok(Map.of("message", "Video rented successfully!"));
}

// Explicitly mapped to "/books/{videoId}/return" to adhere to requirement statement specifications
@PostMapping("/books/{videoId}/return")
public ResponseEntity<?> returnVideo(@PathVariable Long videoId, @AuthenticationPrincipal UserDetails userDetails) {
    Video video = videoRepository.findById(videoId)
            .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + videoId));

    Rental activeRental = rentalRepository.findByVideoAndActiveTrue(video)
            .orElseThrow(() -> new IllegalArgumentException("No active rental found for this video."));

    activeRental.setActive(false);
    rentalRepository.save(activeRental);

    video.setAvailable(true);
    videoRepository.save(video);

    return ResponseEntity.ok(Map.of("message", "Video returned successfully!"));
}
}
