import controlP5.*;

ControlP5 control;

double scaleFactor = 1;
double translationX;
double translationY;

App app;

void setup ()
{
  app = new App(this);
}

void draw ()
{
  double z = zoomSlider.value();
  double oz = z;
  if (z < 0)
    z = 1 / (-z + 1);
  else
    z += 1;
  //print(oz + " " + z + "\n");
  translate((float)(-width*z/2+width/2), (float)(-height*z/2+height/2));
  scaleFactor = z;
  translationX = -width*z/2+width/2;
  translationY = -height*z/2+height/2;
  scale((float)z);
  //print((width*z) + "\n");
  background(0);
  fill(255,255,255);
  //(new Vector2D(300,300)).drawCircle(30);
  app.draw();
  resetMatrix();
}

Vector2D getMousePos ()
{
  return new Vector2D(mouseX-translationX, mouseY-translationY).dividedBy(scaleFactor);
}

void mousePressed ()
{
  app.mousePressed(getMousePos());
}

void mouseMoved ()
{
  app.mouseMoved(getMousePos());
}

void mouseReleased ()
{
  app.mouseReleased(getMousePos());
}

void keyPressed ()
{
  app.keyPressed(key);
}

void mouseDragged ()
{
  app.mouseDragged(getMousePos());
}

