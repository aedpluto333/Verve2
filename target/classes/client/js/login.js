// login function to allow user to access the
//        !!! LOGGING IN ONLY EVER WORKS ON THE SECOND ATTEMPT !!!          ///
//            IF SUCCESS=FALSE DISPLAY P TAG ON THE ACTUAL
//            PAGE AND DON'T REDIRECT TO USERS

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
            Cookies.set("UserID", response.UserID);
            window.open("user.html", "_self");
        }
    });
}

// logout function to remove user session token from database
function logout() {
    //debugger;
    console.log("Invoked logout");
    let url = "/users/logout";
    fetch(url, {method: "POST"
    }).then(response => {
        return response.json();                 // now return that promise to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert
        } else {
            Cookies.remove("SessionToken", response.SessionToken);    //UserName and Token are removed
            Cookies.remove("Username", response.Username);
            Cookies.remove("UserID", response.UserID);
            window.open("login.html", "_self");       //open index.html in same tab
        }
    });
}

