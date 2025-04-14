package de.medical.app.controller;

import de.medical.app.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
public class FileUploadOSController {

    private final FileService fileService;

    public FileUploadOSController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/uploadOS")
    public String showUploadForm() {
        return "uploadOSForm";
    }

    @PostMapping("/uploadOS")
    public String handleFileUploadInOS(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            log.info("File is empty");
            model.addAttribute("message", "Вы не выбрали файл для загрузки");
            return "uploadOSForm";
        }
        if (!file.getContentType().equalsIgnoreCase("image/jpeg")) {
            model.addAttribute("message", "Разрешена загрузка только JPEG-файлов");
            return "uploadOSForm";
        } else {
            String fileName = fileService.saveFileOS(file);
            if (fileName == null) {
                log.error("File not saved");
                model.addAttribute("message", "Ошибка при загрузке файла");
                return "uploadOSForm";
            } else {
                log.info("File " + fileName + " saved successfully");
                model.addAttribute("message", "Файл " + fileName + " успешно загружен");
                return "uploadOSForm";
            }


        }
    }

}
