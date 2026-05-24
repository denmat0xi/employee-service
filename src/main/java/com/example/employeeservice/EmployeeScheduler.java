package com.example.employeeservice;

import com.example.employeeservice.dto.*;
import com.example.employeeservice.service.EmployeeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeScheduler {

    private final EmployeeService employeeService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(cron = "0 0 0 * * *")
    public void fetchAndCreateDailyEmployee() {
        log.info("Starting daily employee fetch from randomuser.me");

        try {
            String response = restTemplate.getForObject("https://randomuser.me/api/", String.class);
            JsonNode root = objectMapper.readTree(response);

            JsonNode user = root.path("results").get(0);

            NameDTO nameDto = NameDTO.builder()
                    .title(user.path("name").path("title").asText())
                    .first(user.path("name").path("first").asText())
                    .last(user.path("name").path("last").asText())
                    .middle("")
                    .build();

            LocationDTO locationDto = LocationDTO.builder()
                    .street(StreetDTO.builder()
                            .number(user.path("location").path("street").path("number").asInt())
                            .name(user.path("location").path("street").path("name").asText())
                            .build())
                    .city(user.path("location").path("city").asText())
                    .state(user.path("location").path("state").asText())
                    .country(user.path("location").path("country").asText())
                    .postcode(user.path("location").path("postcode").asText())
                    .coordinates(CoordinatesDTO.builder()
                            .latitude(user.path("location").path("coordinates").path("latitude").asText())
                            .longitude(user.path("location").path("coordinates").path("longitude").asText())
                            .build())
                    .timezone(TimezoneDTO.builder()
                            .offset(user.path("location").path("timezone").path("offset").asText())
                            .description(user.path("location").path("timezone").path("description").asText())
                            .build())
                    .build();

            DobDTO dobDto = DobDTO.builder()
                    .date(ZonedDateTime.parse(user.path("dob").path("date").asText()).toLocalDateTime())
                    .age(user.path("dob").path("age").asInt())
                    .build();

            RegisteredDTO registeredDto = RegisteredDTO.builder()
                    .date(ZonedDateTime.parse(user.path("registered").path("date").asText()).toLocalDateTime())
                    .age(user.path("registered").path("age").asInt())
                    .build();

            PictureDTO pictureDto = PictureDTO.builder()
                    .large(user.path("picture").path("large").asText())
                    .medium(user.path("picture").path("medium").asText())
                    .thumbnail(user.path("picture").path("thumbnail").asText())
                    .build();

            IdDTO idDto = IdDTO.builder()
                    .name(user.path("id").path("name").asText())
                    .value(user.path("id").path("value").asText())
                    .build();

            EmployeeRequestDTO dto = EmployeeRequestDTO.builder()
                    .name(nameDto)
                    .location(locationDto)
                    .email(user.path("email").asText())
                    .phone(user.path("phone").asText())
                    .cell(user.path("cell").asText())
                    .dob(dobDto)
                    .registered(registeredDto)
                    .gender(user.path("gender").asText())
                    .nat(user.path("nat").asText())
                    .picture(pictureDto)
                    .id(idDto)
                    .build();

            employeeService.createEmployee(dto);
            log.info("Daily employee created successfully: {}", dto.email());

        } catch (Exception e) {
            log.error("Failed to fetch or create daily employee: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}