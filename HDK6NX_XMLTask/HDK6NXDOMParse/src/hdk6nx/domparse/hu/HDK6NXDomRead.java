package hdk6nx.domparse.hu;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class HDK6NXDomRead
{

    public static void main(String[] args)
    {
        try
        {
            // 1. XML fájl elérési útja – NYUGODTAN ÍRD ÁT a sajátodra
            String xmlFile = "HDK6NX_XML.xml";

            // 2. DOM builder létrehozása
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 3. XML beolvasása Document-be
            Document doc = builder.parse(new File(xmlFile));

            // 4. whitespace-ek normalizálása
            doc.getDocumentElement().normalize();

            System.out.println("Gyökérelem: " + doc.getDocumentElement().getNodeName());
            System.out.println("=========================================");

            // 5. összes <ugyfel> elem kigyűjtése
            NodeList ugyfelLista = doc.getElementsByTagName("ugyfel");

            for (int i = 0; i < ugyfelLista.getLength(); i++)
            {
                Node ugyfelNode = ugyfelLista.item(i);

                if (ugyfelNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element ugyfelElem = (Element) ugyfelNode;

                    // ügyfél attribútum
                    String ugyfelID = ugyfelElem.getAttribute("ugyfelID");
                    System.out.println("ÜGYFÉL ID: " + ugyfelID);

                    // sima gyermek-elemek
                    String nev = getTextContent(ugyfelElem, "nev");
                    String email = getTextContent(ugyfelElem, "email");
                    String telefon = getTextContent(ugyfelElem, "telefon");

                    System.out.println("  Név: " + nev);
                    System.out.println("  Email: " + email);
                    System.out.println("  Telefon: " + telefon);

                    // cím kiolvasása (al-elemek)
                    Element cimElem = (Element) ugyfelElem.getElementsByTagName("cim").item(0);
                    if (cimElem != null)
                    {
                        String varos = getTextContent(cimElem, "cimVaros");
                        String utca = getTextContent(cimElem, "cimUtca");
                        String irsz = getTextContent(cimElem, "cimIrsz");
                        System.out.println("  Cím: " + varos + ", " + utca + " (" + irsz + ")");
                    }

                    // 6. ügyfél eszközei
                    NodeList eszkozLista = ugyfelElem.getElementsByTagName("eszkoz");
                    for (int j = 0; j < eszkozLista.getLength(); j++)
                    {
                        Element eszkozElem = (Element) eszkozLista.item(j);

                        String eszkozID = eszkozElem.getAttribute("eszkozID");
                        String tipus = getTextContent(eszkozElem, "tipus");
                        String marka = getTextContent(eszkozElem, "marka");
                        String modell = getTextContent(eszkozElem, "modell");
                        String azonosito = getTextContent(eszkozElem, "azonosito");
                        String garancialis = getTextContent(eszkozElem, "garancialis");

                        System.out.println("    ESZKÖZ ID: " + eszkozID);
                        System.out.println("      Típus: " + tipus);
                        System.out.println("      Márka: " + marka);
                        System.out.println("      Modell: " + modell);
                        System.out.println("      Azonosító: " + azonosito);
                        System.out.println("      Garanciális: " + garancialis);

                        // 7. eszköz megrendelései
                        NodeList megrendelesLista = eszkozElem.getElementsByTagName("megrendeles");
                        for (int k = 0; k < megrendelesLista.getLength(); k++)
                        {
                            Element megrendelesElem = (Element) megrendelesLista.item(k);

                            String megrID = megrendelesElem.getAttribute("megrendelesID");
                            String hibaLeiras = getTextContent(megrendelesElem, "hibaLeiras");
                            String allapot = getTextContent(megrendelesElem, "allapot");
                            String beerkDatum = getTextContent(megrendelesElem, "beerkDatum");
                            String fizetendo = getTextContent(megrendelesElem, "fizetendoOsszeg");

                            System.out.println("      MEGRENDELÉS ID: " + megrID);
                            System.out.println("        Hiba leírás: " + hibaLeiras);
                            System.out.println("        Állapot: " + allapot);
                            System.out.println("        Beérkezés dátuma: " + beerkDatum);
                            System.out.println("        Fizetendő összeg: " + fizetendo);

                            // 8. szerelés(ek)
                            NodeList szerelesLista = megrendelesElem.getElementsByTagName("szereles");
                            for (int s = 0; s < szerelesLista.getLength(); s++)
                            {
                                Element szerelesElem = (Element) szerelesLista.item(s);

                                String munkaOra = getTextContent(szerelesElem, "munkaOra");
                                String munkaDatum = getTextContent(szerelesElem, "munkaDatum");
                                String szerep = getTextContent(szerelesElem, "szerep");

                                System.out.println("        SZERELÉS:");
                                System.out.println("          Munkaóra: " + munkaOra);
                                System.out.println("          Munkadátum: " + munkaDatum);
                                System.out.println("          Szerep: " + szerep);

                                // technikus az szerelésen belül
                                Element technikusElem = (Element) szerelesElem.getElementsByTagName("technikus").item(0);
                                if (technikusElem != null)
                                {
                                    String techID = technikusElem.getAttribute("technikusID");
                                    String techNev = getTextContent(technikusElem, "nev");
                                    String techSzak = getTextContent(technikusElem, "szakTerulet");
                                    String techTel = getTextContent(technikusElem, "telefon");
                                    String techEmail = getTextContent(technikusElem, "email");

                                    System.out.println("          TECHNIKUS ID: " + techID);
                                    System.out.println("            Név: " + techNev);
                                    System.out.println("            Szakterület: " + techSzak);
                                    System.out.println("            Telefon: " + techTel);
                                    System.out.println("            Email: " + techEmail);
                                }
                            }

                            // 9. felhasznált alkatrészek
                            NodeList felhAlkLista = megrendelesElem.getElementsByTagName("felhasznalt_alkatresz");
                            for (int f = 0; f < felhAlkLista.getLength(); f++)
                            {
                                Element felhElem = (Element) felhAlkLista.item(f);

                                String mennyiseg = getTextContent(felhElem, "mennyiseg");
                                String egysegar = getTextContent(felhElem, "egysegar");
                                String felhDatum = getTextContent(felhElem, "felhasznalasDatum");

                                System.out.println("        FELHASZNÁLT ALKATRÉSZ:");
                                System.out.println("          Mennyiség: " + mennyiseg);
                                System.out.println("          Egységár: " + egysegar);
                                System.out.println("          Felhasználás dátuma: " + felhDatum);

                                // benne lévő <alkatresz>
                                Element alkatreszElem = (Element) felhElem.getElementsByTagName("alkatresz").item(0);
                                if (alkatreszElem != null)
                                {
                                    String alkID = alkatreszElem.getAttribute("alkatreszID");
                                    String megnev = getTextContent(alkatreszElem, "megnevezes");
                                    String tipusAlk = getTextContent(alkatreszElem, "tipus");
                                    String beszerAr = getTextContent(alkatreszElem, "beszerAr");
                                    String raktaronDb = getTextContent(alkatreszElem, "raktaronDb");

                                    System.out.println("          ALKATRÉSZ ID: " + alkID);
                                    System.out.println("            Megnevezés: " + megnev);
                                    System.out.println("            Típus: " + tipusAlk);
                                    System.out.println("            Beszer ár: " + beszerAr);
                                    System.out.println("            Raktáron db: " + raktaronDb);
                                }
                            }
                        }
                    }

                    System.out.println("-----------------------------------------");
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Segédfüggvény: visszaadja egy elem adott nevű gyerekeinek szövegét.
     * Ha nincs ilyen elem, üres stringet ad vissza, így nem lesz NullPointer.
     */
    private static String getTextContent(Element parent, String tagName)
    {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0)
        {
            Node node = list.item(0);
            return node.getTextContent();
        }
        return "";
    }
}
