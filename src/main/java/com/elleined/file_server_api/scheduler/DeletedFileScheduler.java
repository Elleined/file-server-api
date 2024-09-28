package com.elleined.file_server_api.scheduler;

import com.elleined.file_server_api.service.file.deleted.db.DBDeletedFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@Transactional
@RequiredArgsConstructor
public class DeletedFileScheduler {
    private final DBDeletedFileService DBDeletedFileService;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void deleteAllJob() throws IOException {
        DBDeletedFileService.permanentlyDeleteDeletedImages();
    }
}
