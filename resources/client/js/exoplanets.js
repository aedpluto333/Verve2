document.getElementById("gameTitle").innerHTML = "Discovering exoplanets";
document.getElementById("gameDescription").innerHTML = "";

function setup() {
    var canvas = createCanvas(window.innerWidth*0.46875, window.innerHeight*0.66176);
    canvas.parent('lessonPanel');
    background(0);
}

// planet data
var p = [window.innerWidth*0.46875/2,
    window.innerHeight*0.66176/3,
    window.innerWidth*0.46875/20,
    true];
var s = [window.innerWidth*0.46875/2,
    window.innerHeight*0.66176/3,
    window.innerWidth*0.46875/4];
var count = 0;
var graphHeight = 0;
var behindStar = false;

function draw() {
    background(0);
    noStroke();

    // draw star and planet
    if (behindStar) {
        // draw the planet first
        fill(100);
        ellipse(p[0], p[1], p[2], p[2]);

        // then draw the star over it
        fill(255, 255, 0);
        ellipse(s[0], s[1], s[2], s[2]);
    } else {
        // draw the star first
        fill(255, 255, 0);
        ellipse(s[0], s[1], s[2], s[2]);

        // then draw the planet over it
        fill(100);
        ellipse(p[0], p[1], p[2], p[2]);
    }
    // update position of planet for the next iteration
    count += 0.01;
    p[0] = width/2 + Math.sin(count)*200;

    // draw graph beneath
    var rightOfStar = s[0]+s[2]*0.5;
    var leftOfStar = s[0]-s[2]*0.5;

    // if the angle is between +- pi/2 radians the planet should be in front of the star
    // otherwise it should be behind it
    // tau is 6.28
    if (count >= 6.28) {
        count=0;
    } else if (count.toFixed(2) == 1.57) {
        behindStar = !behindStar;
    } else if (count.toFixed(2) == 4.71) {
        behindStar = !behindStar;
    }

    /*
    if ((rightOfStar > p[0]) && (leftOfStar < p[0])) {
        if (p[0] > rightOfStar-s[2]*0.1) {
            graphHeight += height*0.05/(rightOfStar-s[2]*0.1);
        } else if (p[0] < leftOfStar+s[2]*0.1) {
            graphHeight -= height*0.05/(leftOfStar+s[2]*0.1);
        }
        //graphHeight += height*0.05*0.1;
        //graphHeight = min(graphHeight, height*0.85);
        stroke(0, 255, 255);
    } else {
        graphHeight = 0;
        stroke(255);
    }
     */

    point(p[0], height*0.8+graphHeight);
    stroke(255);
    rect(0, height*0.9, width, 1);
}