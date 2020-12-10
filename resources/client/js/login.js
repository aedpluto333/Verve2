// login function to allow user to access the
//        !!! LOGGING IN ONLY EVER WORKS ON THE SECOND ATTEMPT !!!          ///
function logInUser() {
    console.log("Invoked logInUser()");
    //debugger;
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
            Cookies.set("SessionToken", response.SessionToken);
            Cookies.set("Username", response.Username);
            window.open("/client/user.html", "_self");
            getImageRecommended();
        }
    });
}

// logout function to remove user session token from database
