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
        //
        public String UserLoggedIn(@PathParam("LessonID") Integer LessonID) {
            System.out.println("Invoked Quiz.Get() with LessonID " + LessonID);
            try {
                // Get the data from the quiz table
                PreparedStatement ps = main.db.prepareStatement("SELECT * FROM Quizzes WHERE LessonID = ?");
                ps.setInt(1, LessonID);
                ResultSet results = ps.executeQuery();
                JSONObject response = new JSONObject();
                // If there's a session token in the field, the user is logged in so return true
                if (results.next() == true) {
                    if (results.getString(1) != null) {
                        response.put("SessionToken", true);
                    } else {
                        response.put("SessionToken", false);
                    }
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
        public String UserAttemptLogin(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password) {
            System.out.println("Invoked Users.AttemptLogin()");
            try {
                // Checks for a password under the given username
                // Could throw an error is that user does not exist
                PreparedStatement ps = main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
                ps.setString(1, Username);
                ResultSet results = ps.executeQuery();
                JSONObject response = new JSONObject();

                if (results.next() == true) {

                    if (results.getString(1).equals(sha2Hex)) {
                        //https://docs.google.com/presentation/d/1nMsSzWwXeCvzod9FwE4b96sdEH8hTkaLcfv8OI6oDV4/edit?usp=sharing

                        // create a random session token
                        String token = UUID.randomUUID().toString();

                        // set the session token in the database to the value calculated above
                        PreparedStatement ps2 = main.db.prepareStatement("UPDATE Users SET SessionToken = ? WHERE Username = ?");
                        ps2.setString(1, token);
                        ps2.setString(2, Username);
                        ps2.executeUpdate();

                        // output the result
                        response.put("Success", true);
                        response.put("Username", Username);
                        response.put("SessionToken", token);
                    } else {
                        response.put("Success", false);
                    }
                }

                return response.toString();
            } catch (Exception exception) {
                System.out.println("Database error: " + exception.getMessage());
                return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
            }
        }
    }
