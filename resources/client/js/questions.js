function getQuestion(){
    console.log("Invoked getQuestion();");
    let url = "/quiz/get/"+document.cookie.split("LessonID=")[1].split(";")[0];
    fetch(url, {
        method: "GET",
    }).then(response => {
        return response.json();                 //now return that promise to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            console.log(response.Error);        // if it does, convert JSON object to string and console output
        } else {
            document.getElementById("quizQuestion").innerHTML = response.Question;
            for (i=1; i<=4; i++) {
                document.getElementById("option"+i).value = response.Options[i-1][0];
                document.getElementsByTagName("label")[i-1].innerHTML = response.Options[i-1][1];
            }
        }
    });
}

function quizMark() {
    console.log("Invoked quizMark();");

    // get user id
    const usid = document.cookie.split("UserID=")[1].split(";")[0];

    // get which radio button is checked
    var option;
    if (document.getElementById('option1').checked) {
        option = document.getElementById('option1').value;
    } else if (document.getElementById('option2').checked) {
        option = document.getElementById('option2').value;
    } else if (document.getElementById('option3').checked) {
        option = document.getElementById('option3').value;
    } else if (document.getElementById('option4').checked) {
        option = document.getElementById('option4').value;
    } else {
        alert("You haven't checked any of the options");
        return "";
    }

        // create a form containing data necessary to mark the quiz
    var formData = new FormData();
    formData.append('UserID', usid);
    formData.append('OptionID', option);
    var url = "/quiz/mark";

    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()          //method returns a promise, have to return from here to get text
    }).then(response => {
        if (response.hasOwnProperty("Error")) {   //checks if response from server has a key "Error"
            alert(response.Error);        // if it does, convert JSON object to string and alert
        } else {
            document.getElementById("quizResult").innerHTML = response.QuizResult;
        }
    });
}
