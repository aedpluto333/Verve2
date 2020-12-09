// login function to allow user to access the website
function logInUser() {
    console.log("Invoked Login()");
    const formData = new FormData(document.getElementById('LoginForm'));
    let url = "/users/attemptlogin";
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));
        } else {
            // URL replaces the current page
            window.open("/client/user.html", "_self");
        }
    });
}

// logout function to remove user session token from database