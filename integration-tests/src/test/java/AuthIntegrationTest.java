import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

import io.restassured.response.Response;

public class AuthIntegrationTest {

	@BeforeAll
	static void setUp() {
		RestAssured.baseURI = "http://localhost:4004";
	}

	@Test
	public void shouldReturnOKWithValidToken() {
		// Arrange - Gather all info for method
		// Act - Process the request
		// Assert - should valid response

		String loginPayload = """
				{
						"email": "testuser@test.com",
						"password": "password123"
				}

					""";

		Response response = given().contentType("application/json").body(loginPayload).when().post("/auth/login").then()
				.statusCode(200).body("token", notNullValue()).extract().response();

		System.out.println("Generated token: " + response.jsonPath().getString("token"));

	}

	@Test
	public void shouldReturnUnauthorizedOnInvalidLogin() {
		// Arrange - Gather all info for method
		// Act - Process the request
		// Assert - should valid response

		String loginPayload = """
				{
						"email": "wrong_email@test.com",
						"password": "wrong_password"
				}

					""";
		
		 given()
				.contentType("application/json")
				.body(loginPayload)
				.when()
				.post("/auth/login")
				.then()
				.statusCode(401);
				
		
	}

}
