import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import g4p_controls.*; 
import controlP5.*; 
import processing.pdf.*; 
import sojamo.drop.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class loppenplakat extends PApplet {






SDrop drop;
MyDropListener m;

ControlP5 cp5;
ColorPicker cp1, cp2, cp3;


int a, b, c;
int olda, oldb, oldc;

boolean locked=false;

PGraphics pdf;
String[] lines= {
  "", ""
};
PShape loppen;

boolean loading=false;



int datecounter=0;


PFont arial, loppen2, christiania;

GTextArea txaSample;
Textlabel myTextlabelB;
Textlabel myConsole;

/*
int sizes[] = {
 1, 8, 10, 14, 19
 };
 */
int sizes[] = {
  1, 6, 8, 10, 12, 14, 16, 18, 19, 21
};

int space=2;

boolean save=false;

int pdfwidth=300;
public void setup() {
  loppen = loadShape("loppen.svg");


  size(930, 510);

  drop = new SDrop(this);
  m = new MyDropListener();
  drop.addDropListener(m);

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
    .setText("TEXT OG RAMMER:")
      .setPosition(305, 10)
        .setColor(0XFFFFFFFF)
          .setFont(createFont("Georgia", 15))
            ;

  myTextlabelB = cp5.addTextlabel("label2")
    .setText("RUBRIK FARVE:")
      .setPosition(310, 100)
        .setColorValue(0XFFFFFFFF)
          .setFont(createFont("Georgia", 15))
            ;

  myTextlabelB = cp5.addTextlabel("label3")
    .setText("BAGGRUND:")
      .setPosition(310, 190)
        .setColorValue(0XFFFFFFFF)
          .setFont(createFont("Georgia", 15))
            ;

  //color pickers initialiseres til r\u00f8d text/ramme gulalternate background og hvid background
  cp1 = cp5.addColorPicker("picker1")
    .setPosition(310, 30)
      .setColorValue(color(255, 0, 0, 255))
        ;

  cp2 = cp5.addColorPicker("picker2")
    .setPosition(310, 120)
      .setColorValue(color(255, 221, 0, 255))
        ;

  cp3 = cp5.addColorPicker("picker3")
    .setPosition(310, 210)
      .setColorValue(color(255, 255, 255, 255))
        ;
  //generer pdf knap
  cp5.addBang("bang")
    .setPosition(530, 280)
      .setSize(30, 30)
        .setTriggerEvent(Bang.PRESSED)
          .setLabel("GENERER PLAKAT")
            ;
  cp5.addTextfield("plakatnavn")
    .setPosition(310, 280)
      .setSize(210, 30)
        .setFont(createFont("arial", 20))
          .setAutoClear(false)
            ;

  //generer pdf knap
  cp5.addBang("gem")
    .setPosition(530, 340)
      .setSize(30, 30)
        .setTriggerEvent(Bang.PRESSED)
          .setLabel("GEM TEKST")
            ;

  cp5.addTextfield("FILNAVN")
    .setPosition(310, 340)
      .setSize(210, 30)
        .setFont(createFont("arial", 20))
          .setAutoClear(false)
            ;

  myConsole = cp5.addTextlabel("console")
    .setText("Message:")
      .setPosition(305, 385)
        .setColorValue(0XFFFFFFFF)
          .setFont(createFont("Georgia", 10))
            ;
  //farve variable init til colorpickers v\u00e6rdier
  olda=a=cp1.getColorValue();
  oldb=b=cp2.getColorValue();
  oldc=c=cp3.getColorValue();
  //baggrund s\u00e6ttes
  background(100);
  //plakat opdateres
  pdfupdate(calcPdfHeight(lines));
}

public void draw() {
  //baggrund s\u00e6ttes for gui omr\u00e5de
  noStroke();
  fill(100);
  rect(0, 0, 620, height);
  //hvis der ikke er save mode (generer knap trykket ned) s\u00e5 bliver et billede af plakat vist og skaleret
  if (!save && !loading) {
    PImage img=pdf;
    if (img.height>height-10) {
      img.resize(0, height-20);
    }
    image(img, width-5-pdfwidth, 5);
  }
  //farve v\u00e6rdier opdateres
  a=cp1.getColorValue();
  b=cp2.getColorValue();
  c=cp3.getColorValue();
  //der tjekkes om der er \u00e6ndret ved faver og plakat opdateres
  if (olda!=a || oldb!=b || oldc!=c) {
    pdfupdate(calcPdfHeight(lines));
    olda=a;
    oldb=b;
    oldc=c;
  }
  m.draw();
}
//funktion der regner h\u00f8jden p\u00e5 plakaten ud
public int calcPdfHeight(String [] lulz) {
  //datecounter regner ud hvor mange datorubrikker der er
  datecounter=0;
  //v\u00e6rdi for header h\u00f8jden
  int returner=58;
  //loop der g\u00e5r igennem alle linjer i tekst area
  for (int i=0;i<lulz.length;i++) {
    //nuv\u00e6rende tekstlinje
    String tmp=lines[i];
    //der tjekkes hvad den st\u00f8rste skrifttype i nuv\u00e6rende linje er
    if (tmp.indexOf("<<<<<<<<<")!=-1) {//h1 (st\u00f8rst)
      returner+=sizes[9];
    }
    else if (tmp.indexOf("<<<<<<<<")!=-1) {//h1 (st\u00f8rst)
      returner+=sizes[8];
    }
    else if (tmp.indexOf("<<<<<<<")!=-1) {//h1 (st\u00f8rst)
      returner+=sizes[7];
    }
    else if (tmp.indexOf("<<<<<<")!=-1) {//h1 (st\u00f8rst)
      returner+=sizes[6];
    }
    else if (tmp.indexOf("<<<<<")!=-1) {//h1 (st\u00f8rst)
      returner+=sizes[5];
    }
    else if (tmp.indexOf("<<<<")!=-1) {//h1 (st\u00f8rst)
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
      //hvis ja s\u00e5 t\u00e6lles der rubrikker og laves space immellem denne og n\u00e6ste rubrik
      datecounter++;
      returner+=5;
    }
  }
  //tilf\u00f8jes plads til footer
  returner+=30;
  //returnerer total h\u00f8jde
  return returner;
}
//funktion der opdaterer og tegner plakat i framebuffer
public void pdfupdate(int h) {
  locked=true;
  //bredde p\u00e5 plakat er ikke sammenh\u00e6ngende med skriftst\u00f8rrelser
  int w=230;
  //flip til at v\u00e6lge baggrundsfarve
  boolean alternatebackground=false;
  //hvis der er ulige antal rubrikker skal vi starte med hvid baggrund
  if (datecounter%2==0) {
    alternatebackground=true;
  }
  //hvis save mode skal det v\u00e6re en pdf
  if (save) {
    pdf = createGraphics(w, h, PDF, sketchPath+"/gemte/"+cp5.get(Textfield.class, "plakatnavn").getText()+".pdf");
  }
  //hvis ikke save mode skal det v\u00e6re en bitmap til visning i gui
  else {
    pdf = createGraphics(w, h);
  }
  //sted hvor header er f\u00e6rdig
  int y=58;
  //start framebuffer object tegning af plakat
  pdf.beginDraw();
  //s\u00e6t baggrund
  pdf.background(0);
  //lav skrifttype ARIAL black
  pdf.textFont(createFont("Arial-Black", 32), 32);
  //hvis save er teskten en vector ellers en bitmap
  if (save) {  

    pdf.textMode(SHAPE);
  }
  else {
    pdf.textMode(MODEL);
  }
  //s\u00e6t tykkelsen p\u00e5 rammer
  pdf.strokeWeight(3);
  //s\u00e6t farven p\u00e5 baggrund
  pdf.fill(c);
  //tegn baggrund
  pdf.rect(-5, -5, 305, h+5);
  //s\u00e6t baggrund til to header rammer
  pdf.fill(b);
  //s\u00e6t ramme farve til to header rammer og generel ramme
  pdf.stroke(a);
  //tegn lille firkant til loppe logo
  pdf.rect(1, 1, 50, 50);
  //tegn loppe ramme
  pdf.rect(57, 1, w-59, 50);
  //s\u00e6t ramme tykkelse til footer
  pdf.strokeWeight(1);
  //tegn footer ramme
  pdf.rect(1, h-30, w-2, 50);
  //s\u00e6t ramme tykkelse til stor ramme
  pdf.strokeWeight(3);
  //ingen baggrund
  pdf.noFill();
  //stor program ramme
  pdf.rect(1, 57, w-3, h-58);

  //________
  //s\u00e6t fill til tekst i header
  pdf.fill(a);
  //s\u00e6t tekstst\u00f8rrelse til loppen
  pdf.textSize(36);
  pdf.textAlign(CENTER);
  //skriv loppen
  pdf.text("LOPPEN", 142, 33);
    pdf.textAlign(LEFT);


  //s\u00e6t tekst st\u00f8rrelse til footer og skriv footer
  // [
  pdf.textSize(9.20f);
  pdf.text("FORSALG: WWW.BILLETLUGEN.DK & FONA", 5, h-21);
  pdf.textSize(7.1f);
  pdf.text("KONCERTEN STARTER EN TIME EFTER D\u00d8RENE \u00c5BNES!", 4, h-13);
  pdf.textSize(10.7f);
  pdf.text("RET TIL \u00c6NDRINGER FORBEHOLDES", 5, h-3);
  // ]

  //s\u00e6t ramme tykkelse til rubrikker 

  pdf.strokeWeight(1);
  //s\u00e6t start y punkt
  int y1=0;
  //loop igennem alle linjer i tekst
  for (int i=0;i<lines.length;i++) {
    //fetch nuv\u00e6rende linje
    String tmp=lines[i];
    //hvis det bare er en rubrikslinje
    boolean line=false;
    //s\u00e6t st\u00f8rrelse p\u00e5 linje stykke (maks h\u00f8jde)
    int size=0;
    //indryk til tekst og variable til hvor p\u00e5 linjen du er
    float x=4;

    int newSize=0;
    //variable til at tjekke om du er f\u00e6rdig med at indstille tekst st\u00f8rrelse
    char last=' ';
    //tmp linje h\u00f8jde
    int tmpspace=0;
    if (tmp.indexOf("<<<<<<<<<")!=-1) {//h1 (st\u00f8rst)
      tmpspace+=sizes[9];
    }
    else if (tmp.indexOf("<<<<<<<<")!=-1) {//h1 (st\u00f8rst)
      tmpspace+=sizes[8];
    }
    else if (tmp.indexOf("<<<<<<<")!=-1) {//h1 (st\u00f8rst)
      tmpspace+=sizes[7];
    }
    else if (tmp.indexOf("<<<<<<")!=-1) {//h1 (st\u00f8rst)
      tmpspace+=sizes[6];
    }
    else if (tmp.indexOf("<<<<<")!=-1) {//h1 (st\u00f8rst)
      tmpspace+=sizes[5];
    }
    else if (tmp.indexOf("<<<<")!=-1) {//h1 (st\u00f8rst)
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
    //tmp linjeh\u00f8jde ligges til y position
    y+=tmpspace;
    //looper igennem alle tegn p\u00e5 linje
    for (int k=0;k<tmp.length();k++) {
      //nuv\u00e6rende tegn
      char current=tmp.charAt(k);
      //hvis ikke nuv\u00e6rende tegn er lig med sidste tegn og sidste tegn var < betyder det at skriftst\u00f8rrelse er \u00e6ndret
      if (current!=last && last=='<') {
        size=newSize;
        newSize=0;
      }
      //hvis nuv\u00e6rende tegn er skrift st\u00f8rrelse \u00e6ndring, \u00e6ndres skriftst\u00f8rrelse
      else if (current=='<') {
        newSize+=1;
        if (newSize>4) {
          newSize=4;
        }
      }
      //hvis der er rubrik tegn gemmes y position til senere rectangle skabelse
      else if (current=='[') {
        y1=y;
        line=true;
      }
      //hvis nuv\u00e6rende er rubrikslut tegn skabes der en rectangle
      else if (current==']') {
        //check om der skal v\u00e6re alternate baggrundsfarve
        if (alternatebackground) {
          //flip boolean
          alternatebackground=false;
          //ny fill farve
          pdf.fill(b);
        }
        else {
          alternatebackground=true;

          pdf.fill(c);
        }
        //tegn rectangle til rubrik
        pdf.rect(2, y1, w-5, y-y1);
      }
      //gem nuv\u00e6rende tegn i variabel
      last=current;
    }
  }
  //nu skal der laves tekst, vi starter ved denne y pos
  y=60;
  //s\u00e6t fill til tekst farve
  pdf.fill(a);
  //loop igennem alle linjer
  for (int i=0;i<lines.length;i++) {
    //gem linje i var
    String tmp=lines[i];
    //er vi ved rubrik tegn
    boolean line=false;
    //for f\u00f8lgende ser ovenfor loops
    // [
    int size=0;
    float x=4;
    int newSize=0;
    char last=' ';
    int tmpspace=0;
    if (tmp.indexOf("<<<<<<<<<")!=-1) {//h1 (st\u00f8rst)
      tmpspace+=sizes[9];
    }
    else if (tmp.indexOf("<<<<<<<<")!=-1) {//h1 (st\u00f8rst)
      tmpspace+=sizes[8];
    }
    else if (tmp.indexOf("<<<<<<<")!=-1) {//h1 (st\u00f8rst)
      tmpspace+=sizes[7];
    }
    else if (tmp.indexOf("<<<<<<")!=-1) {//h1 (st\u00f8rst)
      tmpspace+=sizes[6];
    }
    else if (tmp.indexOf("<<<<<")!=-1) {//h1 (st\u00f8rst)
      tmpspace+=sizes[5];
    }
    else if (tmp.indexOf("<<<<")!=-1) {//h1 (st\u00f8rst)
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
        if (newSize>9) {
          newSize=4;
        }
      }
      else if (current=='[' || current==']') {
        line=true;
      }
      pdf.textSize(sizes[size]);
      // ]
      //hvis ikke rubrik tegn s\u00e5 skriv tegn
      if (!line) {

        if (current!='<') {
          if (current=='@') {
            String tmpstr= tmp.substring(k+1);
            pdf.textAlign(RIGHT);
            pdf.text(tmpstr, w-5, y);
            pdf.textAlign(LEFT);
            break;
          }
          else {
            pdf.text(""+current, x, y);
            x+=pdf.textWidth(current);
          }
        }
      }
      else {
        // pdf.line(0, y, 300, y);
      }
      last=current;
    }
  }
  //if (save) {  
  loppen.disableStyle();
  fill(a);
  pdf.shape(loppen, 9, 5, 36, 41);            // Draw at coordinate (280, 40) at the default size
  //}
  //for en sikkerheds skyld init fill
  pdf.noFill();
  pdf.endDraw();
  locked=false;
}
//generer plakat knappen
public void bang() {
  background(100);
  save=true;
  pdfupdate(calcPdfHeight(lines));
  //gem pdf
  pdf.dispose();
  String filnavn =sketchPath+"/gemte/"+cp5.get(Textfield.class, "plakatnavn").getText()+".pdf";
  myConsole.setText("Saved: "+filnavn);
  //bryd ud af savemode
  save=false;
  //husk at generer igen uden savemode s\u00e5 vi kan vise bitmap :-)
  pdfupdate(calcPdfHeight(lines));
}
public void gem() {
  String colorString="color:"+hex(a)+","+hex(b)+","+hex(c);
  String filnavn=sketchPath+"/gemte/"+cp5.get(Textfield.class, "FILNAVN").getText()+".txt";
  saveStrings(filnavn, splice(lines,colorString,0));
  myConsole.setText("Saved: "+filnavn);
}
//hvis tekst \u00e6ndres s\u00e5 opdateres plakat ogs\u00e5
public void handleTextEvents(GEditableTextControl textarea, GEvent event) {
  //println("\n______"+millis()+"\n"+txaSample.getText());
  //f\u00e5 tekst fra textarea
  String lol=txaSample.getText();
  //lav tekst til et string array
  lines=split(lol, "\n");
  //s\u00e6t baggrund bag plakat 
  background(100);
  pdfupdate(calcPdfHeight(lines));
}

// a custom DropListener class.
class MyDropListener extends DropListener {

  int myColor;

  MyDropListener() {
    myColor = color(255);
    // set a target rect for drop event.
    setTargetRect(310, 400, 260, 100);
  }

  public void draw() {
    fill(myColor);
    rect(310, 400, 260, 100);
    fill(0);
    text("drop text file here", 390, 450);
  }

  // if a dragged object enters the target area.
  // dropEnter is called.
  public void dropEnter() {
    myColor = color(255, 0, 0);
  }

  // if a dragged object leaves the target area.
  // dropLeave is called.
  public void dropLeave() {
    myColor = color(255);
  }

  public void dropEvent(DropEvent theEvent) {
    if(!locked){
    println("isFile()\t"+theEvent.isFile());
    String tmp=theEvent.file().toString();
    if (tmp.substring(tmp.length()-4).equals(".txt")) {
      loading=true;
      lines = loadStrings(tmp);
      String [] m=match(lines[0],"color:");
      if(m!=null){
        String indexLine=lines[0].substring(6);
        println(indexLine);
        String [] colors=split(indexLine,",");
        if(colors.length==3){
         cp1.setColorValue(unhex(colors[0]));
         cp2.setColorValue(unhex(colors[1]));
         cp3.setColorValue(unhex(colors[2]));
        }
        lines=subset(lines,1);
      }
      txaSample.setText(PApplet.join(lines, '\n'), 250);
      background(100);
      pdfupdate(calcPdfHeight(lines));
      loading=false;
    }

    println("Dropped on MyDropListener");
  }  else{
        println("fail");

  }
  }

}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "loppenplakat" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
