import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiTest {

    @Test
    public void testGetRequest() {
        HttpResponse<JsonNode> response = Unirest.get("https://jsonplaceholder.typicode.com/posts/1")
                .asJson();

        assertEquals(200, response.getStatus());
        JsonNode body = response.getBody();
        System.out.println(body.toString());

        assertEquals(1, body.getObject().getInt("id"));
    }

    @Test
    public void testGetAllPosts() {
        HttpResponse<JsonNode> response = Unirest.get("https://jsonplaceholder.typicode.com/posts")
                .asJson();

        assertEquals(200, response.getStatus());
        assertTrue(response.getBody().getArray().length() > 0);
    }

    @Test
    public void testCreatePost() {
        HttpResponse<JsonNode> response = Unirest.post("https://jsonplaceholder.typicode.com/posts")
                .header("Content-Type", "application/json")
                .body("{\"title\":\"foo\",\"body\":\"bar\",\"userId\":1}")
                .asJson();

        assertEquals(201, response.getStatus());
        assertEquals("foo", response.getBody().getObject().getString("title"));
    }

    @Test
    public void testUpdatePost() {
        HttpResponse<JsonNode> response = Unirest.put("https://jsonplaceholder.typicode.com/posts/1")
                .header("Content-Type", "application/json")
                .body("{\"id\":1, \"title\":\"updated title\", \"body\":\"updated body\", \"userId\":1}")
                .asJson();

        assertEquals(200, response.getStatus());
        assertEquals("updated title", response.getBody().getObject().getString("title"));
    }

    @Test
    public void testDeletePost() {
        HttpResponse<String> response = Unirest.delete("https://jsonplaceholder.typicode.com/posts/1")
                .asString();

        assertEquals(200, response.getStatus()); // Bazı sistemler 204 dönebilir
    }

    @Test
    public void testGetNonExistingPost_ShouldFail() {
        HttpResponse<JsonNode> response = Unirest.get("https://jsonplaceholder.typicode.com/posts/99999")
                .asJson();

        assertEquals(200, response.getStatus());

        JsonNode body = response.getBody();
        System.out.println("Response: " + body.toString());

        assertEquals(1, body.getObject().getInt("id")); // bu satır testin patlamasına sebep olur
    }

    @Test
    public void testGetPostWithQueryParam() {
        HttpResponse<JsonNode> response = Unirest.get("https://jsonplaceholder.typicode.com/posts")
                .queryString("title", "qui est esse")
                .asJson();

        assertEquals(200, response.getStatus());
        assertTrue(response.getBody().getArray().length() >= 0); // eşleşmeyebilir ama istek başarılı olmalı
    }

    @Test
    public void testGetWithCustomHeader() {
        HttpResponse<JsonNode> response = Unirest.get("https://jsonplaceholder.typicode.com/posts/1")
                .header("Accept", "application/json")
                .asJson();

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testCreatePostAndCheckBody() {
        HttpResponse<JsonNode> response = Unirest.post("https://jsonplaceholder.typicode.com/posts")
                .header("Content-Type", "application/json")
                .body("{\"title\":\"test title\",\"body\":\"test body\",\"userId\":5}")
                .asJson();

        assertEquals(201, response.getStatus());
        assertEquals("test body", response.getBody().getObject().getString("body"));
    }

    @Test
    public void testGetPostsByUserId() {
        HttpResponse<JsonNode> response = Unirest.get("https://jsonplaceholder.typicode.com/posts")
                .queryString("userId", 1)
                .asJson();

        assertEquals(200, response.getStatus());
        assertTrue(response.getBody().getArray().length() > 0);
    }

    @Test
    public void testCreatePostWithInvalidBody() {
        HttpResponse<JsonNode> response = Unirest.post("https://jsonplaceholder.typicode.com/posts")
                .header("Content-Type", "application/json")
                .body("{}")
                .asJson();

        assertEquals(201, response.getStatus()); // jsonplaceholder yine de bir şey dönebilir
    }

    @Test
    public void testInvalidUrl() {
        HttpResponse<JsonNode> response = Unirest.get("https://jsonplaceholder.typicode.com/wrongpath")
                .asJson();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void testGetUserDetails() {
        HttpResponse<JsonNode> response = Unirest.get("https://jsonplaceholder.typicode.com/users/1")
                .asJson();

        assertEquals(200, response.getStatus());
        assertEquals("Leanne Graham", response.getBody().getObject().getString("name"));
    }

    @Test
    public void testGetComments() {
        HttpResponse<JsonNode> response = Unirest.get("https://jsonplaceholder.typicode.com/comments")
                .asJson();

        assertEquals(200, response.getStatus());
        assertTrue(response.getBody().getArray().length() > 0);
    }

    @Test
    public void testDeleteAndTryToGetPost() {
        // Silme
        HttpResponse<String> deleteResponse = Unirest.delete("https://jsonplaceholder.typicode.com/posts/2")
                .asString();
        assertEquals(200, deleteResponse.getStatus());

        // Tekrar GET ile kontrol (JSONPlaceholder sahte API olduğu için hala dönebilir)
        HttpResponse<JsonNode> getResponse = Unirest.get("https://jsonplaceholder.typicode.com/posts/2")
                .asJson();

        assertEquals(200, getResponse.getStatus());
    }


}
