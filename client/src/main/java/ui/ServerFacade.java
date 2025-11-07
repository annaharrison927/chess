package ui;

import com.google.gson.Gson;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import model.UserData;
import result.RegisterResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void register(UserData userData) throws Exception {
        var request = buildRequest("POST", "/user", userData);
        var response = sendRequest(request);
        handleResponse(response, RegisterResult.class);
    }


    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new Exception("Error: " + ex.getMessage() + "\n"); // EDIT THIS LATER
        }
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private <T> void handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (status != 200) {
            var body = response.body();
            if (body != null) {
                if (status == 403) {
                    throw new AlreadyTakenException("Error: Already taken" + "\n");
                } else if (status == 401) {
                    throw new DataAccessException("Error: Cannot access data" + "\n");
                } else if (status == 400) {
                    throw new BadRequestException("Error: Bad request" + "\n");
                } else if (status == 500) {
                    throw new DataAccessException("Error: Failed to connect" + "\n");
                }
            }
            throw new Exception("Error: Other issue" + "\n");
        }

        if (responseClass != null) {
            new Gson().fromJson(response.body(), responseClass);
        }

    }
}
