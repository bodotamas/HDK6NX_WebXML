package HDK6NX_1015;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class DomReadHDK6NX 
{

    public static void main(String[] args) 
    {
        try 
        {
            File xmlFile = new File("HDK6NX_orarend.xml");
            if (!xmlFile.exists()) 
            {
                System.out.println("Nem található az XML fájl!");
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("📚 HDK6NX órarend blokk formában:\n");
            printElement(doc.getDocumentElement(), 0);

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    private static void printElement(Element element, int indent) 
    {
        printIndent(indent);
        System.out.println("<" + element.getTagName() + getAttributesAsString(element) + ">");

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) 
        {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) 
            {
                printElement((Element) node, indent + 2);
            } 
            else if (node.getNodeType() == Node.TEXT_NODE) 
            {
                String text = node.getTextContent().trim();
                if (!text.isEmpty()) 
                {
                    printIndent(indent + 2);
                    System.out.println(text);
                }
            }
        }

        printIndent(indent);
        System.out.println("</" + element.getTagName() + ">");
    }

    private static String getAttributesAsString(Element element) 
    {
        NamedNodeMap attrs = element.getAttributes();
        if (attrs == null || attrs.getLength() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < attrs.getLength(); i++) 
        {
            Node attr = attrs.item(i);
            sb.append(" ").append(attr.getNodeName()).append("=\"").append(attr.getNodeValue()).append("\"");
        }
        return sb.toString();
    }

    private static void printIndent(int indent) 
    {
        for (int i = 0; i < indent; i++) System.out.print(" ");
    }
}
