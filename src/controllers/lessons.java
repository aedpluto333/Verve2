package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


@Path("lessons/")

public class lessons {
    @GET
    @Path("list/{SearchTerm}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // API takes a search term as input in the URL
    public String ListLessons(@PathParam("SearchTerm") Integer SearchTerm) {
        System.out.println("Invoked Users.LoggedIn() with UserID " + UserID);
        try {
            // Search the database for all lessons where
            // a) the search term is in the title
            // b) the title is in the search term
            // c) the search term is in the description
            // d) the search term is in the extra info

            PreparedStatement ps = main.db.prepareStatement("SELECT LessonID, Name FROM Lessons WHERE ? IN (SELECT Name, Description, ExtraInfo FROM Lessons)");
            ps.setString(1, SearchTerm);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();

            int slots = 50;
            int slotsFull - 0;
            String searchResults[slotsFull] = {};

            // prevent there from being too many search results
            // not sorting based on the quality of the result
            if (results.next() == true) {
                if (slotsFull < slots) {
                    searchResults[slotsFull] = results.getString(1);
                    slotsFull++;
                }
            }

            // Output the search results in one array
            // with a maxium length of "slots"
            response.put("Results", searchResults);

            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get data, please see server console for more info.\"}";
        }
    }

    @POST
    @PATH("getnextlesson")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetNextLesson(@FormDataParam("LessonID") int LessonID) {
        try {
            PreparedStatement ps = main.db.prepareStatement("SELECT * FROM Lessons WHERE LessonID = ? + 1");
            ps.setInt(1, LessonID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();

            if (results.next() == true) {
                response.put(results.getInt(1))
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get data, please see server console for more info.\"}";
        }
    }


}