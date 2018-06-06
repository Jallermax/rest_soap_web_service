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
    private static final String LOCALHOST = "http://localhost";
    private static final String VIRMACH = "http://";


    private String host = LOCALHOST + ":" + ConfigReader.getInstance().getRestPort();

    @Test
    public void addProfile() {
        Response response = RestAssured.given()
                .baseUri(host)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Test\",\"surName\":\"Autotestov\",\"secondName\":\"Testovich\",\"birthday\": \"Feb 2, 1905 12:00:00 AM\",\"phone\":\"+79200001035\"," +
                        "\"email\":\"sample@gmail.com\",\"position\":\"Senior QA engineer\",\"projectList\":[{\"name\":\"Sberbank technologies\"}," +
                        "{\"name\":\"VTB24\",\"description\": \"Описание 2.\",\"startDate\": \"Mar 01, 2018 12:00:00 AM\",\"endDate\": \"Mar 15, 2018 12:00:00 AM\"}]}")
                .log().all(true)
                .post("/rest/profiles");

        response.then().log().all(true)
                .assertThat().statusCode(200)
                .content(StringContains.containsString("Sberbank technologies"));
    }

    @Test
    public void getProfiles() {
        Response response = RestAssured.given()
                .baseUri(host)
                .formParam("pretty", "true")
                .log().all(true)
                .get("/rest/profiles");
        response.then().log().all(true)
                .assertThat().statusCode(200);
    }

    @Test
    public void getAllProfiles() {
        Response response = RestAssured.given()
                .baseUri(host)
                .formParam("message", "getProfileList")
                .log().all(true)
                .post("/gui");
        response.then().log().all(true)
                .assertThat().statusCode(200);
    }

    @Test
    public void getTaskKey() {
        Response response = RestAssured.given()
                .baseUri(host)
                .log().all(true)
                .post("/rest/task_key");
        response.then().log().all(true)
                .assertThat().statusCode(200);
    }
}
