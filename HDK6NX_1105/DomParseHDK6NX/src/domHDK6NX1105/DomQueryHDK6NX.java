package domHDK6NX1105;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class DomQueryHDK6NX
{

    public static void main(String[] args)
    {
        try {
            // 1. XML beolvasása
            File xmlFile = new File("hdk6nxhallgato.xml");  // tedd a projekt gyökerébe
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            // szépen normalizáljuk
            doc.getDocumentElement().normalize();

            // 2. gyökérelem kiírása
            System.out.println("Gyökér elem: " + doc.getDocumentElement().getNodeName());

            // 3. összes <hallgato> összegyűjtése
            NodeList hallgatoLista = doc.getElementsByTagName("hallgato");
            System.out.println("Hallgatók száma: " + hallgatoLista.getLength());
            System.out.println("Vezetéknevek:");

            // 4. végigmegyünk rajtuk és kiírjuk a <lastname> tartalmát
            for (int i = 0; i < hallgatoLista.getLength(); i++) {
                Node node = hallgatoLista.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element hallgatoElem = (Element) node;

                    // id is kiírható, hogy szebb legyen
                    String id = hallgatoElem.getAttribute("id");

                    // lastname elem lekérése
                    Element lastnameElem = (Element) hallgatoElem.getElementsByTagName("lastname").item(0);
                    String lastname = lastnameElem.getTextContent();

                    System.out.println("  hallgato id=" + id + " -> " + lastname);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
