package com.example.beginnerfitbe.youtube.util;

import com.example.beginnerfitbe.youtube.domain.YoutubeVideo;
import com.example.beginnerfitbe.youtube.dto.YoutubeSearchResDto;
import com.example.beginnerfitbe.youtube.dto.YoutubeVideoDto;
import com.example.beginnerfitbe.youtube.repository.YoutubeVideoRepository;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class YoutubeUtil {

    @Value("${youtube.api.key}")
    private String apiKey;

    private final YoutubeVideoRepository youtubeRepository;

    // 비디오 검색
    public List<YoutubeSearchResDto> search(String keyword) throws IOException {
        JsonFactory jsonFactory = new JacksonFactory();
        YouTube youtube = new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                jsonFactory,
                request -> {
                })
                .setApplicationName("youtube-search")
                .build();

        YouTube.Search.List search = youtube.search().list(Collections.singletonList("id,snippet"));

        search.setKey(apiKey);
        search.setQ(keyword);
        search.setMaxResults(20L);

        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();

        if (searchResultList != null && !searchResultList.isEmpty()) {
            return searchResultList.stream()
                    .filter(searchResult -> "youtube#video".equals(searchResult.getId().getKind()))
                    .map(searchResult -> {
                        String videoId = searchResult.getId().getVideoId();
                        String videoTitle = searchResult.getSnippet().getTitle();
                        String videoDescription = searchResult.getSnippet().getDescription();
                        String videoChannel = searchResult.getSnippet().getChannelTitle();
                        String publishedAt = searchResult.getSnippet().getPublishedAt().toString();
                        String videoThumbnail = searchResult.getSnippet().getThumbnails().getHigh().getUrl();
                        String duration = videoInfo(youtube, videoId);

                        return YoutubeSearchResDto.builder()
                                .videoId(videoId)
                                .url("https://www.youtube.com/watch?v=" + videoId)
                                .title(videoTitle)
                                .description(videoDescription)
                                .channel(videoChannel)
                                .publishedAt(publishedAt)
                                .thumbnail(videoThumbnail)
                                .duration(duration)
                                .build();
                    })
                    .collect(Collectors.toList());
        }
        System.out.println("No search results found.");
        return Collections.emptyList();
    }

    // 비디오 메타데이터
    public String videoInfo(YouTube youtube, String videoId) {
        try {
            YouTube.Videos.List videosListByIdRequest = youtube.videos().list(Collections.singletonList("contentDetails"));
            videosListByIdRequest.setKey(apiKey);

            videosListByIdRequest.setId(Collections.singletonList(videoId));
            VideoListResponse videoListResponse = videosListByIdRequest.execute();

            List<Video> videoList = videoListResponse.getItems();
            if (videoList != null && !videoList.isEmpty()) {
                Video video = videoList.get(0);
                return convertISO8601Duration(video.getContentDetails().getDuration());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ISO 8601 format 변환
    public String convertISO8601Duration(String isoDuration) {
        Pattern pattern = Pattern.compile("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?");
        Matcher matcher = pattern.matcher(isoDuration);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not ISO 8601");
        }

        String hours = matcher.group(1) != null ? matcher.group(1) : "0";
        String minutes = matcher.group(2) != null ? matcher.group(2) : "0";
        String seconds = matcher.group(3) != null ? matcher.group(3) : "0";

        int h = Integer.parseInt(hours);
        int m = Integer.parseInt(minutes);
        int s = Integer.parseInt(seconds);

        if (h > 0) {
            return String.format("%02d:%02d:%02d", h, m, s);
        } else if (m > 0) {
            return String.format("%02d:%02d", m, s);
        } else {
            return String.format("00:%02d", s);
        }
    }

    //search한 유튜브 영상 중 플레이리스트로 만들 영상 선택
    public YoutubeVideoDto selectVideos(String keyword, String requestTime) throws IOException {
        int targetTime = Integer.parseInt(requestTime);
        List<YoutubeVideo> selectedVideos = new ArrayList<>();
        double totalTime = 0;

        // 만약 시간을 만족하지 못하면, 검색 api 더 호출
        while (totalTime < targetTime -3) { //-3분까지는 허용
            List<YoutubeSearchResDto> youtubeSearchResDtos = search(keyword);
            if (youtubeSearchResDtos.isEmpty()) {
                break;
            }
            List<YoutubeVideo> newVideos = findOptimalVideos(youtubeSearchResDtos, targetTime - (int) totalTime);
            selectedVideos.addAll(newVideos);

            totalTime = selectedVideos.stream().mapToDouble(video -> convertDurationToMinutes(video.getDuration())).sum();
        }

        String totalTimeFormatted = convertMinutesToDurationFormat(totalTime);

        System.out.println("전체 시간: " + totalTimeFormatted);

        YoutubeVideoDto youtubeVideoDto = new YoutubeVideoDto();
        youtubeVideoDto.setYoutubeVideos(selectedVideos);
        youtubeVideoDto.setTotalTime(totalTimeFormatted);

        return youtubeVideoDto;
    }

    private List<YoutubeVideo> findOptimalVideos(List<YoutubeSearchResDto> youtubeSearchResDtos, int targetTime) {
        int n = youtubeSearchResDtos.size();
        double[] durations = new double[n];
        YoutubeVideo[] videos = new YoutubeVideo[n];

        for (int i = 0; i < n; i++) {
            durations[i] = convertDurationToMinutes(youtubeSearchResDtos.get(i).getDuration());
            videos[i] = youtubeSearchResDtos.get(i).toEntity();
        }

        List<YoutubeVideo> bestSelection = new ArrayList<>();
        dfs(videos, durations, 0, targetTime, 0, 0, new ArrayList<>(), bestSelection);

        return bestSelection;
    }

    //dfs로 최적화 플레이리스트 생성
    private double dfs(YoutubeVideo[] videos, double[] durations, int index, int targetTime, double currentTime, double closestTime, List<YoutubeVideo> currentSelection, List<YoutubeVideo> bestSelection) {
        if (currentTime > targetTime + 3) { // 3분 초과까지 허용
            return closestTime;
        }
        if (currentTime > closestTime) {
            closestTime = currentTime;
            bestSelection.clear();
            bestSelection.addAll(new ArrayList<>(currentSelection));
        }
        for (int i = index; i < videos.length; i++) {
            currentSelection.add(videos[i]);
            closestTime = dfs(videos, durations, i + 1, targetTime, currentTime + durations[i], closestTime, currentSelection, bestSelection);
            currentSelection.remove(currentSelection.size() - 1);
        }
        return closestTime;
    }

    // 분으로 변환
    private double convertDurationToMinutes(String duration) {
        String[] parts = duration.split(":");
        int minutes = 0;
        int seconds = 0;

        if (parts.length == 2) { // MM:SS
            minutes = Integer.parseInt(parts[0]);
            seconds = Integer.parseInt(parts[1]);
        } else if (parts.length == 3) { // HH:MM:SS
            int hours = Integer.parseInt(parts[0]);
            minutes = hours * 60 + Integer.parseInt(parts[1]);
            seconds = Integer.parseInt(parts[2]);
        }

        return minutes + (seconds / 60.0);
    }

    // 시간 포맷
    private String convertMinutesToDurationFormat(double totalMinutes) {
        int minutes = (int) totalMinutes;
        int seconds = (int) Math.round((totalMinutes - minutes) * 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
}
