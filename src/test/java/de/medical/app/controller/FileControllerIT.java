package de.medical.app.controller;


import de.medical.app.repository.FileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileRepository fileRepository;

    @Test
    @DisplayName("Проверка успешной загрузки и последующей выдачи файла")
    void testFileUploadAndDownload() throws Exception {
        String testContent = "File content in Db";
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", testContent.getBytes());
        MvcResult uploadResult = mockMvc.perform(multipart("/file/upload")
                        .file(mockFile).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status()
                        .isOk()).andExpect(content().string(containsString("Файл успешно загружен. ID = 1")))
                .andReturn();
        Assertions.assertNotNull(uploadResult);
        Assertions.assertEquals("multipart/form-data", uploadResult.getRequest().getContentType());
        String fileId = "1";
        Optional<de.medical.app.model.FileEntity> file = fileRepository.findById(Long.valueOf(fileId));
        Assertions.assertTrue(file.isPresent());
        Assertions.assertEquals(testContent, new String(file.get().getData()));
        mockMvc.perform(get("/file/" + fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/octet-stream"))
                .andExpect(content().string(containsString(testContent)));
    }

    @Test
    @DisplayName("Проверка валидации с пустым файлом")
    void testHandleUploadEmptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);
        mockMvc.perform(multipart("/file/upload").file(emptyFile))
                .andExpect(status().isOk()).andExpect(view().name("uploadForm"))
                .andExpect(model().attribute("message", "Вы не выбрали файл для загрузки"));
    }

    @Test
    @DisplayName("Файл не найден")
    void testFileNotFound() throws Exception {
        mockMvc.perform(get("/file/99"))
                .andExpect(status().isNotFound());
    }

}
