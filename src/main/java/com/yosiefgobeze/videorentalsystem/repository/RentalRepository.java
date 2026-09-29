package com.yosiefgobeze.videorentalsystem.repository;

import com.yosiefgobeze.videorentalsystem.model.Rental;
import com.yosiefgobeze.videorentalsystem.model.User;
import com.yosiefgobeze.videorentalsystem.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserAndActiveTrue(User user);
    Optional<Rental> findByVideoAndActiveTrue(Video video);
}
