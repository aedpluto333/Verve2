function getImageRecommended() {
    console.log("Invoked getImageRecommendation()");
    //debugger;
    const url = "/lessons/recommend/";
    fetch(url, {
        method: "GET",
    }).then(response => {
        //return response to JSON
        return response.json();
    }).then(response => {
        if (response.hasOwnProperty("Error")) {         //checks if response from server has an "Error"
            alert(JSON.stringify(response));               // if it does, convert JSON object to string and alert
        } else {
            document.getElementById("recommendedImage").src = response.Picture;
            // accessibility feature
            document.getElementById("recommendedImage").alt = response.LessonName;
        }
    });
}

"use strict";
function getUserProgress() {
    //debugger;
    console.log("Invoked getUserProgress();");
    const url = "/users/listprogress/"+document.cookie.split("UserID=")[1].split(";")[0];
    fetch(url, {
        method: "GET",				                //Get method
    }).then(response => {
        return response.json();                     //return response as JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {  //checks if response from the web server has an "Error"
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert (pop up window)
        } else {
            formatProgressBars(response.Results);    //this function will create an HTML table of the data (as per previous lesson)
        }
    });
}

function formatProgressBars(myJSONArray){
    let dataHTML = "";

    // if the array is empty display link to courses page
    if (myJSONArray.length == 0) {
        dataHTML = "<p class='mainContent'>You haven't completed any lessons yet. Go to <a href='courses.html' style='color:red;'><b>Courses</b></a> to start out.</p>";
    } else {

        // if there is data, display progress bars
        for (let item of myJSONArray) {
            // calculate percentage
            var percentage = ((item[1] / item[2]) * 100).toFixed(0);

            // inject div into the HTML page
            dataHTML += "<div class=\"mainContent\">\n" +
                "        <progress id=\"" + item[0] + "\" max=\"100\" value=\"" + percentage + "\"> " + percentage + "% </progress><br>\n" +
                "        <label for=\"" + item[0] + "\">" + percentage + "% through " + item[0] + "</label>\n" +
                "    </div><br>";
        }
    }
    console.log(dataHTML);
    document.getElementById("progressBars").innerHTML = dataHTML;
}