package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import server.main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

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

                if (results1.next() == true) {
                    response.put("Question", results1.getString(1));
                }

                // Get the options from the quiz table
                PreparedStatement ps2 = main.db.prepareStatement("SELECT Option FROM options WHERE OptionID = (SELECT OptionID FROM options WHERE QuestionID = (SELECT QuestionID FROM lessons WHERE LessonID = ?))");
                ps2.setInt(1, LessonID);
                ResultSet results2 = ps2.executeQuery();
                // could be coded better
                String labels[] = {"Option1", "Option2", "Option3", "Option4"};
                int count = 0;

                if (results1.next() == true) {
                    response.put(labels[count], results2.getString(1));
                    count++;
                }

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
            System.out.println("Invoked Users.AttemptLogin()");
            try {
                // Update score in progresses table
                // Update quizguesses
                // Return whether correct or false
                PreparedStatement ps = main.db.prepareStatement("SELECT Correct FROM options WHERE Option = ?;" +
                        "INSERT INTO quizGuesses (OptionID, UserID) VALUES ((SELECT OptionID FROM Options WHERE Option=?), ?)";
                ps.setInt(1, Option);
                ResultSet results = ps.executeQuery();
                JSONObject response = new JSONObject();

                if (results.next() == true) {
                        response.put("QuizResult", results.getString(1));
                }

                return response.toString();
            } catch (Exception exception) {
                System.out.println("Database error: " + exception.getMessage());
                return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
            }
        }
    }
