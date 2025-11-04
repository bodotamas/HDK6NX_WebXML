package hdk6nx.domparse.hu;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class HDK6NXDomModify
{

    public static void main(String[] args)
    {
        try
        {
            // 1. XML beolvasása
            String inputXml = "HDK6NX_XML.xml";              // eredeti
            String outputXml = "HDK6NX_XML_updated.xml";     // ide mentjük a módosítottat

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(inputXml));
            doc.getDocumentElement().normalize();

            // 2. PÉLDA 1: módosítunk egy megrendelést (pl. M002 állapota legyen "javítás alatt")
            modositsMegrendelesAllapotot(doc, "M002", "javítás alatt");

            // 3. PÉLDA 2: beszúrunk egy új megrendelést az U002 ügyfél E003 eszközéhez
            beszurUjMegrendeles(doc,
                    "U002",            // ügyfél ID
                    "E003",            // eszköz ID
                    "M004",            // új megrendelés ID
                    "Kijelző védőüveg csere",
                    "befogadva",
                    "2025-11-04",
                    8000
            );

            // 4. módosított DOM kiírása fájlba
            kiirXmlFajlba(doc, outputXml);

            System.out.println("Módosítás kész. Nézd meg ezt a fájlt: " + outputXml);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Egy konkrét megrendelés (megrendelesID alapján) állapotát módosítja.
     */
    private static void modositsMegrendelesAllapotot(Document doc, String megrID, String ujAllapot)
    {
        NodeList megrendelesek = doc.getElementsByTagName("megrendeles");
        for (int i = 0; i < megrendelesek.getLength(); i++)
        {
            Element megr = (Element) megrendelesek.item(i);
            if (megrID.equals(megr.getAttribute("megrendelesID")))
            {
                // megtaláltuk
                Element allapotElem = (Element) megr.getElementsByTagName("allapot").item(0);
                allapotElem.setTextContent(ujAllapot);
                System.out.println("Megrendelés " + megrID + " állapota módosítva erre: " + ujAllapot);
                break;
            }
        }
    }

    /**
     * Új <megrendeles> elem beszúrása egy adott ügyfél adott eszközéhez.
     */
    private static void beszurUjMegrendeles(Document doc,
                                            String ugyfelID,
                                            String eszkozID,
                                            String ujMegrID,
                                            String hibaLeiras,
                                            String allapot,
                                            String beerkDatum,
                                            int fizetendo)
    {

        // 1. megkeressük az ügyfelet
        Element ugyfelElem = keresUgyfel(doc, ugyfelID);
        if (ugyfelElem == null)
        {
            System.out.println("Nincs ilyen ügyfél: " + ugyfelID);
            return;
        }

        // 2. azon belül megkeressük az eszközt
        Element eszkozElem = keresEszkoz(ugyfelElem, eszkozID);
        if (eszkozElem == null)
        {
            System.out.println("Nincs ilyen eszköz az ügyfélnél: " + eszkozID);
            return;
        }

        // 3. létrehozzuk az új <megrendeles> elemet
        Element ujMegr = doc.createElement("megrendeles");
        ujMegr.setAttribute("megrendelesID", ujMegrID);

        Element hibaElem = doc.createElement("hibaLeiras");
        hibaElem.setTextContent(hibaLeiras);
        ujMegr.appendChild(hibaElem);

        Element allapotElem = doc.createElement("allapot");
        allapotElem.setTextContent(allapot);
        ujMegr.appendChild(allapotElem);

        Element datumElem = doc.createElement("beerkDatum");
        datumElem.setTextContent(beerkDatum);
        ujMegr.appendChild(datumElem);

        Element fizElem = doc.createElement("fizetendoOsszeg");
        fizElem.setTextContent(String.valueOf(fizetendo));
        ujMegr.appendChild(fizElem);

        // 4. hozzáfűzzük az eszközhöz
        eszkozElem.appendChild(ujMegr);

        System.out.println("Új megrendelés beszúrva az ügyfél (" + ugyfelID + ") eszközéhez (" + eszkozID + "): " + ujMegrID);
    }

    /**
     * Ügyfél keresése ID alapján
     */
    private static Element keresUgyfel(Document doc, String ugyfelID)
    {
        NodeList ugyfelek = doc.getElementsByTagName("ugyfel");
        for (int i = 0; i < ugyfelek.getLength(); i++) {
            Element ugyfel = (Element) ugyfelek.item(i);
            if (ugyfelID.equals(ugyfel.getAttribute("ugyfelID")))
            {
                return ugyfel;
            }
        }
        return null;
    }

    /**
     * Eszköz keresése egy ügyfélen belül
     */
    private static Element keresEszkoz(Element ugyfelElem, String eszkozID)
    {
        NodeList eszkozok = ugyfelElem.getElementsByTagName("eszkoz");
        for (int i = 0; i < eszkozok.getLength(); i++)
        {
            Element eszkoz = (Element) eszkozok.item(i);
            if (eszkozID.equals(eszkoz.getAttribute("eszkozID")))
            {
                return eszkoz;
            }
        }
        return null;
    }

    /**
     * Dokumentum kiírása fájlba
     */
    private static void kiirXmlFajlba(Document doc, String fajlNev) throws TransformerException
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        // szebb, behúzott xml
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fajlNev));
        transformer.transform(source, result);
    }
}
