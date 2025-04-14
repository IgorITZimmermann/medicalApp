package de.medical.app.controller;

import de.medical.app.model.FileEntity;
import de.medical.app.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
@Slf4j
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            log.info("File is empty");
            model.addAttribute("message", "Вы не выбрали файл для загрузки");
            return "uploadForm";
        }
        FileEntity savedFile = fileService.saveFile(file);
        if (savedFile == null) {
            log.info("File not saved");
            model.addAttribute("message", "Ошибка при загрузке файла !");
        } else {
            log.info("File saved successfully");
            model.addAttribute("message","Файл успешно загружен. ID = " + savedFile.getId());
        }
        return "uploadForm";
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        return fileService.getFile(id)
                .map(fileEntity -> {
                    return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"" + fileEntity.getName() + "\"").body(fileEntity.getData());
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();
                });
    }

}
