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
    public String ListLessons(@PathParam("SearchTerm") String SearchTerm) {
        System.out.println("Invoked Lessons.ListLessons() with search term " + SearchTerm);
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
            int slotsFull = 0;
            String[] searchResults = new String[slots];

            // prevent there from being too many search results
            // not sorting based on the quality of the result
            if (results.next() == true) {
                if (slotsFull < slots) {
                    searchResults[slotsFull] = results.getString(1);
                    slotsFull++;
                }
            }

            // Output the search results in one array
            // with a maximum length of "slots"
            response.put("Results", searchResults);

            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get data, please see server console for more info.\"}";
        }
    }


    // probably don't need this GetNextLesson API method
    // store the current lesson number on the page
    // when next is pressed add one to the lesson number then reload the page
    // converting it to an ordinary GetLesson method

    @POST
    @Path("getlesson")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String GetLesson(@FormDataParam("LessonID") int LessonID) {
        System.out.println("Invoked Lessons.GetLesson() with LessonID " + LessonID);
        try {
            PreparedStatement ps = main.db.prepareStatement("SELECT * FROM Lessons WHERE LessonID = ? + 1");
            ps.setInt(1, LessonID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();

            // Put all lesson data into an array                !!!!!!!!!!!!!!!!! check the number of spaces needed in this array
            String[] lessonData = new String[20];
            int i=0;

            if (results.next() == true) {
                lessonData[i] = results.getString(1);
                i++;
            }

            // output the lesson data
            response.put("LessonData", lessonData);
            return response.toString();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get data, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("recommend")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
        // API takes a search term as input in the URL
        public String Recommend() {
        System.out.println("Invoked Lessons.Recommend()");
        try {
            // find the number of courses in the database
            // and randomly select a course ID
            PreparedStatement ps1 = main.db.prepareStatement("FLOOR(RAND()*COUNT(SELECT CourseID FROM Courses))");
            ResultSet results1 = ps1.executeQuery();
            JSONObject response1 = new JSONObject();

            // find the number of lessons in the course
            // and randomly select a lesson ID
            PreparedStatement ps2 = main.db.prepareStatement("FLOOR(RAND()*COUNT(SELECT LessonID FROM Lessons WHERE CourseID = ?))");
            ResultSet results2 = ps2.executeQuery();
            JSONObject response2 = new JSONObject();

            // return the lesson title, lessonID and the associated image
            PreparedStatement ps3 = main.db.prepareStatement("SELECT LessonID, Name, Picture FROM WHERE LessonID = ?");
            ResultSet results3 = ps3.executeQuery();
            JSONObject response3 = new JSONObject();
            String labels[] = {"LessonID", "Name", "Picture"};
            int count = 0;
            if (results3.next() == true) {
                response3.put(labels[count], results3.getString(1));
                count++;
            }

            return response3.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get data, please see server console for more info.\"}";
        }
    }

}