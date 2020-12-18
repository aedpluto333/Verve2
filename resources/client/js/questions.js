function getQuestion(){
    console.log("invoked getQuestion()");
    let url = "/quiz/getQuestion";
    fetch(url, {
        method: "GET",
    }).then(response => {
        return response.json();                 //now return that promise to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert
        } else {
            //Cookies.set("Token", response.Token);
            //Cookies.set("Username", response.Username);
            //window.open("game.html", "_self");       //open index.html in same tab
        }
    });
}