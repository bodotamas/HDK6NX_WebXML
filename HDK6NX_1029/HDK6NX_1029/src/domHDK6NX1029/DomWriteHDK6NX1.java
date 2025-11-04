package domHDK6NX1029;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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

public class DomWriteHDK6NX1
{
    public static void main(String[] args)
    {
        try {
            // 1. DOM builder előkészítése
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            factory.setNamespaceAware(false);
            factory.setValidating(false);

            DocumentBuilder builder = factory.newDocumentBuilder();

            // 2. Bemeneti XML beolvasása
            // Fontos: legyen ott a projekt gyökerében / working directoryban "orarendNeptunkod.xml" néven
            File inputXml = new File("orarend1HDK6NX.xml");
            Document doc = builder.parse(inputXml);

            // 3. normalizálás
            doc.getDocumentElement().normalize();

            // 4. DOM kiírása faszerkezetben konzolra
            System.out.println("=== Fastruktúra a orarendNeptunkod.xml alapján ===");
            printNode(doc, 0);

            // 5. DOM kiírása új XML fájlba
            File outputXml = new File("orarend1Neptunkod.xml");
            writeDocumentToFile(doc, outputXml);

            System.out.println("\nA dokumentum ki lett írva ide: " + outputXml.getName());

        } catch (Exception e) {
            System.out.println("Hiba történt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========================
    // DOM -> konzol fa formában
    // ========================
    private static void printNode(Node node, int indent) {
        // behúzás string felépítése
        StringBuilder padBuilder = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            padBuilder.append("    "); // 4 space per szint
        }
        String pad = padBuilder.toString();

        short type = node.getNodeType();

        if (type == Node.DOCUMENT_NODE) {
            // XML deklaráció
            System.out.println(pad + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

            // gyerekek bejárása
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                printNode(children.item(i), indent);
            }
            return;
        }

        if (type == Node.ELEMENT_NODE) {
            Element elem = (Element) node;

            // kezdő tag + attribútumok
            StringBuilder startTag = new StringBuilder();
            startTag.append(pad).append("<").append(elem.getNodeName());

            NamedNodeMap attrs = elem.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Node a = attrs.item(i);
                startTag.append(" ")
                        .append(a.getNodeName())
                        .append("=\"")
                        .append(a.getNodeValue())
                        .append("\"");
            }
            startTag.append(">");

            System.out.println(startTag.toString());

            // gyerekek feldolgozása
            NodeList children = elem.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node ch = children.item(i);

                if (ch.getNodeType() == Node.TEXT_NODE) {
                    String text = ch.getNodeValue();
                    if (text != null) {
                        text = text.trim();
                        if (!text.isEmpty()) {
                            System.out.println(pad + "    " + text);
                        }
                    }
                } else {
                    printNode(ch, indent + 1);
                }
            }

            // záró tag
            System.out.println(pad + "</" + elem.getNodeName() + ">");
            return;
        }

        if (type == Node.TEXT_NODE) {
            String text = node.getNodeValue();
            if (text != null) {
                text = text.trim();
                if (!text.isEmpty()) {
                    System.out.println(pad + text);
                }
            }
            return;
        }

        if (type == Node.COMMENT_NODE) {
            System.out.println(pad + "<!-- " + node.getNodeValue() + " -->");
            return;
        }

        // fallback más node típusokra (CDATA, stb.)
        System.out.println(pad + node.getNodeName() + ": " + node.getNodeValue());
    }

    // ========================
    // DOM -> fájlba mentés
    // ========================
    private static void writeDocumentToFile(Document doc, File outFile) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            // szépen formázott kimenet
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // extra behúzás property (Apache-implementációk értik)
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(outFile);

            transformer.transform(source, result);

        } catch (Exception e) {
            System.out.println("Nem sikerült kiírni a fájlt: " + outFile.getName());
            e.printStackTrace();
        }
    }
}
