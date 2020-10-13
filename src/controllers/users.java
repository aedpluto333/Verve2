package controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("users/")

public class users {
     // create function for sha256 hash
    // https://www.baeldung.com/sha-256-hashing-java
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @GET
    @Path("loggedin/{UserID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UserLoggedIn(@PathParam("UserID") Integer UserID) {
        System.out.println("Invoked Users.GetUser() with UserID " + UserID);
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

    @GET
    @Path("attemptlogin/u={Username}p={Password}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UserAttemptLogin(@PathParam("Username") String Username, @PathParam("Password") String Password) {
        System.out.println("Invoked Users.GetUser() with Username,Password " + Username + ", " + Password);
        try {
            PreparedStatement ps = main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
            ps.setString(1, Username);
            ResultSet results = ps.executeQuery();
            JSONObject response = new JSONObject();
            if (results.next() == true) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] encodehash = digest.digest(Password.getBytes(StandardCharsets.UTF_8));
                digest = MessageDigest.getInstance("SHA-256");
                final byte[] hashbytes = digest.digest(Password.getBytes(StandardCharsets.UTF_8));
                String sha2Hex = bytesToHex(hashbytes);

                // See if password guess equals the password
                if (results.getString(1) == sha2Hex) {
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
