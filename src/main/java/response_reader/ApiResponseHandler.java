package response_reader;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiResponseHandler {
    private final String apiUrl;
    private Response response;

    public ApiResponseHandler(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void makeRequest() {
        this.response = RestAssured.given()
                .relaxedHTTPSValidation()
                .get(apiUrl);
    }

    public Response getResponse() {
        return response;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}