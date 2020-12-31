"use strict";
function getLessonsList() {
    //debugger;
    console.log("Invoked getLessonsList();");
    const url = "/lessons/list";
    fetch(url, {
        method: "GET",				                //Get method
    }).then(response => {
        return response.json();                     //return response as JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {  //checks if response from the web server has an "Error"
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert (pop up window)
        } else {
            formatLessonsList(response.Results);    //this function will create an HTML table of the data (as per previous lesson)
        }
    });
}

function formatLessonsList(myJSONArray){
    let dataHTML = "";
    for (let item of myJSONArray) {
        // inject div into the HTML page
        dataHTML += "<div class=\"lessonInfoBlock\">\n" +
            "        <p>"+item[1]+": "+item[2]+"</p>\n" +
            "        <p><a onClick=\"Cookies.set('LessonID', "+item[0]+");\" href=\"index.html\">Start lesson >></a></p>\n" +
            "    </div>";
    }
    document.getElementById("mainPanel").innerHTML = dataHTML;
}

function getLesson() {
    //debugger;
    console.log("Invoked getLesson();");
    const url = "/lessons/get/"+document.cookie.split("; ")[3].split("=")[1];
    fetch(url, {
        method: "GET",
    }).then(response => {
        return response.json();                          //return response as JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {       //checks if response from the web server has an "Error"
            alert(JSON.stringify(response));             //if it does, convert JSON object to string and alert (pop up window)
            if (response.Error == "Not a valid lesson number") {
                window.open("courses.html", "_self");
            }
        } else {
            // set elements on page to the data returned
            document.getElementById("gameTitle").innerHTML = response.LessonData[1]; // page title
            document.getElementById("simulation").src = response.LessonData[3]; // simulation
            document.getElementById("gameDescription").innerHTML = response.LessonData[5]; // description
            document.getElementById("extraInfo").innerHTML = response.LessonData[6]; // extra info
            document.getElementById("nextButton").onclick = "Cookies.set('LessonID', "+(response.LessonData[2]+1)+"); window.open(\"index.html\", \"_self\");"; // next button
        }
    });
}

function getNextLesson() {
    // would need a much more complicated query to get the lessons in the order they are
    // in the course - they will be jambled up in the database
    console.log("Invokes getNextLesson();");
    // set the new lesson ID to the current one plus 1
    try {
        Cookies.set("LessonID", parseInt(document.cookie.split("; ")[3].split("=")[1]) + 1);
        // reload the page to show the new lesson
        window.open("index.html", "_self");
    } catch (error) {
        console.log(error);
        window.open("courses.html", "_self");
    }
}