import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import java.io.IOException;


public class xPathQueryHDK6NX 
{

    public static void main(String[] args) 
    {

        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse("studentHDK6NX.xml");

            document.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList hdk6nx = (NodeList) xPath.compile("HDK6NX").evaluate(document, XPathConstants.NODESET);

            for(int i = 0; i < hdk6nx.getLength(); i++) {

                Node node = hdk6nx.item(i);

                System.out.println("\nAktualis elem: " + node.getNodeName());

                if(node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("studentHDK6NX")) {

                    Element element = (Element) node;

                    System.out.println("Hallgató ID: "
                            + element.getAttribute("id"));

                    System.out.println("Keresztnév: "
                            + element.getElementsByTagName("keresztnev").item(0).getTextContent());

                    System.out.println("Vezeteknév: "
                            + element.getElementsByTagName("vezeteknev").item(0).getTextContent());

                    System.out.println("Becenév: "
                            + element.getElementsByTagName("becenev").item(0).getTextContent());

                    System.out.println("kor: "
                            + element.getElementsByTagName("kor").item(0).getTextContent());
                }
            }
    } catch (ParserConfigurationException e) {
        e.printStackTrace();
    } catch (SAXException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (XPathExpressionException e) {
        e.printStackTrace();
    }
    }
}
