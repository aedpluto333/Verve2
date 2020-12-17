package controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Path("lessons/")

public class lessons {
    @GET
    @Path("list")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // API returns a list of all titles and ids
    public String ListLessons() {
        System.out.println("Invoked Lessons.ListLessons()");
        try {

            PreparedStatement ps = main.db.prepareStatement("SELECT LessonID, Name FROM lessons");
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            JSONArray lessons = new JSONArray();

            // not sorting based on the quality of the result
            while (results.next() == true) {
                JSONArray currentLesson = new JSONArray();
                currentLesson.add(results.getString(1));
                currentLesson.add(results.getString(2));
                lessons.add(currentLesson);
            }

            // Output the search results in one array
            response.put("Results", lessons);

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

    @GET
    @Path("getlesson/{LessonID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // This API method lists all of the data required to display a lesson
    public String GetLesson(@PathParam("LessonID") Integer LessonID) {
        System.out.println("Invoked Lessons.GetLesson() with LessonID " + LessonID);
        try {
            PreparedStatement ps = main.db.prepareStatement("SELECT * FROM lessons WHERE LessonID = ?");
            ps.setInt(1, LessonID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();

            // Put all lesson data into a JSON array
            JSONArray lessonData = new JSONArray();

            int i = 0;
            if (results.next() == true) {
                for (i = 1; i < 10; i++) {
                    System.out.println(results.getString(i));
                    lessonData.add(results.getString(i));
                }
            }

            if (i==0) {
                response.put("Error", "Not a valid lesson number");
                return response.toString();
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
        // API recommends a lesson to the user (for the user homepage)
        public String Recommend() {
        System.out.println("Invoked Lessons.Recommend()");
        try {
            // these should be three separate sql statements - evaluation - split for readability

            // find the number of courses in the database
            // and randomly select a course ID

            // find the number of lessons in the course
            // and randomly select a lesson ID

            // return the lesson title, lessonID and the associated image
            PreparedStatement ps = main.db.prepareStatement("SELECT LessonID, Name, Picture FROM WHERE LessonID = FLOOR(RAND()*COUNT(SELECT LessonID FROM Lessons WHERE CourseID = FLOOR(RAND()*COUNT(SELECT CourseID FROM Courses))))");
            ResultSet results3 = ps.executeQuery();
            JSONObject response = new JSONObject();
            String labels[] = {"LessonID", "Name", "Picture"};
            int count = 0;
            if (results3.next() == true) {
                response.put(labels[count], results3.getString(1));
                count++;
            }

            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get data, please see server console for more info.\"}";
        }
    }

}