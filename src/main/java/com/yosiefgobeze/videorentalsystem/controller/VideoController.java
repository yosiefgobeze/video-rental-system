package com.yosiefgobeze.videorentalsystem.controller;

import com.yosiefgobeze.videorentalsystem.dto.VideoDto;
import com.yosiefgobeze.videorentalsystem.exception.ResourceNotFoundException;
import com.yosiefgobeze.videorentalsystem.model.Video;
import com.yosiefgobeze.videorentalsystem.repository.VideoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VideoController {

    private final VideoRepository videoRepository;

    public VideoController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    // --- Private Endpoint (Accessible by Authenticated CUSTOMER and ADMIN) ---
    @GetMapping("/videos")
    public List<Video> getAllAvailableVideos() {
        return videoRepository.findByIsAvailableTrue();
    }

    // --- Admin Endpoints (Accessible only by ADMIN) ---
    @PostMapping("/admin/videos")
    public ResponseEntity<Video> createVideo(@RequestBody VideoDto videoDto) {
        Video video = Video.builder()
                .title(videoDto.getTitle())
                .director(videoDto.getDirector())
                .genre(videoDto.getGenre())
                .isAvailable(true)
                .build();
        return new ResponseEntity<>(videoRepository.save(video), HttpStatus.CREATED);
    }

    @PutMapping("/admin/videos/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable Long id, @RequestBody VideoDto videoDto) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));

        video.setTitle(videoDto.getTitle());
        video.setDirector(videoDto.getDirector());
        video.setGenre(videoDto.getGenre());

        return ResponseEntity.ok(videoRepository.save(video));
    }

    @DeleteMapping("/admin/videos/{id}")
    public ResponseEntity<?> deleteVideo(@PathVariable Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));

        videoRepository.delete(video);
        return ResponseEntity.ok(Map.of("message", "Video deleted successfully"));
    }
}
