package rest;

import com.aplana.apiPractice.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;


public class RestSmokeTest {

    private static final Logger LOG = Log.getLogger(RestSmokeTest.class);

    @Test
    public void addProfile() {
        Response response = RestAssured.given()
                .baseUri("http://localhost:" + ConfigReader.getInstance().getRestPort())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Maxim\",\"surName\":\"Sinitcin\",\"secondName\":\"Alexandrovich\",\"phone\":\"+792096219035\"," +
                        "\"email\":\"sample@gmail.com\",\"position\":\"Senior QA engineer\"," +
                        "\"projectList\":[{\"name\":\"Sberbank technologies\"},{\"name\":\"VTB24\"}]}")
                .log().all(true)
                .post("/rest/profiles");

        response.then().log().all(true)
                .assertThat().statusCode(200)
                .content(StringContains.containsString("Sberbank technologies"));
    }

    @Test
    public void getProfiles() {
        Response response = RestAssured.given()
                .baseUri("http://localhost:" + ConfigReader.getInstance().getRestPort())
                .formParam("pretty", "true")
                .log().everything(true)//.all()
                .get("/rest/profiles");
        response.then().log().all(true)
                .assertThat().statusCode(200);
    }
}
