package com.vitametrics.vitametrics.common.fixtures.acceptance;

import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordRegisterRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class SleepRecordAcceptanceFixtures {

    private static final String BASE_URL = "/api/sleep";

    public static ExtractableResponse<Response> SLEEP_RECORD_REGISTER_REQUEST(SleepRecordRegisterRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post(BASE_URL)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> SLEEP_RECORD_SEARCH_REQUEST(Long sleepRecordId) {
        return RestAssured.given().log().all()
                .pathParam("sleepRecordId", sleepRecordId)
                .when().log().all()
                .get(BASE_URL + "/{sleepRecordId}")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> SLEEP_RECORD_UPDATE_REQUEST(SleepRecordUpdateRequest request, Long sleepRecordId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .patch(BASE_URL + "/{sleepRecordId}", sleepRecordId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> SLEEP_RECORD_DELETE_REQUEST(Long sleepRecordId) {
        return RestAssured.given().log().all()
                .delete(BASE_URL + "/{sleepRecordId}", sleepRecordId)
                .then().log().all()
                .extract();
    }
}
