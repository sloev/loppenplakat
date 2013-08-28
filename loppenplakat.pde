import g4p_controls.*;
import controlP5.*;
import processing.pdf.*;

ControlP5 cp5;
ColorPicker cp1, cp2, cp3;


color a, b, c;
PGraphics pdf;
String[] lines;


PFont arial, loppen, christiania;

GTextArea txaSample;
Textlabel myTextlabelB;

int sizes[] = {
  1, 8, 10, 14, 19
};

int space=2;

boolean save=false;

int pdfwidth=300;
void setup() {
  a=color(0, 0, 0);
  c=b=a;
  size(620, 800);
  frameRate(24);

  arial=loadFont("arial.vlw");

  lines = loadStrings("text.txt");


  txaSample = new GTextArea(this, 5, 5, 300, 500, G4P. SCROLLBARS_VERTICAL_ONLY  | G4P.SCROLLBARS_AUTOHIDE);
  txaSample.setText(PApplet.join(lines, '\n'), 100);

  cp5 = new ControlP5(this);

  myTextlabelB = cp5.addTextlabel("label1")
    .setText("FARVE A:")
      .setPosition(5, 505)
        .setColor(0x00000000)
          .setFont(createFont("Georgia", 15))
            ;

  myTextlabelB = cp5.addTextlabel("label2")
    .setText("FARVE B:")
      .setPosition(5, 590)
        .setColorValue(0x00000000)
          .setFont(createFont("Georgia", 15))
            ;

  myTextlabelB = cp5.addTextlabel("label3")
    .setText("FARVE C:")
      .setPosition(5, 670)
        .setColorValue(0x00000000)
          .setFont(createFont("Georgia", 15))
            ;


  cp1 = cp5.addColorPicker("picker1")
    .setPosition(5, 530)
      .setColorValue(color(255, 0, 0, 255))
        ;

  cp2 = cp5.addColorPicker("picker2")
    .setPosition(5, 610)
      .setColorValue(color(255, 221, 0, 255))
        ;

  cp3 = cp5.addColorPicker("picker3")
    .setPosition(5, 690)
      .setColorValue(color(255, 255, 255, 255))
        ;

  a=cp1.getColorValue();
  b=cp2.getColorValue();
  c=cp3.getColorValue();
  pdfupdate(calcPdfHeight(lines));
}

void draw() {
  background(200);
  if (!save) {
    image(pdf, width-5-pdfwidth, 5);
  }

  pdfupdate(calcPdfHeight(lines));
}

int calcPdfHeight(String [] lulz) {
  int returner=58;
  for (int i=0;i<lulz.length;i++) {
    String tmp=lines[i];
    if (tmp.indexOf("<<<<")!=-1) {//h1 (størst)
      returner+=sizes[4];
    }
    else if (tmp.indexOf("<<<")!=-1) {//h2
      returner+=sizes[3];
    }
    else if (tmp.indexOf("<<")!=-1) {//h3 (anden mindst)
      returner+=sizes[2];
    }
    else if (tmp.indexOf("[")!=-1 || tmp.indexOf("]")!=-1) {//linje
      returner+=(space*2)+2;
    }
    else if (tmp.indexOf("<")!=-1) {//h3 (anden mindst)
      returner+=sizes[1];
    }
    //  returner+=2;
  }
  returner+=30;
  return returner;
}

void pdfupdate(int h) {
  int w=230;
  a=cp1.getColorValue();
  b=cp2.getColorValue();
  c=cp3.getColorValue();
  if (save) {
    pdf = createGraphics(w, h, PDF, "output.pdf");
  }
  else {
    pdf = createGraphics(w, h);
  }
  int y=58;
  pdf.beginDraw();
  pdf.background(0);
  pdf.textFont(createFont("Arial-Black", 32), 32);

  if (save) {  
    pdf.textMode(SHAPE);
  }
  else {
    pdf.textMode(MODEL);
  }
  pdf.strokeWeight(3);
  pdf.fill(c);
  pdf.rect(-5, -5, 305, h+5);

  pdf.fill(b);
  pdf.stroke(a);
  pdf.rect(1, 1, 50, 50);
  pdf.rect(57, 1, w-59, 50);
  pdf.strokeWeight(1);

  pdf.rect(1, h-30, w-2, 50);
  pdf.strokeWeight(3);

  pdf.noFill();
  pdf.rect(1, 57, w-3, h-58);

  //________
  pdf.fill(a);

  pdf.textSize(37);
  pdf.text("LOPPEN", 59, 32);

  pdf.textSize(9.48);
  pdf.text("FORSALG: WWW.BILLETLUGEN.DK & FONA", 5, h-21);
  pdf.textSize(7.1);
  pdf.text("KONCERTEN STARTER EN TIME EFTER DØRENE ÅBNES!", 4, h-13);
   pdf.textSize(10.7);
  pdf.text("RET TIL ÆNDRINGER FORBEHOLDES", 5, h-3);
  //________
  pdf.strokeWeight(1);
  int x1=0;
  int y1=0;

  for (int i=0;i<lines.length;i++) {
    String tmp=lines[i];
    boolean line=false;
    int size=0;
    float x=4;
    int newSize=0;
    char last=' ';
    int tmpspace=0;
    if (tmp.indexOf("<<<<")!=-1) {//h1 (størst)
      tmpspace+=sizes[4];
    }
    else if (tmp.indexOf("<<<")!=-1) {//h2
      tmpspace+=sizes[3];
    }
    else if (tmp.indexOf("<<")!=-1) {//h3 (anden mindst)
      tmpspace+=sizes[2];
    }
    else if (tmp.indexOf("[")!=-1 || tmp.indexOf("]")!=-1) {//linje
      tmpspace+=(space*2)+2;
    }
    else if (tmp.indexOf("<")!=-1) {//h3 (anden mindst)
      tmpspace+=sizes[1];
    }
    y+=tmpspace;

    for (int k=0;k<tmp.length();k++) {
      char current=tmp.charAt(k);
      if (current!=last && last=='<') {
        size=newSize;
        newSize=0;
      }
      else if (current=='<') {
        newSize+=1;
        if (newSize>4) {
          newSize=4;
        }
      }
      else if (current=='[') {
        y1=y;
        line=true;
      }
      else if (current==']') {
        pdf.fill(b);
        pdf.rect(2, y1, w-5, y-y1);
      }
      pdf.textSize(sizes[size]);
      if (!line) {
        if (current!='<') {
          // pdf.text(""+current, x, y);
          x+=pdf.textWidth(current);
        }
      }
      last=current;
    }
  }

  //_______

  //_______
  y=60;
  pdf.fill(a);
  for (int i=0;i<lines.length;i++) {
    String tmp=lines[i];
    boolean line=false;
    int size=0;
    float x=4;
    int newSize=0;
    char last=' ';
    int tmpspace=0;
    if (tmp.indexOf("<<<<")!=-1) {//h1 (størst)
      tmpspace+=sizes[4];
    }
    else if (tmp.indexOf("<<<")!=-1) {//h2
      tmpspace+=sizes[3];
    }
    else if (tmp.indexOf("<<")!=-1) {//h3 (anden mindst)
      tmpspace+=sizes[2];
    }
    else if (tmp.indexOf("[")!=-1 || tmp.indexOf("]")!=-1) {//linje
      tmpspace+=(space*2)+2;
    }
    else if (tmp.indexOf("<")!=-1) {//h3 (anden mindst)
      tmpspace+=sizes[1];
    }
    y+=tmpspace;

    for (int k=0;k<tmp.length();k++) {
      char current=tmp.charAt(k);
      if (current!=last && last=='<') {
        size=newSize;
        newSize=0;
      }
      else if (current=='<') {
        newSize+=1;
        if (newSize>4) {
          newSize=4;
        }
      }
      else if (current=='@') {
        line=true;
      }
      pdf.textSize(sizes[size]);
      if (!line) {
        if (current!='<') {
          pdf.text(""+current, x, y);
          x+=pdf.textWidth(current);
        }
      }
      else {
        // pdf.line(0, y, 300, y);
      }
      last=current;
    }
  }
  pdf.noFill();

  if (save) {
    pdf.dispose();
    save=false;
    pdfupdate(calcPdfHeight(lines));
  }
  pdf.endDraw();
}

void mousePressed() {
  save=true;
}


public void handleTextEvents(GEditableTextControl textarea, GEvent event) {
  println("\n______"+millis()+"\n"+txaSample.getText());
  lines=split(txaSample.getText(), "\n");
  pdfupdate(calcPdfHeight(lines));
}
public void controlEvent(ControlEvent ce) {
  // when a value change from a ColorPicker is received, extract the ARGB values
  // from the controller's array value
  if (ce.isFrom(cp1)||ce.isFrom(cp2)||ce.isFrom(cp3)) {
  }
}

