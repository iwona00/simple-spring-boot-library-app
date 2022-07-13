package pl.javaspringboot.libraryapp.library;

import com.google.gson.internal.LinkedTreeMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.*;
import pl.javaspringboot.libraryapp.library.model.Author;
import pl.javaspringboot.libraryapp.library.model.Book;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.util.Map;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import pl.javaspringboot.libraryapp.library.utils.LibraryUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestLibraryTest {
 
    @LocalServerPort
    private int port;
 
    private TestRestTemplate testRestTemplate = new TestRestTemplate();
    private static HttpHeaders headers        = new HttpHeaders();

    @BeforeAll
    private static void addJwtToken() {
        LibraryUtils.addJwtTokenToHeaders(headers);
    }

    @Test
    @Order(1)
    public void testCreateNewBook() throws JSONException {
        Book book = new Book("The Da Vinci Code", new Author("Dan", "Brown"));
        HttpEntity<Book> entity         = new HttpEntity(book, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/api/addNewBook"), HttpMethod.POST, entity, String.class);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void testGetAddedBook() throws Exception {
        HttpEntity<String> emptyEntityWithJwtHeader       = new HttpEntity(null, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(createURLWithPort("/api/findAllBooksInLibrary"), HttpMethod.GET, emptyEntityWithJwtHeader, String.class);
        JSONAssert.assertEquals(LibraryUtils.createJsonSampleData(), LibraryUtils.getFirstJsonObjectFromResponseEntity(response), false);
    }

    @Test
    @Order(3)
    public void testUpdateBook() {

        HttpEntity<String> emptyEntityWithJwtHeader          = new HttpEntity(null, headers);
        ResponseEntity<String> findAllBooksInLibraryResponse = testRestTemplate.exchange(createURLWithPort("/api/findAllBooksInLibrary"), HttpMethod.GET, emptyEntityWithJwtHeader, String.class);
        JSONObject jsonObject                                = LibraryUtils.getFirstJsonObjectFromResponseEntity(findAllBooksInLibraryResponse);

        Map<String, Object> getJsonObject = LibraryUtils.getMapFromJson(jsonObject);

        double id = (double)getJsonObject.get("id");

        // update book
        HttpEntity<Book> bookToBeUpdated    = new HttpEntity(new Book("The Da Vinci Code", new Author("JavaLeader", "pl")), headers);
        ResponseEntity<String> updatedBookResponse  = testRestTemplate.exchange(createURLWithPort("/api/updateBook/" + (int)id), HttpMethod.PUT, bookToBeUpdated, String.class);

        // check update book status
        Assert.assertEquals("The book has been successfully saved", updatedBookResponse.getBody());
        ResponseEntity<String> afterUpdatedResponse = testRestTemplate.exchange(createURLWithPort("/api/getBookById/" + (int)id), HttpMethod.GET, emptyEntityWithJwtHeader, String.class);

        // verify updated data
        Assert.assertEquals("JavaLeader",((LinkedTreeMap) LibraryUtils.getMapFromJson(afterUpdatedResponse.getBody()).get("author")).get("name"));
        Assert.assertEquals("pl",((LinkedTreeMap) LibraryUtils.getMapFromJson(afterUpdatedResponse.getBody()).get("author")).get("surname"));

    }

    @Test
    @Order(4)
    public void testDeleteBook() {

        HttpEntity<String> emptyEntityWithJwtHeader          = new HttpEntity(null, headers);
        ResponseEntity<String> findAllBooksInLibraryResponse = testRestTemplate.exchange(createURLWithPort("/api/findAllBooksInLibrary"), HttpMethod.GET, emptyEntityWithJwtHeader, String.class);
        JSONObject jsonObject                                = LibraryUtils.getFirstJsonObjectFromResponseEntity(findAllBooksInLibraryResponse);

        Map<String, Object> getJsonObject = LibraryUtils.getMapFromJson(jsonObject);

        double id = (double)getJsonObject.get("id");

        // delete book
        ResponseEntity<String> afterDeleteResponse = testRestTemplate.exchange(createURLWithPort("/api/deleteBook/" + (int)id), HttpMethod.DELETE, emptyEntityWithJwtHeader, String.class);
        Assert.assertEquals(HttpStatus.NO_CONTENT, afterDeleteResponse.getStatusCode());

        // get book after deleted
        ResponseEntity<String> afterTryToGetUpdatedResponse = testRestTemplate.exchange(createURLWithPort("/api/getBookById/" + (int)id), HttpMethod.GET, emptyEntityWithJwtHeader, String.class);

        Assert.assertEquals(null, afterTryToGetUpdatedResponse.getBody());
        Assert.assertEquals(HttpStatus.OK, afterTryToGetUpdatedResponse.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}

