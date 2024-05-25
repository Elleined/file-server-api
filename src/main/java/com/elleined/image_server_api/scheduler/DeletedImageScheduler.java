package com.elleined.image_server_api.scheduler;

import com.elleined.image_server_api.service.image.deleted.DeletedImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class DeletedImageScheduler {
    private final DeletedImageService deletedImageService;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void deleteAllJob() {
        deletedImageService.deleteAll();
    }
}
