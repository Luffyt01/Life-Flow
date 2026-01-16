package com.project.Life_Flow.donor_service.controllers;

import com.project.Life_Flow.donor_service.dto.*;
import com.project.Life_Flow.donor_service.services.GamificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/donor")
public class GamificationController {

    private final GamificationService gamificationService;

    public GamificationController(GamificationService gamificationService) {
        this.gamificationService = gamificationService;
    }

    @GetMapping("/{donor_id}/gamification")
    public ResponseEntity<GamificationProfileDto> getGamificationProfile(@PathVariable("donor_id") UUID donorId) {
        return ResponseEntity.ok(gamificationService.getGamificationProfile(donorId));
    }

    @PostMapping("/{donor_id}/points/add")
    public ResponseEntity<AddPointsResponseDto> addPoints(@PathVariable("donor_id") UUID donorId, @Valid @RequestBody AddPointsRequestDto requestDto) {
        return ResponseEntity.ok(gamificationService.addPoints(donorId, requestDto));
    }

    @PostMapping("/{donor_id}/badge/check")
    public ResponseEntity<BadgeUpdateDto> checkAndUpdateBadge(@PathVariable("donor_id") UUID donorId) {
        return ResponseEntity.ok(gamificationService.checkAndUpdateBadge(donorId));
    }

    @GetMapping("/{donor_id}/achievements")
    public ResponseEntity<List<AchievementDto>> getAchievements(@PathVariable("donor_id") UUID donorId) {
        return ResponseEntity.ok(gamificationService.getAchievements(donorId));
    }

    @PostMapping("/{donor_id}/achievements/award")
    public ResponseEntity<AwardAchievementResponseDto> awardAchievement(@PathVariable("donor_id") UUID donorId, @RequestBody AwardAchievementRequestDto requestDto) {
        return new ResponseEntity<>(gamificationService.awardAchievement(donorId, requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboard(
            @RequestParam(required = false) String timeframe,
            @RequestParam(required = false) UUID center_id,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(gamificationService.getLeaderboard(timeframe, center_id, limit));
    }

    @PostMapping("/{donor_id}/referral")
    public ResponseEntity<ReferralDto> createReferral(@PathVariable("donor_id") UUID donorId) {
        return new ResponseEntity<>(gamificationService.createReferral(donorId), HttpStatus.CREATED);
    }

    @PostMapping("/referral/use")
    public ResponseEntity<UseReferralResponseDto> useReferral(@RequestBody UseReferralRequestDto requestDto) {
        return ResponseEntity.ok(gamificationService.useReferral(requestDto));
    }

    @GetMapping("/{donor_id}/streak")
    public ResponseEntity<DonationStreakDto> getDonationStreak(@PathVariable("donor_id") UUID donorId) {
        return ResponseEntity.ok(gamificationService.getDonationStreak(donorId));
    }
}
