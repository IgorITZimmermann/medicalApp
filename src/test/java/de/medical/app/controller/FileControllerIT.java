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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status()
                        .isOk()).andExpect(content().string(containsString("Файл успешно загружен. ID =")))
                .andReturn();
        Assertions.assertNotNull(uploadResult);
    }

}
