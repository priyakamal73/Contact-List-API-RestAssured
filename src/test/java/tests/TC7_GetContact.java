package tests;

import base.BaseClass;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Properties;
import static io.restassured.RestAssured.given;
import utils.reportInfo;

public class TC7_GetContact extends BaseClass {

    Response response;
    Properties prop = getProp();

    @BeforeMethod
    public void startTest() {
        test = extent.createTest("Get Contact Details");
    }

    @Test(description = "Get contact details")
    public void getContact() {
        try{
            String token = prop.getProperty("token");
            String contact_id = prop.getProperty("contact_id");

            response = given()
                    .header("Content-Type", "application/json")
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .get("contacts/" + contact_id);

            String responseBody = response.getBody().asPrettyString();
            System.out.println(responseBody);

            Assert.assertEquals(response.getStatusCode(),200,"The status code is not 200");
            Assert.assertTrue(response.getStatusLine().contains("OK"));

            reportInfo.logPass(test, "Contact details retrieved successfully", responseBody);
        }catch (AssertionError e) {
            String errorMessage = (response != null && response.jsonPath().getString("message") != null)
                    ? response.jsonPath().getString("message")
                    : "No response message received";

            reportInfo.logFail(test, "Test failed: " + errorMessage, response != null ? response.getBody().asPrettyString() : "No response body received");
            throw e;
        }
    }
}