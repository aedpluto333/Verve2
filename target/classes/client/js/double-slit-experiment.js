var time = 0;
function setup() {
    var canvas = createCanvas(window.innerWidth*0.46875, window.innerHeight*0.66176);
    canvas.parent('lessonPanel');
    background(0);
}

function randn_bm() {
    let u = 0, v = 0;
    while(u === 0) u = Math.random(); //Converting [0,1) to (0,1)
    while(v === 0) v = Math.random();
    let num = Math.sqrt( -2.0 * Math.log( u ) ) * Math.cos( 2.0 * Math.PI * v );
    num = num / 10.0 + 0.5; // Translate to 0 -> 1
    if (num > 1 || num < 0) return randn_bm(); // resample between 0 and 1
    return num;
}

function draw() {
    var dotSize = 10;

    // draw slits
    var slit1x = width*0.3;
    var slit2x = width*0.7;
    var slity = height*0.8;

    noStroke();
    fill(255);
    rect(0, height*0.8, width, 1);
    fill(0);
    rect(slit1x, slity, width*0.01, 1);
    rect(slit2x, slity, width*0.01, 1);

    // coherent waves -> purely constructive
    var num1 = randn_bm();
    var num3 = randn_bm();
    var sd = width*1.5;
    var mean = width*0.5;

    var x1 = sd * num1 + mean;
    var x3 = sd * num3 + mean;

    // single slit
    fill(0, 255, 0, 5);
    ellipse(x1-(sd/2), height*0.2, dotSize, dotSize);

    // double slit
    var xpos = x3-(sd/2);
    var ypos = height*0.1;
    var distToSlit1 = ((xpos-slit1x)**2 + (ypos-slity)**2)**(1/2);
    var distToSlit2 = ((xpos-slit2x)**2 + (ypos-slity)**2)**(1/2);

    // put into a scale which is visible on the user's screen
    distToSlit1 -= 290;
    distToSlit2 -= 290;
    distToSlit1 *= 12;
    distToSlit2 *= 12;

    // is the break in blue dots caused by the distance to the slits rather than
    // the function actually working?
    //javascript works in radians -> multiply by (pi/180)
    var intensity = (Math.sin((distToSlit1*Math.PI)/180) + Math.sin((distToSlit2*Math.PI)/180)+2)*50;


    fill(255, 0, 0, intensity);
    ellipse(xpos, ypos, dotSize, dotSize);

    // refresh screen after a certain length of time
    time++;
    if (time % 3000 == 0) {
        background(0);
    }
}