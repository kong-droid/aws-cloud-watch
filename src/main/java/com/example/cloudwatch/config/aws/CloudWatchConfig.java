package com.example.cloudwatch.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import com.amazonaws.services.logs.model.CreateLogStreamRequest;
import com.amazonaws.services.logs.model.DescribeLogStreamsRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
@Slf4j
@Configuration
@EnableScheduling
public class CloudWatchConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.cloudfront.log.group}")
    private String logGroup;

    @Value("${spring.application.name}")
    private String applicationName;

    private AWSLogs awsLogs;

    @PostConstruct
    public void init() {
        val awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        awsLogs = AWSLogsClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        create();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void create() {
        val logStreamName = applicationName + "_" +
                LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // check exist logStreams.
        val describeLogStreamsRequest = new DescribeLogStreamsRequest()
                .withLogGroupName(logGroup)
                .withLogStreamNamePrefix(logStreamName);
        val describeLogStreamsResult = awsLogs.describeLogStreams(describeLogStreamsRequest);
        val logStreams = describeLogStreamsResult.getLogStreams();
        if(logStreams.isEmpty()) {
            val createLogStreamRequest = new CreateLogStreamRequest()
                    .withLogGroupName(logGroup)
                    .withLogStreamName(logStreamName);
            awsLogs.createLogStream(createLogStreamRequest);
        } else {
            log.info("log stream '{} already exists.'", logStreamName);
        }
    }
}
