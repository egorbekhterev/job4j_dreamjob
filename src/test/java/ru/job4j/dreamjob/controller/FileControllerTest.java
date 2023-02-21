package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.service.FileService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileControllerTest {

    private FileService fileService;

    private FileController fileController;

    @BeforeEach
    public void initServices() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
    }

    @Test
    public void whenRequestAndFileIsFound() {
        var body = new byte[] {1, 2, 3};
        var file = Optional.of(new FileDto("file", body));
        when(fileService.getFileById(any(Integer.class))).thenReturn(file);

        var view = fileController.getById(any(Integer.class));
        assertThat(view.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(view.getBody()).isEqualTo(body);
    }

    @Test
    public void whenRequestAndFileIsNotFound() {
        when(fileService.getFileById(any(Integer.class))).thenReturn(Optional.empty());

        var view = fileController.getById(any(Integer.class));
        assertThat(view.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(view.getBody()).isNull();
    }
}
