// login function to allow user to access the website
function logInUser() {
    console.log("Invoked logInUser()");
    const formData = new FormData(document.getElementById('LoginForm'));
    let url = "/users/attemptlogin";
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        console.log("Line 10");
        return response.json()
    }).then(response => {
        console.log("Line 13");
        if (response.hasOwnProperty("Error")) {
            console.log("Line 15");
            alert(JSON.stringify(response));
        } else {
            // URL replaces the current page
            console.log("Line 19");
            window.open("/client/user.html", "_self");
        }
    });
}

// logout function to remove user session token from database