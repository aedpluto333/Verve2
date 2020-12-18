package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("quiz/")

public class quiz {

    @GET
    @Path("get/{LessonID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // API method to get the quiz question and options
    public String GetQuiz(@PathParam("LessonID") Integer LessonID) {
        System.out.println("Invoked Quiz.Get() with LessonID " + LessonID);
        try {
            // Get the question from the quiz table
            PreparedStatement ps1 = main.db.prepareStatement("SELECT Question FROM questions WHERE QuestionID = (SELECT QuestionID FROM lessons WHERE LessonID = ?)");
            ps1.setInt(1, LessonID);
            ResultSet results1 = ps1.executeQuery();
            JSONObject response = new JSONObject();

            boolean returnedResult = false;
            while (results1.next() == true) {
                response.put("Question", results1.getString(1));
                returnedResult = true;
            }

            // If no question could be returned there must be a problem
            if (!returnedResult) {
                response.put("Error", "Invalid LessonID");
                return response.toString();
            }

            // Get the options from the quiz table
            PreparedStatement ps2 = main.db.prepareStatement("SELECT OptionID, Option FROM options WHERE QuestionID = (SELECT QuestionID FROM lessons WHERE LessonID = ?)");
            ps2.setInt(1, LessonID);
            ResultSet results2 = ps2.executeQuery();

            // create the array for the results to be stored in
            JSONArray options = new JSONArray();

            // expect four options
            while (results2.next() == true) {
                // create an array for each option [OptionID, Option]
                JSONArray currentOption = new JSONArray();
                currentOption.add(results2.getString(1));
                currentOption.add(results2.getString(2));
                // Add the array for this specific option to the options array
                options.add(currentOption);
            }

            response.put("Options", options);
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("mark")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // API to mark the quiz on the lessons page
    // update the database
    // then return a result
    public String Mark(@FormDataParam("OptionID") int OptionID, @FormDataParam("UserID") int UserID) {
        System.out.println("Invoked Quiz.Mark() with OptionID " + OptionID + " and UserID " + UserID);
        try {
            // check the result of the quiz
            PreparedStatement ps1 = main.db.prepareStatement("SELECT Correct FROM options WHERE OptionID = ?;");
            ps1.setInt(1, OptionID);
            ResultSet validityOfOption = ps1.executeQuery();
            JSONObject response = new JSONObject();

            // Return whether correct or false
            boolean returnedResult = false;
            while (validityOfOption.next() == true) {
                response.put("QuizResult", validityOfOption.getString(1));
                returnedResult = true;
            }

            // stop running API method if the OptionID is not in the database
            if (!returnedResult) {
                response.put("Error", "Invalid quiz option");
                return response.toString();
            }

            // update the quizguesses table
            PreparedStatement ps2 = main.db.prepareStatement("INSERT INTO quizguesses (OptionID, UserID) VALUES (?, ?)");
            ps2.setInt(1, OptionID);
            ps2.setInt(2, UserID);
            ps2.executeUpdate();

            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to mark quiz, please see server console for more info.\"}";
        }
    }
}