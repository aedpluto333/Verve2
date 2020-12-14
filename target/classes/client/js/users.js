/*"use strict";
function getUsersList() {
    console.log("Invoked ListLessons()");
    const url = "/lessons/list/";    		// API method on web server is in Users class
    fetch(url, {
        method: "GET",				//Get method
    }).then(response => {
        return response.json();                 //return response as JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) { //checks if response from the web server has an "Error"
            alert(JSON.stringify(response));    // if it does, convert JSON object to string and alert (pop up window)
        } else {
            formatUsersList(response);          //this function will create an HTML table of the data (as per previous lesson)
        }
    });
}

function formatUsersList(myJSONArray){
    let dataHTML = "";
    for (let item of myJSONArray) {
        dataHTML += "<tr><td>" + item.UserID + "<td><td>" + item.UserName + "<tr><td>";
    }
    document.getElementById("UsersTable").innerHTML = dataHTML;
}
*/

function getImageRecommended() {
    console.log("Invoked getImageRecommendation()");
    //debugger;
    const url = "/users/recommend/";                       // API method on webserver
    fetch(url, {
        method: "GET",
    }).then(response => {
        return response.json();                            //return response to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {         //checks if response from server has an "Error"
            alert(JSON.stringify(response));               // if it does, convert JSON object to string and alert
        } else {
            document.getElementById("recommendedImage").src = response;
        }
    });
}
