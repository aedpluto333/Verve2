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
                PreparedStatement ps2 = main.db.prepareStatement("SELECT Option FROM options WHERE QuestionID = (SELECT QuestionID FROM lessons WHERE LessonID = ?)");
                ps2.setInt(1, LessonID);
                ResultSet results2 = ps2.executeQuery();

                // create the array for the results to be stored in
                JSONArray options = new JSONArray();

                // expect four options
                while (results2.next() == true) {
                    options.add(results2.getString(1));
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
        public String Mark(@FormDataParam("Option") int Option, @FormDataParam("UserID") String UserID) {
            System.out.println("Invoked Quiz.Mark()");
            try {
                PreparedStatement ps1 = main.db.prepareStatement("SELECT Correct FROM options WHERE Option = ?;");
                ps1.setInt(1, Option);
                ResultSet validityOfOption = ps1.executeQuery();
                JSONObject response = new JSONObject();

                // Return whether correct or false
                boolean returnedResult = false;
                if (validityOfOption.next() == true) {
                        response.put("QuizResult", validityOfOption.getBoolean(1));
                        returnedResult = true;
                }

                // stop running API method if the option is not in the database
                // there may be the problem that if two options are the same
                // but are associated with different questions
                // the API method may only read the first to come up
                if (!returnedResult) {
                    response.put("Error", "Invalid quiz option");
                    return response.toString();
                }

                // update the quizguesses table
                PreparedStatement ps2 = main.db.prepareStatement("INSERT INTO quizguesses (OptionID, UserID) VALUES ((SELECT OptionID FROM options WHERE Option=?), ?)");
                ps2.setInt(1, Option);
                ResultSet updateQuizGuessesTable = ps2.executeQuery();

                if (updateQuizGuessesTable.next() == true) {
                    System.out.println("Ohhh this got triggered");
                }


                return response.toString();
            } catch (Exception exception) {
                System.out.println("Database error: " + exception.getMessage());
                return "{\"Error\": \"Unable to mark quiz, please see server console for more info.\"}";
            }
        }
    }
