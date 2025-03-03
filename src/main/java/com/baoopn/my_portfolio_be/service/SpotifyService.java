package com.baoopn.my_portfolio_be.service;

import com.baoopn.my_portfolio_be.exception.SpotifyServiceException;
import com.baoopn.my_portfolio_be.model.SpotifyCurrentlyPlayingTrackDTO;
import com.baoopn.my_portfolio_be.model.SpotifyRecentlyPlayedTrackDTO;
import com.baoopn.my_portfolio_be.model.SpotifyTokenRequestDTO;
import com.baoopn.my_portfolio_be.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpotifyService {

    @Value("${SPOTIFY_CLIENT_ID}")
    private String clientId;

    @Value("${SPOTIFY_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${SPOTIFY_REFRESH_TOKEN}")
    private String refreshToken;

    // Using constants instead of @Value annotations
    private static final String tokenEndpoint = Constants.SPOTIFY_TOKEN_ENDPOINT;
    private static final String nowPlayingEndpoint = Constants.SPOTIFY_NOW_PLAYING_ENDPOINT;
    private static final String recentlyPlayedEndpoint = Constants.SPOTIFY_RECENTLY_PLAYED_ENDPOINT;

    private String accessToken;
    private long lastFetched;
    private static final long TOKEN_CACHE_DURATION = Constants.TOKEN_CACHE_DURATION_MS;

    private SpotifyCurrentlyPlayingTrackDTO cachedCurrentlyPlayingTrack;
    private long lastCurrentlyPlayingFetched;
    private static final long CURRENTLY_PLAYING_CACHE_DURATION = Constants.CURRENTLY_PLAYING_CACHE_DURATION_MS;

    private Map<Integer, List<SpotifyRecentlyPlayedTrackDTO>> cachedRecentlyPlayedTracks = new HashMap<>();
    private Map<Integer, Long> lastRecentlyPlayedFetched = new HashMap<>();
    private static final long RECENTLY_PLAYED_CACHE_DURATION = Constants.RECENTLY_PLAYED_CACHE_DURATION_MS;

    public String getAccessToken() {
        long now = System.currentTimeMillis();

        if (accessToken != null && lastFetched + TOKEN_CACHE_DURATION > now) {
            return accessToken;
        }

        SpotifyTokenRequestDTO tokenRequest = new SpotifyTokenRequestDTO();
        tokenRequest.setClientId(clientId);
        tokenRequest.setClientSecret(clientSecret);
        tokenRequest.setRefreshToken(refreshToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                accessToken = jsonNode.get("access_token").asText();
                lastFetched = now;
                return accessToken;
            } catch (Exception e) {
                throw new SpotifyServiceException("Failed to parse access token response", e);
            }
        } else {
            throw new SpotifyServiceException("Failed to fetch access token");
        }
    }

    public SpotifyCurrentlyPlayingTrackDTO getCurrentlyPlayingTrack() {
        long now = System.currentTimeMillis();

        if (cachedCurrentlyPlayingTrack != null
                && lastCurrentlyPlayingFetched + CURRENTLY_PLAYING_CACHE_DURATION > now) {
            return cachedCurrentlyPlayingTrack;
        }

        String accessToken;
        try {
            accessToken = getAccessToken();
        } catch (Exception e) {
            return new SpotifyCurrentlyPlayingTrackDTO();
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                    nowPlayingEndpoint,
                    HttpMethod.GET,
                    request,
                    String.class);
        } catch (Exception e) {
            return new SpotifyCurrentlyPlayingTrackDTO();
        }

        if (response.getStatusCode() != HttpStatus.OK || !response.hasBody()) {
            return new SpotifyCurrentlyPlayingTrackDTO();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            SpotifyCurrentlyPlayingTrackDTO track = new SpotifyCurrentlyPlayingTrackDTO();
            track.setAlbumImageUrl(jsonNode.get("item").get("album").get("images").get(0).get("url").asText());
            track.setArtist(
                    jsonNode.get("item").get("artists")
                            .findValues("name")
                            .stream()
                            .map(JsonNode::asText)
                            .collect(Collectors.joining(", ")));
            track.setPlaying(jsonNode.get("is_playing").asBoolean());
            track.setSongUrl(jsonNode.get("item").get("external_urls").get("spotify").asText());
            track.setTitle(jsonNode.get("item").get("name").asText());
            track.setId(jsonNode.get("item").get("id").asText());
            track.setProgressMs(jsonNode.get("progress_ms").asInt());
            track.setDurationMs(jsonNode.get("item").get("duration_ms").asInt());

            cachedCurrentlyPlayingTrack = track;
            lastCurrentlyPlayingFetched = now;

            return track;
        } catch (Exception e) {
            throw new SpotifyServiceException("Failed to parse currently playing track response", e);
        }
    }

    public List<SpotifyRecentlyPlayedTrackDTO> getRecentlyPlayedTracks(int limit) {
        long now = System.currentTimeMillis();

        if (cachedRecentlyPlayedTracks.containsKey(limit)
                && lastRecentlyPlayedFetched.get(limit) + RECENTLY_PLAYED_CACHE_DURATION > now) {
            return cachedRecentlyPlayedTracks.get(limit);
        }

        if (limit < 1 || limit > 50) {
            throw new IllegalArgumentException("Limit must be between 1 and 50");
        }

        String accessToken;
        try {
            accessToken = getAccessToken();
        } catch (Exception e) {
            throw new SpotifyServiceException("Failed to get access token", e);
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        String url = recentlyPlayedEndpoint + "?limit=" + limit;

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class);
        } catch (Exception e) {
            throw new SpotifyServiceException("Failed to fetch recently played tracks", e);
        }

        if (response.getStatusCode() != HttpStatus.OK || !response.hasBody()) {
            throw new SpotifyServiceException("No recently played tracks found");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            List<SpotifyRecentlyPlayedTrackDTO> tracks = new ArrayList<>();

            for (JsonNode item : jsonNode.get("items")) {
                SpotifyRecentlyPlayedTrackDTO track = new SpotifyRecentlyPlayedTrackDTO();
                track.setAlbumImageUrl(item.get("track").get("album").get("images").get(0).get("url").asText());
                track.setArtist(
                        item.get("track").get("artists")
                                .findValues("name")
                                .stream()
                                .map(JsonNode::asText)
                                .collect(Collectors.joining(", ")));
                track.setPlayedAt(ZonedDateTime.parse(item.get("played_at").asText()));
                track.setSongUrl(item.get("track").get("external_urls").get("spotify").asText());
                track.setTitle(item.get("track").get("name").asText());
                tracks.add(track);
            }

            cachedRecentlyPlayedTracks.put(limit, tracks);
            lastRecentlyPlayedFetched.put(limit, now);

            return tracks;
        } catch (Exception e) {
            throw new SpotifyServiceException("Failed to parse recently played tracks response", e);
        }
    }
}
