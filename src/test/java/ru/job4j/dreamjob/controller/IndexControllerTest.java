package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class IndexControllerTest {

    @Test
    public void whenGetIndex() {
        var actual = new IndexController().getIndex();
        assertThat(actual).isEqualTo("index");
    }
}
