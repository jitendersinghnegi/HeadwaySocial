package headway.backend.service.impl;

import headway.backend.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {
    private final Path rootLocation;

    public FileStorageServiceImpl(
            @Value("${expense.bills.upload-dir:uploads/bills}") String uploadDir
    ) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    /**
     * @param file
     * @return
     */
    @Override
    public String storeBillFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String randomName = UUID.randomUUID() + "-" + originalFilename;

        try {
            Path target = this.rootLocation.resolve(randomName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Stored bill file at: {}", target);
            return target.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store bill file", e);
        }
    }
}
