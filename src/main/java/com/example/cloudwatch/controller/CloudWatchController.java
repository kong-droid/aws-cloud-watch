package com.example.cloudwatch.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/aws/cloud-watch")
@RequiredArgsConstructor
@Tag(name = "CloudWatchTest", description = "클라우드 와치 테스트")
public class CloudWatchController {

    @GetMapping
    @Operation(summary = "호출하면 로그에 출력됩니다.")
    public void write() {
        log.info("start write log.");
        log.info("Hello World.");
        log.info("end write log.");
    };
}

