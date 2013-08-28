import g4p_controls.*;
import controlP5.*;
import processing.pdf.*;

ControlP5 cp5;
ColorPicker cp1, cp2, cp3;


color a, b, c;
color olda, oldb, oldc;

PGraphics pdf;
String[] lines= {
  "", ""
};
int datecounter=0;


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

  size(620, 800);
  //  frameRate(24);

  arial=loadFont("arial.vlw");

  lines = loadStrings("text.txt");

  //texarea
  txaSample = new GTextArea(this, 5, 5, 300, 500, G4P. SCROLLBARS_BOTH);
  txaSample.setText(PApplet.join(lines, '\n'), 250);
  //andet ui init
  cp5 = new ControlP5(this);

  //text labels
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

  //color pickers initialiseres til rød text/ramme gulalternate background og hvid background
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
  //generer pdf knap
  cp5.addBang("bang")
    .setPosition(5, 760)
      .setSize(30, 30)
        .setTriggerEvent(Bang.PRESSED)
          .setLabel("generate poster")
            ;
  //farve variable init til colorpickers værdier
  olda=a=cp1.getColorValue();
  oldb=b=cp2.getColorValue();
  oldc=c=cp3.getColorValue();
  //baggrund sættes
  background(100);
  //plakat opdateres
  pdfupdate(calcPdfHeight(lines));
}

void draw() {
  //baggrund sættes for gui område
  noStroke();
  fill(100);
  rect(0, 0, 310, height);
  //hvis der ikke er save mode (generer knap trykket ned) så bliver et billede af plakat vist og skaleret
  if (!save) {
    PImage img=pdf;
    if (img.height>height-10) {
      img.resize(0, height-20);
    }
    image(img, width-5-pdfwidth, 5);
  }
  //farve værdier opdateres
  a=cp1.getColorValue();
  b=cp2.getColorValue();
  c=cp3.getColorValue();
  //der tjekkes om der er ændret ved faver og plakat opdateres
  if (olda!=a || oldb!=b || oldc!=c) {
    pdfupdate(calcPdfHeight(lines));
    olda=a;
    oldb=b;
    oldc=c;
  }
}
//funktion der regner højden på plakaten ud
int calcPdfHeight(String [] lulz) {
  //datecounter regner ud hvor mange datorubrikker der er
  datecounter=0;
  //værdi for header højden
  int returner=58;
  //loop der går igennem alle linjer i tekst area
  for (int i=0;i<lulz.length;i++) {
    //nuværende tekstlinje
    String tmp=lines[i];
    //der tjekkes hvad den største skrifttype i nuværende linje er
    if (tmp.indexOf("<<<<")!=-1) {//h1 (størst)
      returner+=sizes[4];
    }
    else if (tmp.indexOf("<<<")!=-1) {//h2
      returner+=sizes[3];
    }
    else if (tmp.indexOf("<<")!=-1) {//h3 (anden mindst)
      returner+=sizes[2];
    }
    else if (tmp.indexOf("<")!=-1) {//h3 (anden mindst)
      returner+=sizes[1];
    }
    //der tjekkes om der er rubrikslut tegn i linjen
    else if (tmp.indexOf("]")!=-1) {
      //hvis ja så tælles der rubrikker og laves space immellem denne og næste rubrik
      datecounter++;
      returner+=5;
    }
  }
  //tilføjes plads til footer
  returner+=30;
  //returnerer total højde
  return returner;
}
//funktion der opdaterer og tegner plakat i framebuffer
void pdfupdate(int h) {
  //bredde på plakat er ikke sammenhængende med skriftstørrelser
  int w=230;
//flip til at vælge baggrundsfarve
  boolean alternatebackground=false;
  //hvis der er ulige antal rubrikker skal vi starte med hvid baggrund
  if (datecounter%2==0) {
    alternatebackground=true;
  }
  if (save) {
    pdf = createGraphics(w, h, PDF, "output.pdf");
  }
  else {
    pdf = createGraphics(w, h);
  }
  int y=58;
  boolean first=true;
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

  pdf.textSize(9.20);
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
    else if (tmp.indexOf("]")!=-1) {//linje
      tmpspace+=5;
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
        first=false;
        y1=y;
        line=true;
      }
      else if (current==']') {
        if (alternatebackground) {
          alternatebackground=false;

          pdf.fill(b);
        }
        else {
          alternatebackground=true;

          pdf.fill(c);
        }
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
    else if (tmp.indexOf("]")!=-1) {//linje
      tmpspace+=5;
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
  }
  pdf.endDraw();
}
public void bang() {
  background(100);
  save=true;
  pdfupdate(calcPdfHeight(lines));
  pdf.dispose();
  save=false;
  pdfupdate(calcPdfHeight(lines));
}

void mousePressed() {
}


public void handleTextEvents(GEditableTextControl textarea, GEvent event) {
  //println("\n______"+millis()+"\n"+txaSample.getText());
  String lol=txaSample.getText();
  lines=split(lol, "\n");
  background(100);
  pdfupdate(calcPdfHeight(lines));
}

public void controlEvent(ControlEvent ce) {
}

