package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import server.main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;


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
    // The API takes in the UserID as input
    public String UserLoggedIn(@PathParam("UserID") Integer UserID) {
        System.out.println("Invoked Users.LoggedIn() with UserID " + UserID);
        try {
            // Search the database for a session token on UserID
            PreparedStatement ps = main.db.prepareStatement("SELECT SessionToken FROM users WHERE UserID = ?");
            ps.setInt(1, UserID);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();

            // Returns true if search has been successful
            boolean successful = false;

            // If there's a session token in the field, the user is logged in so return true
            if (results.next() == true) {
                successful = true;
                if (results.getString(1) != null) {
                    response.put("LoggedIn", true);
                } else {
                    response.put("LoggedIn", false);
                }
            }
            // In case an invalid UserID is input
            response.put("Success", successful);
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    /*
    @POST
    @Path("attemptlogin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // API to help a user login
    // Takes form data as parameters: Username, Password
    public String UserAttemptLogin(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password) {
        System.out.println("Invoked Users.AttemptLogin()");
        try {
            // Checks for a password under the given username
            // Could throw an error is that user does not exist
            PreparedStatement ps1 = main.db.prepareStatement("SELECT Password FROM users WHERE Username = ?");
            ps1.setString(1, Username);
            ResultSet loginResults = ps1.executeQuery();
            JSONObject response = new JSONObject();

            if (loginResults.next() == true) {
                // Hash the password (guess) the user entered via the login page
                String sha2Hex = generateHash(Password);

                // See if password guess equals the password
                if (loginResults.getString(1).equals(sha2Hex)) {
                     //https://docs.google.com/presentation/d/1nMsSzWwXeCvzod9FwE4b96sdEH8hTkaLcfv8OI6oDV4/edit?usp=sharing

                    // create a random session token
                     String token = UUID.randomUUID().toString();

                     // set the session token in the database to the value calculated above
                     PreparedStatement ps2 = main.db.prepareStatement("UPDATE users SET SessionToken = ? WHERE Username = ?");
                     ps2.setString(1, token);
                     ps2.setString(2, Username);
                     ps2.executeUpdate();

                     // output the result
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

     */
    @POST
    @Path("attemptlogin")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // API to help a user login
    // Takes form data as parameters: Username, Password
    public String UserAttemptLogin(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password) {
        System.out.println("Invoked Users.AttemptLogin()");
        boolean successful = false;
        try {
            // Checks for a password under the given username
            // Could throw an error is that user does not exist
            PreparedStatement ps = main.db.prepareStatement("SELECT Password FROM users WHERE Username = ?");
            ps.setString(1, Username);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();

            if (results.next() == true) {
                // Hash the password (guess) the user entered via the login page
                String sha2Hex = generateHash(Password);

                // See if password guess equals the password
                if (results.getString(1).equals(sha2Hex)) {
                    //https://docs.google.com/presentation/d/1nMsSzWwXeCvzod9FwE4b96sdEH8hTkaLcfv8OI6oDV4/edit?usp=sharing

                    // create a random session token
                    String token = UUID.randomUUID().toString();

                    // set the session token in the database to the value calculated above
                    PreparedStatement ps2 = main.db.prepareStatement("UPDATE users SET SessionToken = ? WHERE Username = ?");
                    ps2.setString(1, token);
                    ps2.setString(2, Username);
                    ps2.executeUpdate();

                    // output the result
                    response.put("Username", Username);
                    response.put("SessionToken", token);

                    successful = true;
                }
            }

            response.put("Success", successful);
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("listprogress/{SessionToken}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // API to list the progress in each topic made by the user
    // Take session token as user input
    public String UserListProgress(@CookieParam("SessionToken") String SessionToken) {
        System.out.println("Invoked Users.UserListProgress() with SessionToken " + SessionToken);
        try {
            // look at the quizguesses link table and count the number of lessons completed for each course
            // gets the userid from the session token and finds the lessonscompleted from which quizzes have been done
            // join course titles
            PreparedStatement ps1 = main.db.prepareStatement("COUNT(SELECT OptionID FROM quizguesses" +
                    "WHERE UserID = (SELECT UserID FROM users WHERE SessionToken = ?))" +
                    "AND ");
            // count the number of lessons in each course
            PreparedStatement ps2 = main.db.prepareStatement("COUNT(SELECT Name FROM lessons WHERE CourseID=)" +
                    "AND ");
            // calculate the percentage of the way through each course
            // return the course name with the percentage
            ps1.setString(1, SessionToken);
            ResultSet results = ps1.executeQuery();
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
    @Path("logout")
    // might not need these two lines
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // API method to log a user out of their account
    public static String Logout(@CookieParam("SessionToken") String SessionToken){
        try{
            System.out.println("Invoked Users.Logout() with " + SessionToken);
            // Remove the session token from the database of the user currently logged in
            PreparedStatement ps = main.db.prepareStatement("SELECT UserID FROM users WHERE SessionToken=?");
            ps.setString(1, SessionToken);
            ResultSet logoutResults = ps.executeQuery();
            if (logoutResults.next()){
                int UserID = logoutResults.getInt(1);
                //Set the token to null to indicate that the user is not logged in
                PreparedStatement ps1 = main.db.prepareStatement("UPDATE users SET SessionToken = NULL WHERE UserID = ?");
                ps1.setInt(1, UserID);
                ps1.executeUpdate();
                // output if the user has been sucessfully logged out
                return "{\"Status\": \"OK\"}";
            } else {
                return "{\"Error\": \"Invalid session token\"}";

            }
        } catch (Exception ex) {
            System.out.println("Database error during /users/logout: " + ex.getMessage());
            return "{\"Error\": \"Server side error!\"}";
        }
    }

    // added for testing purposes, please do not remove

    @POST
    @Path("addUser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String addUser(@FormDataParam("Username") String username, @FormDataParam("Password") String password){
        try{
            PreparedStatement ps = main.db.prepareStatement("INSERT INTO users(Username, Password,SessionToken) VALUES (?,?,?)");
            String token = UUID.randomUUID().toString();
            ps.setString(1, username);
            ps.setString(2,generateHash(password));
            ps.setString(3, token);
            ps.executeUpdate();
            return "{\"OK\": \"Added user.\"}";
        }catch(Exception e){
            return "{\"Error\": \""+e.getMessage()+"\"}";
        }
    }


}
