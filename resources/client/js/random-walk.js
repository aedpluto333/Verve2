document.getElementById("gameTitle").innerHTML = "Random walks";
document.getElementById("gameDescription").innerHTML = "Randomness is fundamental to the world in which we live; a lot of Quantum Mechanics is in fact probibalistic. Random walks are a classic example of randomness we see in the real world, where particle randomly chooses to step left, right, up or down. An example of such a random walk is shown above.";

class Walker {
    constructor() {
        this.x = width/2;
        this.y = height/2;
        this.choice = 0;
    }

    display() {
        stroke(255);
        point(this.x, this.y);
    }

    step() {
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
    var canvas = createCanvas(window.innerWidth*0.46875, window.innerHeight*0.66176);
    canvas.parent('lessonPanel');
    w = new Walker();
    background(0);
}

function draw() {
    w.step();
    w.display();
}