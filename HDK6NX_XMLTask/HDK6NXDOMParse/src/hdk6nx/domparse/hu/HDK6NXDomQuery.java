package hdk6nx.domparse.hu;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class HDK6NXDomQuery
{

    public static void main(String[] args)
    {
        try
        {
            // ugyanonnan olvassuk, ahová az előbb beraktad
            String xmlFile = "HDK6NX_XML.xml";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFile));
            doc.getDocumentElement().normalize();

            // 1. lekérdezés: összes ügyfél
            listazUgyfelek(doc);

            // 2. lekérdezés: összes megrendelés, ami "javítás alatt" vagy "befogadva"
            listazMegrendelesAllapotSzerint(doc, "javítás alatt");
            listazMegrendelesAllapotSzerint(doc, "befogadva");

            // 3. lekérdezés: összes technikus kiírása
            listazTechnikusok(doc);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // ===== 1. összes ügyfél neve és ID-je =====
    private static void listazUgyfelek(Document doc)
    {
        System.out.println("=== 1. Ügyfelek listája ===");
        NodeList ugyfelek = doc.getElementsByTagName("ugyfel");
        for (int i = 0; i < ugyfelek.getLength(); i++)
        {
            Element ugyfel = (Element) ugyfelek.item(i);
            String id = ugyfel.getAttribute("ugyfelID");
            String nev = getText(ugyfel, "nev");
            System.out.println("Ügyfél ID: " + id + " | Név: " + nev);
        }
        System.out.println();
    }

    // ===== 2. megrendelések szűrése állapot szerint =====
    private static void listazMegrendelesAllapotSzerint(Document doc, String keresettAllapot)
    {
        System.out.println("=== 2. Megrendelések állapot szerint: " + keresettAllapot + " ===");

        // minden megrendelést összeszedünk
        NodeList megrendelesek = doc.getElementsByTagName("megrendeles");
        for (int i = 0; i < megrendelesek.getLength(); i++)
        {
            Element megr = (Element) megrendelesek.item(i);

            String allapot = getText(megr, "allapot");

            if (keresettAllapot.equalsIgnoreCase(allapot))
            {
                String megrID = megr.getAttribute("megrendelesID");
                String hiba = getText(megr, "hibaLeiras");
                String datum = getText(megr, "beerkDatum");
                String fizetendo = getText(megr, "fizetendoOsszeg");

                System.out.println("Megrendelés ID: " + megrID);
                System.out.println("  Hiba: " + hiba);
                System.out.println("  Dátum: " + datum);
                System.out.println("  Fizetendő: " + fizetendo);
            }
        }
        System.out.println();
    }

    // ===== 3. technikusok kilistázása =====
    private static void listazTechnikusok(Document doc)
    {
        System.out.println("=== 3. Technikusok ===");
        NodeList technikusok = doc.getElementsByTagName("technikus");
        for (int i = 0; i < technikusok.getLength(); i++) {
            Element tech = (Element) technikusok.item(i);
            String techID = tech.getAttribute("technikusID");
            String nev = getText(tech, "nev");
            String szak = getText(tech, "szakTerulet");
            String tel = getText(tech, "telefon");
            System.out.println("Technikus ID: " + techID);
            System.out.println("  Név: " + nev);
            System.out.println("  Szakterület: " + szak);
            System.out.println("  Telefon: " + tel);
        }
        System.out.println();
    }

    // kis segédfüggvény
    private static String getText(Element parent, String tagName)
    {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0)
        {
            return list.item(0).getTextContent();
        }
        return "";
    }
}
