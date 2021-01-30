package petstore_testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TC_PetStoreAPI {

	@Test(priority=1)
	public void test_getAvailablePets() {
	Response resp =
		given()

			.when()
				.get("https://petstore.swagger.io/v2/store/inventory")

			.then()
				.statusCode(200)
				.log().body()
				.extract().response();
	
	int code = resp.getStatusCode();
	Assert.assertEquals(code==200, true, "Status code is not as expected");	
	System.out.println("Status code is as expected");
						
	}
	
	@Test(priority=2)
	public String test_getPetsAvail() {
		Response resp=
				given()
					.when()
					 	.get("https://petstore.swagger.io/v2/pet/findByStatus?status=available")
					.then()
						.statusCode(200)
						.log().body()
						.extract().response();
		int code = resp.getStatusCode();
		Assert.assertEquals(code==200, true, "Status code is not as expected");	
		System.out.println("Status code is as expected");
		String petAvail = resp.asString();
		Assert.assertEquals(petAvail.contains("available"), true, "All pets displayed does not have status as available");
		System.out.println("All pets displayed have status as available");
		return petAvail;
	}
	
	@Test(priority=3)
	public void test_postNewPet() {
		
		// Creating instance for json file
		File jsonFileData = new File(".\\test_data\\jsondata.json");
		Response resp =
				given()
					.contentType("application/json")
					.body(jsonFileData)
				.when()
				 	.post("https://petstore.swagger.io/v2/pet")
				 .then()
				 	.log().body()
				 	.assertThat()
				 	.statusCode(200)
				 	.extract().response();
		String strJson = resp.asString();
		System.out.println(strJson);
		String petA = test_getPetsAvail();
		System.out.println(petA);
		Assert.assertTrue(petA.contains("Rose"), "Pet is not available in store");
		System.out.println("Pet is now available in store");
		
	}
	
	@Test(priority=4)
	public void test_putStatusUpdate() {
		
		// Creating instance for json file
		File jsonFileData = new File(".\\test_data\\status_update.json");
		Response resp =
				given()
					.contentType("application/json")
					.body(jsonFileData)
				.when()
				 	.put("https://petstore.swagger.io/v2/pet")
				 .then()
				 	.log().body()
				 	.assertThat()
				 	.statusCode(200)
				 	.extract().response();
		String strJson = resp.asString();
		System.out.println(strJson);
		Assert.assertTrue(strJson.contains("sold"), "Pet status is not updated as sold");
		System.out.println("Pet status is updated as sold");
		
	}
	
	@Test(priority=5)
	public void test_deletePet() {
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("petId", "123");
		
		Response resp =
				given()
					.contentType("application/json")
					.body(data.toString())
				.when()
				 	.delete("https://petstore.swagger.io/v2/pet/123")
				 .then()
				 	.log().body()
				 	.assertThat()
				 	.statusCode(200)
				 	.extract().response();
		String strJson = resp.asString();
		System.out.println(strJson);
		int code = resp.getStatusCode();
		Assert.assertEquals(code==200, true, "Pet is not deleted");	
		System.out.println("Pet is now deleted");
		
		
	}
}
