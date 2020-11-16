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


@Path("users/")

public class users {
     // create function for sha256 hash
    // https://www.baeldung.com/sha-256-hashing-java
    // https://docs.google.com/presentation/d/1kpT9LC_3Ckwsk7YjoMKgYHTr9a_DcP3kZQ-oeea9anA/edit?usp=sharing

       public static String generateHash(String text) {
           try {
               MessageDigest hasher = MessageDigest.getInstance("SHA-256");
               hasher.update(text.getBytes());
               return DatatypeConverter.printHexBinary(hasher.digest()).toLowerCase();
           } catch (NoSuchAlgorithmException nsae) {
               return nsae.getMessage();
           }
       }

    @GET
    @Path("loggedin/{UserID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // The API takes in the UserID as input.
    // Search the database on that user
    // If there's a session token in the field
    // return true
    public String UserLoggedIn(@PathParam("UserID") Integer UserID) {
        System.out.println("Invoked Users.LoggedIn() with UserID " + UserID);
        try {
            PreparedStatement ps = main.db.prepareStatement("SELECT SessionToken FROM Users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next() == true) {
                if (results.getString(1) != "") {
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
    @Path("attemptlogin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UserAttemptLogin(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password) {
        System.out.println("Invoked Users.AttemptLogin()");
        try {
            PreparedStatement ps = main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
            ps.setString(1, Username);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();

            if (results.next() == true) {
                String sha2Hex = generateHash(Password);

                // See if password guess equals the password
                System.out.println(results.getString(1));
                System.out.println(sha2Hex);
                if (results.getString(1) == sha2Hex) {
                    /*
                    //https://docs.google.com/presentation/d/1nMsSzWwXeCvzod9FwE4b96sdEH8hTkaLcfv8OI6oDV4/edit?usp=sharing
                    String token = UUID.randomUUID().toString();
                   PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?");
                   ps2.setString(1, token);
                   ps2.setString(2, username);
                   ps2.executeUpdate();
                   JSONObject userDetails = new JSONObject();
                   userDetails.put("username", username);
                   userDetails.put("token", token);
                   return userDetails.toString();
                     */
                    response.put("Success", true);
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

    @GET
    @Path("listprogress/{UserID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UserListProgress(@PathParam("UserID") Integer UserID) {
        System.out.println("Invoked Users.listprogress() with UserID " + UserID);
        try {
            // look at the progresses link table and count the number of lessons completed for each course
            // actually this should use the session token instead of the user id
            // get userid from session token
            // get lessonscompleted from progresses
            // join course titles
            PreparedStatement ps = main.db.prepareStatement("SELECT LessonsCompleted FROM progresses WHERE UserID = ?");
            // count the lessons in the eah course to calculate a percentage of the way through
            // return the course name with the percentage
            ps.setInt(1, UserID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next() == true) {
                if (results.getString(1) != "") {
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
}
