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

public class DomWriteHDK6NX
{
    public static void main(String[] args)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            factory.setNamespaceAware(false);
            factory.setValidating(false);

            DocumentBuilder builder = factory.newDocumentBuilder();

            // Bemeneti XML
            File inputXml = new File("HDK6NXhallgato.xml");
            Document doc = builder.parse(inputXml);
            doc.getDocumentElement().normalize();

            System.out.println("=== Fastruktúra a hallgatoNeptunkod.xml alapján ===");
            printNode(doc, 0);

            // Kimeneti XML
            File outputXml = new File("hallgato1HDK6NX.xml");
            writeDocumentToFile(doc, outputXml);

            System.out.println("\nA dokumentum ki lett írva ide: " + outputXml.getName());

        }
        catch (Exception e)
        {
            System.out.println("Hiba történt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printNode(Node node, int indent) {
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < indent; i++) pad.append("    ");
        String prefix = pad.toString();

        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            System.out.println(prefix + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                printNode(children.item(i), indent);
            }
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) node;
            StringBuilder startTag = new StringBuilder();
            startTag.append(prefix).append("<").append(elem.getNodeName());

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
            System.out.println(startTag);

            NodeList children = elem.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node ch = children.item(i);
                if (ch.getNodeType() == Node.TEXT_NODE) {
                    String text = ch.getNodeValue().trim();
                    if (!text.isEmpty()) {
                        System.out.println(prefix + "    " + text);
                    }
                } else {
                    printNode(ch, indent + 1);
                }
            }
            System.out.println(prefix + "</" + elem.getNodeName() + ">");
        }
    }

    private static void writeDocumentToFile(Document doc, File outFile) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
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
