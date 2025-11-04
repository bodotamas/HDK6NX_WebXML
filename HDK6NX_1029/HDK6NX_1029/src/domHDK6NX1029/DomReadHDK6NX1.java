package domHDK6NX1029;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DomReadHDK6NX1
{
    public static void main(String argv[]) throws SAXException, IOException, ParserConfigurationException
    {
        try {
            // DOM builder létrehozása
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            factory.setNamespaceAware(false);
            factory.setValidating(false);

            DocumentBuilder builder = factory.newDocumentBuilder();

            // XML fájl beolvasása
            File xmlFile = new File("orarend1HDK6NX.xml");
            Document doc = builder.parse(xmlFile);

            // normalizálás
            doc.getDocumentElement().normalize();

            // XML fa kiírása blokk formában
            printNode(doc, 0);

        } catch (Exception e) {
            System.out.println("Hiba történt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Rekurzív kiíró függvény az XML struktúrához
    private static void printNode(Node node, int indent) {
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            pad.append("    ");
        }

        switch (node.getNodeType()) {

            case Node.DOCUMENT_NODE: {
                Document doc = (Document) node;
                System.out.println(pad + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                NodeList children = doc.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    printNode(children.item(i), indent);
                }
                break;
            }

            case Node.ELEMENT_NODE: {
                Element elem = (Element) node;
                StringBuilder startTag = new StringBuilder();
                startTag.append(pad).append("<").append(elem.getNodeName());

                // attribútumok
                NamedNodeMap attrs = elem.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Node a = attrs.item(i);
                    startTag.append(" ").append(a.getNodeName()).append("=\"").append(a.getNodeValue()).append("\"");
                }
                startTag.append(">");

                System.out.println(startTag.toString());

                // gyerekek
                NodeList children = elem.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node ch = children.item(i);
                    if (ch.getNodeType() == Node.TEXT_NODE) {
                        String text = ch.getNodeValue().trim();
                        if (!text.isEmpty()) {
                            System.out.println(pad + "    " + text);
                        }
                    } else {
                        printNode(ch, indent + 1);
                    }
                }

                System.out.println(pad + "</" + elem.getNodeName() + ">");
                break;
            }

            case Node.TEXT_NODE: {
                String text = node.getNodeValue().trim();
                if (!text.isEmpty()) {
                    System.out.println(pad + text);
                }
                break;
            }

            case Node.COMMENT_NODE: {
                System.out.println(pad + "<!-- " + node.getNodeValue() + " -->");
                break;
            }

            default: {
                System.out.println(pad + node.getNodeName() + ": " + node.getNodeValue());
                break;
            }

        }

    }
}