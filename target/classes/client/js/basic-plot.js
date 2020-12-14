// next add an input box to the graph to allow the user to enter an exponent
// then modify the graph based on the user's input


document.getElementById("gameTitle").innerHTML = "y=x^2";

// set up game

const fps = 30;
canvas = document.getElementById("gameCanvas");
context = canvas.getContext("2d");
const centre = [canvas.width/2, canvas.height/2];

// allow for logging of key presses

var keyboardInput = "";

document.body.addEventListener('keydown', function(event){
    var key = event.key;
    if (key === "0"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "1"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "2"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "3"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "4"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "5"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "6"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "7"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "8"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "9"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "."){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "-"){
        console.log(key);
        keyboardInput = keyboardInput + key;
        return false;
    } else if (key === "Backspace"){
        console.log(key);
        keyboardInput = keyboardInput.substring(0, keyboardInput.length-1);
        return false;
    }
});

// plot graph

setInterval(update, 1000/fps);

function update() {
    // create points

    var x = [];
    var y = [];


    // reset exponent to 1 if backspace is pressed too many times
    // ie -> keyboard input is an empty string
    var exponent = keyboardInput;

    if (keyboardInput === ""){
        exponent = "1";
    }

    for (i=-400; i<=400; i+=0.005) {
        x.push(i);
        y.push((0.1*i)**Number(exponent));
    }

    document.getElementById("gameTitle").innerHTML = "y=x^"+exponent;

    context.fillStyle = "black";
    context.fillRect(0, 0, canvas.width, canvas.height);
    drawAxes();
    context.fillStyle = "yellow";
    for (i=0; i<x.length-1; i++) {
        var xi = centre[0] + x[i];
        var yi = centre[1] - y[i];
        context.fillRect(xi, yi, 2, 2);
        //drawInputBox();
    }
}

function drawAxes() {
    context.fillStyle = "white";

    for (i=0; i<=canvas.height; i+=0.05) {
        context.fillRect(centre[0], i, 2, 2);

        if (i.toFixed(2)%50 == 0) {
            context.font = "15px Arial";
            context.textAlign = "right";
            context.fillText((centre[1]-i).toFixed(0), centre[0]-5, i+20);
        }
    }

    for (i=0; i<=canvas.width; i+=0.05) {
        context.fillRect(i, centre[1], 2, 2);

        if (i.toFixed(2)%50 == 0) {
            context.font = "15px Arial";
            context.fillText((i-centre[0]).toFixed(0), i-5, centre[1]+20);
        }
    }
}

//function drawInputBox() {
//
//}

//function zoom(int factor) {
//    x->(x/factor)
//    y->(y/factor)
//    max->max/factor
//    min->min/factor
//}