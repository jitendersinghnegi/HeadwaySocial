package headway.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public String storeBillFile(MultipartFile file);
}
