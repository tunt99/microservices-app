package com.microservices.app.controller;

import com.microservices.app.model.AnalyticsResponseModel;
import com.microservices.app.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@PreAuthorize("isAuthenticated()")
@RestController
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/get-word-count-by-word/{word}")
    @Operation(summary = "Get analytics by word.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnalyticsResponseModel.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    public @ResponseBody
    ResponseEntity<AnalyticsResponseModel> getWordCountByWord(@PathVariable("word") @NotEmpty String word) {
        Optional<AnalyticsResponseModel> response = analyticsService.getWordAnalytics(word.toLowerCase(Locale.ROOT));
        if (response.isPresent()) {
            log.info("Analytics data returned with id {}", response.get().getId());
            return ResponseEntity.ok(response.get());
        }
        return ResponseEntity.ok(AnalyticsResponseModel.builder().build());
    }
}
