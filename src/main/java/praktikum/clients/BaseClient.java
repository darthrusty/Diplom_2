package praktikum.clients;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseClient {

    private static final String MAIN_URL = "https://stellarburgers.nomoreparties.site/";

    protected RequestSpecification getSpec() {
        return new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setContentType(ContentType.JSON)
                .setBaseUri(MAIN_URL)
                .build();
    }

}
