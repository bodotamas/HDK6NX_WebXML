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

public class DOMModifyHDK6NX
{

    public static void main(String[] args)
    {
        try
        {
            File xmlFile = new File("HDK6NXhallgato.xml"); // ide rakd az XML-t a projekt gyökérbe
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList hallgatoList = doc.getElementsByTagName("hallgato");

            for (int i = 0; i < hallgatoList.getLength(); i++)
            {
                Node node = hallgatoList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element hallgatoElem = (Element) node;

                    // id ellenőrzése – a te XML-edben "1", nem "01"
                    String id = hallgatoElem.getAttribute("id");
                    if ("1".equals(id) || "01".equals(id))
                    {

                        // firstname elem módosítása
                        Element firstNameElem = (Element) hallgatoElem.getElementsByTagName("firstname").item(0);
                        firstNameElem.setTextContent("Kovács");

                        // lastname elem módosítása
                        Element lastNameElem = (Element) hallgatoElem.getElementsByTagName("lastname").item(0);
                        lastNameElem.setTextContent("János");

                        // ha akarnád, itt a profession is módosítható lenne
                        // Element professionElem = (Element) hallgatoElem.getElementsByTagName("profession").item(0);
                        // professionElem.setTextContent("valami más");
                    }
                }
            }


            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(System.out);
            transformer.transform(source, console);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
