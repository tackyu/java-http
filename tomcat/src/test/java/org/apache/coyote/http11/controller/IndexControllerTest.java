package org.apache.coyote.http11.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IndexControllerTest {

    @Test
    @DisplayName("GET 요청 동작 확인")
    void doGet() throws IOException {
        IndexController indexController = new IndexController();
        String httpRequest = String.join("\r\n",
                "GET /index HTTP/1.1",
                "Host: localhost:8080"
        );
        BufferedReader bufferedReader = new BufferedReader(new StringReader(httpRequest));
        HttpRequest request = new HttpRequest(bufferedReader);
        HttpResponse httpResponse = new HttpResponse();

        indexController.doGet(request, httpResponse);
        String responseBody = new HttpResponse().generateResponseBody("static/index.html");
        final URL resource = getClass().getClassLoader().getResource("static/index.html");
        String actual = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: "+responseBody.getBytes().length+ " \r\n" +
                "\r\n" +
                new String(Files.readAllBytes(new File(resource.getFile()).toPath()));

        String response = httpResponse.getResponse();
        assertThat(actual).isEqualTo(response);
    }
}
