package domHDK6NX1105;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class DOMModify1HDK6NX {

    public static void main(String[] args) {
        try {
            // 0. forrás XML
            File xmlFile = new File("orarendHDK6NX.xml");   // tedd a projekt gyökerébe
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // 1.) -- egy példány módosítása: hozzáadunk egy <oraado> elemet
            // most az id="1" órát vesszük célba
            NodeList oraLista = doc.getElementsByTagName("ora");
            for (int i = 0; i < oraLista.getLength(); i++) {
                Node n = oraLista.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element oraElem = (Element) n;
                    String id = oraElem.getAttribute("id");
                    if ("1".equals(id)) {
                        // létrehozzuk az új elemet
                        Element oraadoElem = doc.createElement("oraado");
                        oraadoElem.setTextContent("Meghívott óraadó");
                        // beszúrjuk az órához a végére
                        oraElem.appendChild(oraadoElem);
                        break; // csak egy példányt kellett módosítani
                    }
                }
            }

            // 1.) kiírás konzolra + fájlba
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // konzolra
            System.out.println("---- 1. lépés: óraadó hozzáadva, módosított XML: ----");
            transformer.transform(new DOMSource(doc), new StreamResult(System.out));

            // fájlba
            File outFile = new File("orarendModify1Neptunkod.xml");
            transformer.transform(new DOMSource(doc), new StreamResult(outFile));
            System.out.println("\nA módosított fájl elmentve ide: " + outFile.getAbsolutePath());

            // 2.) -- minden óra típusának módosítása gyakorlat -> eloadas
            for (int i = 0; i < oraLista.getLength(); i++) {
                Node n = oraLista.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element oraElem = (Element) n;
                    String tipus = oraElem.getAttribute("tipus");
                    if ("gyakorlat".equalsIgnoreCase(tipus)) {
                        oraElem.setAttribute("tipus", "eloadas");
                    }
                }
            }

            // 2.) kiírás konzolra (strukturáltan)
            System.out.println("\n---- 2. lépés: minden gyakorlat -> eloadas ----");
            transformer.transform(new DOMSource(doc), new StreamResult(System.out));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
