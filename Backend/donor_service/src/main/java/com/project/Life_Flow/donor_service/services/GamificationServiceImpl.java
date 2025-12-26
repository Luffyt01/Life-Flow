package com.project.Life_Flow.donor_service.services;

import com.project.Life_Flow.donor_service.dto.*;
import com.project.Life_Flow.donor_service.entities.Gamification;
import com.project.Life_Flow.donor_service.entities.enums.BadgeLevel;
import com.project.Life_Flow.donor_service.exception.ResourceNotFoundException;
import com.project.Life_Flow.donor_service.repositories.GamificationRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GamificationServiceImpl implements GamificationService {

    private final GamificationRepository gamificationRepository;

    public GamificationServiceImpl(GamificationRepository gamificationRepository) {
        this.gamificationRepository = gamificationRepository;
    }

    @Override
    public GamificationProfileDto getGamificationProfile(UUID donorId) {
        Gamification gamification = gamificationRepository.findByDonorId(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Gamification profile not found for donor with id: " + donorId));

        return GamificationProfileDto.builder()
                .points(gamification.getTotalPoints())
                .badgeLevel(gamification.getBadgeLevel())
                .donationsCount(gamification.getDonationsCount())
                .emergencyResponses(gamification.getEmergencyResponses())
                .livesSaved(gamification.getLivesSavedEstimate())
                .streakDays(gamification.getStreakDays())
                .achievements(getAchievements(donorId))
                .build();
    }

    @Override
    public AddPointsResponseDto addPoints(UUID donorId, AddPointsRequestDto requestDto) {
        Gamification gamification = gamificationRepository.findByDonorId(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Gamification profile not found for donor with id: " + donorId));

        int newTotalPoints = gamification.getTotalPoints() + requestDto.getPoints();
        gamification.setTotalPoints(newTotalPoints);

        if (gamification.getPointsHistory() == null) {
            gamification.setPointsHistory(new ArrayList<>());
        }
        gamification.getPointsHistory().add(new Gamification.PointsHistory(requestDto.getPoints(), requestDto.getReason(), new Timestamp(System.currentTimeMillis())));
        gamification.setLastUpdated(new Timestamp(System.currentTimeMillis()));

        gamificationRepository.save(gamification);

        return AddPointsResponseDto.builder()
                .newTotal(newTotalPoints)
                .pointsAdded(requestDto.getPoints())
                .build();
    }

    @Override
    public BadgeUpdateDto checkAndUpdateBadge(UUID donorId) {
        Gamification gamification = gamificationRepository.findByDonorId(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Gamification profile not found for donor with id: " + donorId));

        BadgeLevel oldBadge = gamification.getBadgeLevel();
        BadgeLevel newBadge = oldBadge; // Determine new badge based on points or other criteria

        if (gamification.getTotalPoints() > 1000 && oldBadge.equals(BadgeLevel.BRONZE)) {
            newBadge = BadgeLevel.SILVER;
        }

        gamification.setBadgeLevel(newBadge);
        gamificationRepository.save(gamification);

        return BadgeUpdateDto.builder()
                .oldBadge(oldBadge)
                .newBadge(newBadge)
                .achievedAt(newBadge != oldBadge ? LocalDateTime.now() : null)
                .build();
    }

    @Override
    public List<AchievementDto> getAchievements(UUID donorId) {
        Gamification gamification = gamificationRepository.findByDonorId(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Gamification profile not found for donor with id: " + donorId));

        if (gamification.getAchievements() == null) {
            return new ArrayList<>();
        }

        return gamification.getAchievements().stream()
                .map(achievement -> AchievementDto.builder()
                        .achievementId(null) // Achievements in Gamification entity don't have IDs
                        .name(achievement.getAchievementName())
                        .earnedDate(achievement.getDateAwarded().toLocalDate())
                        .description("") // Placeholder
                        .points(0) // Placeholder
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public AwardAchievementResponseDto awardAchievement(UUID donorId, AwardAchievementRequestDto requestDto) {
        Gamification gamification = gamificationRepository.findByDonorId(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Gamification profile not found for donor with id: " + donorId));

        if (gamification.getAchievements() == null) {
            gamification.setAchievements(new ArrayList<>());
        }

        Gamification.Achievement newAchievement = new Gamification.Achievement(requestDto.getAchievementName(), new Date(System.currentTimeMillis()));
        gamification.getAchievements().add(newAchievement);
        gamification.setTotalPoints(gamification.getTotalPoints() + requestDto.getPoints());
        gamificationRepository.save(gamification);

        return AwardAchievementResponseDto.builder()
                .achievementId(null) // Not applicable here
                .awardedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public List<LeaderboardEntryDto> getLeaderboard(String timeframe, UUID centerId, int limit) {
        // This is a simplified implementation. A real implementation would involve a more complex query
        // to filter by timeframe and center, and to join with a donor information table to get the donor's name.
        return gamificationRepository.findAll().stream()
                .sorted((g1, g2) -> g2.getTotalPoints().compareTo(g1.getTotalPoints()))
                .limit(limit)
                .map(gamification -> LeaderboardEntryDto.builder()
                        .donorId(gamification.getDonorId())
                        .name("Unknown") // Placeholder
                        .rank(gamification.getRankingPosition())
                        .points(gamification.getTotalPoints())
                        .donations(gamification.getDonationsCount())
                        .emergencyResponses(gamification.getEmergencyResponses())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ReferralDto createReferral(UUID donorId) {
        // Business logic to create a referral code
        String referralCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return ReferralDto.builder()
                .referralCode(referralCode)
                .expiresAt(null) // Placeholder
                .build();
    }

    @Override
    public UseReferralResponseDto useReferral(UseReferralRequestDto requestDto) {
        // Business logic to use a referral code
        return UseReferralResponseDto.builder()
                .pointsAwarded(50) // Placeholder
                .referrerPoints(100) // Placeholder
                .build();
    }

    @Override
    public DonationStreakDto getDonationStreak(UUID donorId) {
        Gamification gamification = gamificationRepository.findByDonorId(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Gamification profile not found for donor with id: " + donorId));

        return DonationStreakDto.builder()
                .streakDays(gamification.getStreakDays())
                .build();
    }
}
