public class PICS {  
    /* NW 22.2.2005 / 13.8.2014    Scanner for PIC compiler*/
  //IMPORT Texts, Oberon;

    final static int IdLen* = 32;
    final static int NofKeys = 25;
    /*symbols*/
    final static int Null = 0;
    final static int ast = 1;
    final static int slash = 2;
    final static int plus = 3;
    final static int minus = 4;
    final static int not = 5;
    final static int and = 6;
    final static int or = 7;
    final static int eql = 10;
    final static int neq = 11;
    final static int geq = 12;
    final static int lss = 13;
    final static int leq = 14;
    final static int gtr = 15;
    final static int period = 16;
    final static int comma = 17;
    final static int colon = 18;
    final static int op = 20;
    final static int query = 21;
    final static int lparen = 22;
    final static int becomes = 23;
    final static int ident = 24;
    final static int If = 25;
    final static int While = 26;
    final static int repeat = 27;
    final static int inc = 28;
    final static int dec = 29;
    final static int rol = 30;
    final static int ror = 31;
    final static int number = 32;
    final static int rparen = 33;
    final static int then = 34;
    final static int Do =  35;
    final static int semicolon = 36;
    final static int end = 37;
    final static int else = 38 ;
    final static int elsif =  39;
    final static int until =  40;
    final static int Return = 41;
    final static int Int = 42;
    final static int set = 43;
    final static int bool = 44;
    final static int Const = 50;
    final static int begin = 51;
    final static int proced = 52;
    final static int module = 53;
    final static int eof = 54;

  int val, typ;
   char[IdLen] id;

    char ch;  /*lookahead*/
    int K;
    Texts.Reader R;
    Texts.Writer W;
    char[NofKeys][16] key;
    int[NofKeys] symno;

  public long position(){
    return Texts.Pos(R)
  }

  public void Ident(int sym){
    int i, j, m;
   i = 0;
    do{
      IF i < IdLen-1 THEN id[i] := ch; INC(i) END ;
      Texts.Read(R, ch)
    }while( (ch < "0") || (ch > "9") && (ch < "A") || (ch > "Z") && (ch < "a") || (ch > "z"));
    id[i] := 0X;
    i := 0; j := NofKeys; (*search for keyword*)
    WHILE i < j DO
      m := (i + j) DIV 2;
      IF key[m] < id THEN i := m+1 ELSE j := m END
    END ;
    IF key[j] = id THEN sym := symno[i] ELSE sym := ident END
  }

  PROCEDURE Number;
  BEGIN val := 0; typ := 1;
    REPEAT val := 10 * val + ORD(ch) - ORD("0"); Texts.Read(R, ch)
    UNTIL (ch < "0") OR (ch > "9")
  END Number;

  PROCEDURE GetDigit(): INTEGER;
    VAR d: INTEGER;
  BEGIN
    IF ("0" <= ch) & (ch <= "9") THEN d := ORD(ch) - 30H
    ELSIF ("A" <= ch) & (ch <= "F") THEN d := ORD(ch) - 37H
    ELSE d := 0
    END ;
    Texts.Read(R, ch); RETURN d
  END GetDigit;

  PROCEDURE Hex;
    VAR d1, d0: INTEGER;
  BEGIN val := GetDigit()*10H + GetDigit(); typ := 2
  END Hex;

  PROCEDURE Get*(VAR sym: INTEGER);
  BEGIN
    WHILE (ch <= " ") OR (ch = "{") DO
      IF ch = "{" THEN
        REPEAT Texts.Read(R, ch) UNTIL (ch = "}") OR R.eot
      END ;
      Texts.Read(R, ch)
    END ;
    REPEAT
      WHILE ~R.eot & (ch <= " ") DO Texts.Read(R, ch) END;
      IF ch < "A" THEN
        IF ch < "0" THEN
          IF ch = "!" THEN Texts.Read(R, ch); sym := op
          ELSIF ch = "#" THEN Texts.Read(R, ch); sym := neq
          ELSIF ch = "$" THEN Texts.Read(R, ch); Hex; sym := number; typ := 2
          ELSIF ch = "&" THEN Texts.Read(R, ch); sym := and
          ELSIF ch = "(" THEN Texts.Read(R, ch); sym := lparen
          ELSIF ch = ")" THEN Texts.Read(R, ch); sym := rparen
          ELSIF ch = "*" THEN Texts.Read(R, ch); sym := ast
          ELSIF ch = "+" THEN Texts.Read(R, ch); sym := plus
          ELSIF ch = "," THEN Texts.Read(R, ch); sym := comma
          ELSIF ch = "-" THEN Texts.Read(R, ch); sym := minus
          ELSIF ch = "." THEN Texts.Read(R, ch); sym := period
          ELSIF ch = "/" THEN Texts.Read(R, ch); sym := slash
          ELSE Texts.Read(R, ch); (* " %  ' *) sym := null
          END
        ELSIF ch <= "9" THEN Number; sym := number
        ELSIF ch = ":" THEN Texts.Read(R, ch);
          IF ch = "=" THEN Texts.Read(R, ch); sym := becomes ELSE sym := colon END
        ELSIF ch = ";" THEN Texts.Read(R, ch); sym := semicolon
        ELSIF ch = "<" THEN  Texts.Read(R, ch);
          IF ch = "=" THEN Texts.Read(R, ch); sym := leq ELSE sym := lss END
        ELSIF ch = "=" THEN Texts.Read(R, ch); sym := eql
        ELSIF ch = ">" THEN Texts.Read(R, ch);
          IF ch = "=" THEN Texts.Read(R, ch); sym := geq ELSE sym := gtr END
        ELSIF ch = "?" THEN Texts.Read(R, ch); sym := query
        ELSE (* @ *) Texts.Read(R, ch); sym := null
        END
      ELSIF  ch < "a" THEN
        IF ch <= "Z" THEN Ident(sym)
        ELSE (* [ \ ] ^ _ `*) Texts.Read(R, ch); sym := null
        END
      ELSIF ch <= "z" THEN Ident(sym)
      ELSIF ch = "~" THEN Texts.Read(R, ch); sym := not
      ELSE (* { | } *) Texts.Read(R, ch); sym := null
      END
    UNTIL sym # null
  END Get;

  PROCEDURE Init*(T: Texts.Text; pos: LONGINT);
  BEGIN Texts.OpenReader(R, T, pos); Texts.Read(R, ch)
  END Init;

  PROCEDURE Enter(word: ARRAY OF CHAR; val: INTEGER);
  BEGIN key[K] := word; symno[K] := val; INC(K)
  END Enter;

BEGIN Texts.OpenWriter(W); K := 0;
  Enter("BEGIN", begin);
  Enter("BOOL", bool);
  Enter("CONST", const);
  Enter("DEC", dec);
  Enter("DO", do);
  Enter("ELSE", else);
  Enter("ELSIF", elsif);
  Enter("END", end);
  Enter("IF", if);
  Enter("INC", inc);
  Enter("INT", int);
  Enter("MODULE", module);
  Enter("OR", or);
  Enter("PROCEDURE", proced);
  Enter("REPEAT", repeat);
  Enter("RETURN", return);
  Enter("ROL", rol);
  Enter("ROR", ror);
  Enter("SET", set);
  Enter("THEN", then);
  Enter("UNTIL", until);
  Enter("WHILE", while);
  key[K] := "~ "
}//END PICS.
