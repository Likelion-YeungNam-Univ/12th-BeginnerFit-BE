package com.example.beginnerfitbe.youtube.util;

import com.example.beginnerfitbe.youtube.dto.YoutubeSearchResDto;
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
        search.setMaxResults(10L);

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

    //비디오 메타데이터
    public String videoInfo(YouTube youtube, String videoId){
        try {
            YouTube.Videos.List videosListByIdRequest = youtube.videos().list(Collections.singletonList("contentDetails"));
            videosListByIdRequest.setKey(apiKey);

            videosListByIdRequest.setId(Collections.singletonList(videoId));
            VideoListResponse videoListResponse = videosListByIdRequest.execute();

            List<Video> videoList = videoListResponse.getItems();
            String duration=null;
            if (videoList != null && !videoList.isEmpty()) {
                Video video = videoList.get(0);
                duration = convertISO8601Duration(video.getContentDetails().getDuration());
            }
            return duration;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //ISO 8601 format
    public  String convertISO8601Duration(String isoDuration) {
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

        if(h>0){
            return String.format("%02d:%02d:%02d", h, m, s);
        }
        else if (m>0){
            return String.format("%02d:%02d", m, s);
        }
        else{
            return String.format("%02d",s);
        }
    }
}