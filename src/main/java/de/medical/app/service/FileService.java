package de.medical.app.service;

import de.medical.app.model.FileEntity;
import de.medical.app.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileEntity saveFile(MultipartFile file){
        String fileName = file.getOriginalFilename();

        String fileType = file.getContentType();
        try {
            byte[] data = file.getBytes();
            FileEntity fileEntity = new FileEntity(fileName,fileType,data);
            log.info("Saving file: {}", fileEntity);
            return fileRepository.save(fileEntity);
        }
        catch (IOException exception){
            log.error("Error while saving file: {}", exception.getMessage());
            return null;
        }
    }

    public Optional<FileEntity> getFile(Long id){
        log.info("Finding file with id: {}", id);
        return fileRepository.findById(id);
    }

}
