package com.example.beginnerfitbe.youtube.service;

import com.example.beginnerfitbe.youtube.dto.YoutubeSearchResDto;
import com.example.beginnerfitbe.youtube.util.YoutubeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class YoutubeVideoService {

    private final YoutubeUtil youtubeUtil;

    public List<YoutubeSearchResDto> search(String query) throws IOException {
        return youtubeUtil.search(query);
    }






}
