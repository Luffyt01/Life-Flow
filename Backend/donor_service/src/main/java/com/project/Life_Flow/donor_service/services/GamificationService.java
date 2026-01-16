package com.project.Life_Flow.donor_service.services;

import com.project.Life_Flow.donor_service.dto.*;

import java.util.List;
import java.util.UUID;

public interface GamificationService {
    GamificationProfileDto getGamificationProfile(UUID donorId);
    AddPointsResponseDto addPoints(UUID donorId, AddPointsRequestDto requestDto);
    BadgeUpdateDto checkAndUpdateBadge(UUID donorId);
    List<AchievementDto> getAchievements(UUID donorId);
    AwardAchievementResponseDto awardAchievement(UUID donorId, AwardAchievementRequestDto requestDto);
    List<LeaderboardEntryDto> getLeaderboard(String timeframe, UUID centerId, int limit);
    ReferralDto createReferral(UUID donorId);
    UseReferralResponseDto useReferral(UseReferralRequestDto requestDto);
    DonationStreakDto getDonationStreak(UUID donorId);
}
