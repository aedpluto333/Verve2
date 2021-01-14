// object to describe the position of each dot
class Walker {
    constructor() {
        this.x = width/2;
        this.y = height/2;
        // which direction to move in
        this.choice = 0;
    }

    display() {
        stroke(255);
        point(this.x, this.y);
    }

    step() {
        // randomly choose which direction to step in
        this.choice = int(Math.random()*4);

        if (this.choice==0) {
            this.x++;
        } else if (this.choice==1) {
            this.x--;
        } else if (this.choice==2) {
            this.y++;
        } else {
            this.y--;
        }
    }
}

function setup() {
    // set up simulation on webpage
    var canvas = createCanvas(window.innerWidth*0.46875, window.innerHeight*0.66176);
    canvas.parent('lessonPanel');
    w = new Walker();
    background(0);
}

function draw() {
    // run simulation
    w.step();
    w.display();
}