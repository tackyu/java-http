package org.apache.coyote.http11;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class HttpResponse {

    public static final String SET_COOKIE_PREFIX = "Set-Cookie: ";
    public static final String SESSION_ID_NAME = "JSESSIONID";
    public static final String KEY_VALUE_SEPARATOR = "=";

    private String response = "";
    private String body = "";

    public HttpResponse() {
    }

    public String generateResponseBody(String path) throws IOException {
        if (!path.contains(".")) {
            final URL resource = getClass().getClassLoader().getResource(path + ".html");
            body = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
            return body;
        }
        final URL resource = getClass().getClassLoader().getResource(path);
        body = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
        return body;
    }

    public void generate200Response(String path, String responseBody) {
        response = String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: " + responseBody.getBytes().length + " ",
                "",
                responseBody);
        if (path.startsWith("/css")) {
            response = response.replace("text/html", "text/css");
        }
    }

    public void generate302Response(String location) {
        response = String.join("\r\n",
                "HTTP/1.1 302 FOUND ",
                "Location: " + location+" ",
                "Content-Type: text/html;charset=utf-8 "
        );
    }

    public void appendSetCookieHeader(String sessionId) {
        if (!body.isEmpty()) {
            return;
        }
        response = String.join("\r\n",
                response + " ",
                SET_COOKIE_PREFIX + SESSION_ID_NAME + KEY_VALUE_SEPARATOR + sessionId + " "
        );
    }

    public String getResponse() {
        return response;
    }
}
