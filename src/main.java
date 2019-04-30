import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

//finaaaaaal


public class main {

	private static PrintWriter subor;
	//linked listy obsahujuce fakty a pravidla (jednemu pravidlu prisluchaju 3 linkedlisty)
	private static LinkedList<String> fakty = new LinkedList<String>();
	private static LinkedList<String> podmienka = new LinkedList<String>();
	private static LinkedList<String> rozkaz = new LinkedList<String>();
	private static LinkedList<String> meno = new LinkedList<String>();
	static int pravidlo;
	static int uprava;
	static String X, Y, Z;
	
	//vytvory pole stringov ktore obsahuje podmienku s doplnenymi X,Y,Z
	public static String[] kontrolovatelna_podmienka(String vstup) {
		String arr[] = vstup.split(" ");
		for(int i = 0; i<arr.length;i++) {
			if("?X".equals(arr[i])) {
				if(!"".equals(X))arr[i] = X;
			}
			if("?Y".equals(arr[i])) {
				if(!"".equals(Y))arr[i] = Y;
			}
			if("?Z".equals(arr[i])) {
				if(!"".equals(Z))arr[i] = Z;
			}
		}
		return arr;
	}
	
	//porovna fakt s podmienkov. podmienka ktora sem vstupy bola predtym upravena pomocou funkcie kontrolovatelna_podmienka()
	//tato funkcia porovnava pole stringov po prvkoch.
	//vracia true ak su polia "rovnake", false vracia ak "rovnake" niesu
	public static boolean kontrola_fakt(String vstup, String porovnavane[]) {
		String arr[] = vstup.split(" ");
		if("<>".equals(porovnavane[0])) {
			if(!porovnavane[1].equals(porovnavane[2]))return true;
			else return false;
		}
		if(porovnavane.length != arr.length) {
			return false;
		}else {
			for(int i = 0; i<arr.length;i++) {
				if(!porovnavane[i].equals(arr[i])) {
					if("?X".equals(porovnavane[i])) {
						if("".equals(X))X = arr[i];
					}
					else if("?Y".equals(porovnavane[i])) {
						if("".equals(Y))Y = arr[i];
					}
					else if("?Z".equals(porovnavane[i])) {
						if("".equals(Z))Z = arr[i];
					}
					else return false;
				}
			}
			return true;
		}
	}
	
	//vytvori string pre akciu
	//doplni do neho X,Y,Z
	public static String vytvor_rozkazovy_string(String[] pomoc) {
		for(int i=0;i<pomoc.length;i++) {
			if(X!="") if("?X".equals(pomoc[i])){
				pomoc[i] = X;
			}
			if(Y!="") if("?Y".equals(pomoc[i])){
				pomoc[i] = Y;
			}
			if(Z!="") if("?Z".equals(pomoc[i])){
				pomoc[i] = Z;
			}
		}
		return String.join(" ", pomoc);
	}
	
	//funkcia vykonavajuca akcie
	// tato funkcia najskor zisti typ akcie, nasledne oddeli prve slovo a vykona akciu
	//na vykonanie akcie si nechava vytvorit string pomocou funkcie vytvor_rozkazovy_string()
	public static void vykonaj_rozkaz() {
		String pomocna[] = rozkaz.get(pravidlo).split(",");
		for(int j=0;j<pomocna.length;j++) {
			String arr[] = pomocna[j].split(" ");
			if("pridaj".equals(arr[0])) {
				String pomoc[] = (pomocna[j].substring(7)).split(" ");
				String str = vytvor_rozkazovy_string(pomoc);
				for(int i=0;i<fakty.size();i++) {
					if(str.equals(fakty.get(i)))return;
				}
				uprava = 1;
				fakty.add(str);
			}
			if("vymaz".equals(arr[0])) {
				String pomoc[] = (pomocna[j].substring(6)).split(" ");
				String str = vytvor_rozkazovy_string(pomoc);
				for(int i=0;i<fakty.size();i++) {
					if(str.equals(fakty.get(i)))fakty.remove(i);
				}
				uprava = 1;
			}
			if("sprava".equals(arr[0])) {
				System.out.println("\t"+vytvor_rozkazovy_string((pomocna[j].substring(7)).split(" ")));
			}
		}
		
	}
	
	//funkcia pomocou funkcii kontrolovatelna_podmienka() a kontrola_fakt skontroluje pravidlo ktore jej prislo
	// s kazdym faktom a nasledne vola sama seba pokial existuju este nejake podmienky v pravidle
	//pocet podmienok zistuje rozdelenim stringu pomocou ciarky. 
	public static void pravidla(String vstup) {
		String x=X, y=Y, z=Z;
		String podmienky[] = vstup.split(",");
		String kontrola[] = kontrolovatelna_podmienka(podmienky[0]);
		for(int i = 0; i < fakty.size(); i++) {
			if(kontrola_fakt(fakty.get(i), kontrola) == true) {
				if(podmienky.length != 1) {
					vstup = vstup.replace(podmienky[0] + ",", "");
					pravidla(vstup);
				}else {
					vykonaj_rozkaz();
				}
			}
			X=x;
			Y=y;
			Z=z;
		}
		return;
	  }
	
	//hlavna funkcia main ktora nacitava udaje zapisuje ich do linkedlistu a nasledne zavola dalsie funkcie
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
	    	//nacitanie pravidiel zo suboru a rozdelenie ich do linked listov
            File f = new File("pravidla.txt");
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readLine = "";
            int n = 0;
            while ((readLine = b.readLine()) != null) {
                if(n%4 == 0)meno.add(readLine);
                if(n%4 == 1)podmienka.add(readLine.substring(6));
                if(n%4 == 2)rozkaz.add(readLine.substring(6));
                n++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		try {
	    	//nacitanie faktov zo suboru a nacitanie do linked listu
            File f = new File("fakty.txt");
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readLine = "";
            while ((readLine = b.readLine()) != null) {
                fakty.add(readLine);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		//kontrolna podmienka na ukoncenie cyklu
		uprava = 1;
		System.out.println("Spravy:\n");
		//vyklus ktorý sa vykonava pokial existuje nejaka zmena pri prejdeni pravidiel
		while(uprava == 1) {
			uprava = 0;
			for(pravidlo=0;pravidlo<podmienka.size();pravidlo++) {
				X="";
				Y="";
				Z="";
				String pomoc = podmienka.get(pravidlo);
				pravidla(pomoc);
			}
		}
		System.out.println("------------------------\nNove Fakty:\n");
		for(int vypis = 0; vypis < fakty.size(); vypis++) System.out.println("\t"+fakty.get(vypis));
	}

}
