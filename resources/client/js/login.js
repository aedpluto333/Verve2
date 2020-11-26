//addUser function to add a user to the database
function addUser() {
    console.log("Invoked AddUser()");
    const formData = new FormData(document.getElementById('InputUserDetails'));
    let url = "/users/add";
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));
        } else {
            window.open("/client/welcome.html", "_self");   //URL replaces the current page.  Create a new html file
        }                                                  //in the client folder called welcome.html
    });
}

//Add the form elements to index.html inside <body> tags
<form id="InputUserDetails">
    Username: <input type="text" name="Username">
    Password: <input type="text" name="Password">
</form>
<button onclick='addUser();'>Add User</button>