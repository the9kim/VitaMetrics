package com.vitametrics.vitametrics.common.fixtures.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class SleepPredictionAcceptanceFixture {

    private static final String BASE_URL = "/api/sleep";

    public static ExtractableResponse<Response> GET_SLEEP_PREDICTION_REQUEST(String predictionDate) {
        return RestAssured.given().log().all()
                .queryParam("date", predictionDate)
                .when().log().all()
                .get(BASE_URL + "/predict-sleep")
                .then().log().all()
                .extract();


    }
}
