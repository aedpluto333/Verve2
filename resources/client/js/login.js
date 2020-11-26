//login function to add a user to the database
function logInUser() {
    console.log("Invoked Login()");
    const formData = new FormData(document.getElementById('LoginForm'));
    let url = "/users/login";
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));
        } else {
            window.open("/client/user.html", "_self");   //URL replaces the current page.  Create a new html file
        }                                                  //in the client folder called welcome.html
    });
}