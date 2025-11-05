package domHDK6NX1105;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class DomQuery1HDK6NX {

    public static void main(String[] args) {
        try {
            // 0. XML beolvasása
            // próbáljuk meg a feladat szerinti nevet, ha nem sikerül, próbáljuk a tiédet
            File xmlFile = new File("orarendHDK6NX.xml");
            if (!xmlFile.exists()) {
                xmlFile = new File("orarnedHDK6NX.xml"); // a te fájlneved
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("Gyökér elem: " + doc.getDocumentElement().getNodeName());

            // minden <ora> elem
            NodeList oraLista = doc.getElementsByTagName("ora");

            /* =========================================================
               1.) Kérdezze le a kurzusok nevét egy listába, majd írja ki
               ========================================================= */
            System.out.println("\n1.) Kurzusok nevei:");
            // itt egyszerűen kiírjuk őket egymás után
            for (int i = 0; i < oraLista.getLength(); i++) {
                Node n = oraLista.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element oraElem = (Element) n;
                    Element targyElem = (Element) oraElem.getElementsByTagName("targy").item(0);
                    String kurzusNev = targyElem.getAttribute("nev");
                    System.out.println("  - " + kurzusNev);
                }
            }

            /* =========================================================
               2.) Az első példány kiírása struktúráltan + fájlba
               ========================================================= */
            System.out.println("\n2.) Az első óra struktúrált adatai:");

            if (oraLista.getLength() > 0) {
                Element elsoOra = (Element) oraLista.item(0);

                String id = elsoOra.getAttribute("id");
                String tipus = elsoOra.getAttribute("tipus");

                Element targyElem = (Element) elsoOra.getElementsByTagName("targy").item(0);
                String targyNev = targyElem.getAttribute("nev");

                Element idopontElem = (Element) elsoOra.getElementsByTagName("idopont").item(0);
                Element napElem = (Element) idopontElem.getElementsByTagName("nap").item(0);
                Element tolElem = (Element) idopontElem.getElementsByTagName("tol").item(0);
                Element igElem = (Element) idopontElem.getElementsByTagName("ig").item(0);

                String nap = napElem.getAttribute("napn");
                String tol = tolElem.getAttribute("tolt");
                String ig = igElem.getAttribute("igt");

                Element helyElem = (Element) elsoOra.getElementsByTagName("helyszin").item(0);
                String hely = helyElem.getAttribute("hol");

                Element oktatoElem = (Element) elsoOra.getElementsByTagName("oktato").item(0);
                String oktatoNev = oktatoElem.getAttribute("neve");

                Element szakElem = (Element) elsoOra.getElementsByTagName("szak").item(0);
                String szakNev = szakElem.getAttribute("sznev");

                // konzolra
                System.out.println("ID: " + id);
                System.out.println("Típus: " + tipus);
                System.out.println("Tárgy: " + targyNev);
                System.out.println("Nap: " + nap);
                System.out.println("Időtartam: " + tol + " - " + ig);
                System.out.println("Helyszín: " + hely);
                System.out.println("Oktató: " + oktatoNev);
                System.out.println("Szak: " + szakNev);

                // fájlba mentés
                File outFile = new File("orarendQuery1Neptunkod.txt");
                try (PrintWriter pw = new PrintWriter(outFile)) {
                    pw.println("ID: " + id);
                    pw.println("Típus: " + tipus);
                    pw.println("Tárgy: " + targyNev);
                    pw.println("Nap: " + nap);
                    pw.println("Időtartam: " + tol + " - " + ig);
                    pw.println("Helyszín: " + hely);
                    pw.println("Oktató: " + oktatoNev);
                    pw.println("Szak: " + szakNev);
                }
                System.out.println("-> Struktúrált adatok elmentve: " + outFile.getAbsolutePath());
            }

            /* =========================================================
               3.) Kérdezze le a kurzusokat oktatók neveit listába
               ========================================================= */
            System.out.println("\n3.) Oktatók nevei:");
            Set<String> oktatok = new HashSet<>();
            for (int i = 0; i < oraLista.getLength(); i++) {
                Node n = oraLista.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element oraElem = (Element) n;
                    Element oktatoElem = (Element) oraElem.getElementsByTagName("oktato").item(0);
                    String oktatoNev = oktatoElem.getAttribute("neve").trim();
                    oktatok.add(oktatoNev);
                }
            }
            for (String o : oktatok) {
                System.out.println("  - " + o);
            }

            /* =========================================================
               4.) Összetett lekérdezés
               Pl.: listázzuk ki az összes KEDDI órát (tárgy, idő, helyszín)
               ========================================================= */
            System.out.println("\n4.) Összetett lekérdezés – minden keddi óra:");
            for (int i = 0; i < oraLista.getLength(); i++) {
                Node n = oraLista.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element oraElem = (Element) n;

                    Element idopontElem = (Element) oraElem.getElementsByTagName("idopont").item(0);
                    Element napElem = (Element) idopontElem.getElementsByTagName("nap").item(0);
                    String nap = napElem.getAttribute("napn").toLowerCase();

                    if (nap.contains("kedd")) {
                        Element targyElem = (Element) oraElem.getElementsByTagName("targy").item(0);
                        String targyNev = targyElem.getAttribute("nev");

                        Element tolElem = (Element) idopontElem.getElementsByTagName("tol").item(0);
                        Element igElem = (Element) idopontElem.getElementsByTagName("ig").item(0);
                        String tol = tolElem.getAttribute("tolt");
                        String ig = igElem.getAttribute("igt");

                        Element helyElem = (Element) oraElem.getElementsByTagName("helyszin").item(0);
                        String hely = helyElem.getAttribute("hol");

                        System.out.println("  - " + targyNev + " (" + tol + "-" + ig + ") @ " + hely);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
