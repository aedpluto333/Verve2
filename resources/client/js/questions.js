function getQuestion(){
    console.log("invoked getQuestion();");
    let url = "/quiz/get/0";
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