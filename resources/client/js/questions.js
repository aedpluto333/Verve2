function getQuestion(){
    console.log("Invoked getQuestion();");
    let url = "/quiz/get/"+document.cookie.split("; ")[3].split("=")[1];
    fetch(url, {
        method: "GET",
    }).then(response => {
        return response.json();                 //now return that promise to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert
        } else {
            document.getElementById("quizQuestion").innerHTML = response.Question;
            for (i=1; i<=4; i++) {
                document.getElementById("option"+i).value = response.Options[i-1];
                document.getElementsByTagName("label")[i-1].innerHTML = response.Options[i-1];
            }
        }
    });
}

function quizMark() {
    console.log("Invoked quizMark();");

    const date = document.getElementById('datepicker').value;
    const weightInKG = document.getElementById('weightInKG').value;

    // create a form containing data necessary to mark the quiz
    var formData = new FormData();
    formData.append('date', date);
    formData.append('weightInKG', weightInKG);
    var url = "/weight/add";

    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()          //method returns a promise, have to return from here to get text
    }).then(response => {
        if (response.hasOwnProperty("Error")) {   //checks if response from server has a key "Error"
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert
        } else {
            getWeightList();
        }
    });
}
