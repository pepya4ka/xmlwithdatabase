import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

public class Xml {


    public static void parseXMLDocument(HashSet<Person> hashSet) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("persons.xml"));

        Element element = document.getDocumentElement();

        printElements(element.getChildNodes(), hashSet);

    }

    public static void printElements(NodeList nodeList, HashSet<Person> hashSet) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i) instanceof Element) {

                Person person = new Person();
                String value = "";
                if (!nodeList.item(i).getTextContent().trim().isEmpty()
                        && !((Text) nodeList.item(i).getFirstChild()).getData().trim().isEmpty()
                        && !((Text) nodeList.item(i).getFirstChild()).getData().trim().equals("\n")) {
                    Text text = (Text) nodeList.item(i).getFirstChild();
                    value += " = " + text.getData().trim();
                }

                if (nodeList.item(i).hasChildNodes()) {
                    NodeList childNodeList = nodeList.item(i).getChildNodes();
                    for (int j = 0; j < childNodeList.getLength(); j++) {
                        if (childNodeList.item(j) instanceof Element) {
                            String value1 = "";

                            if (!childNodeList.item(j).getTextContent().trim().isEmpty()
                                    && !((Text) childNodeList.item(j).getFirstChild()).getData().trim().isEmpty()
                                    && !((Text) childNodeList.item(j).getFirstChild()).getData().trim().equals("\n")) {
                                Text text = (Text) childNodeList.item(j).getFirstChild();
                                value1 += text.getData().trim();
                            }
                            if (childNodeList.item(j).getNodeName() == "depCode")
                                person.DepCode = value1;
                            if (childNodeList.item(j).getNodeName() == "depJob")
                                person.DepJob = value1;
                            if (childNodeList.item(j).getNodeName() == "description")
                                person.Description = value1;
                        }
                    }
                }
                hashSet.add(person);
            }
        }
    }

    public static void createXMLDocument(HashSet<Person> hashSet) throws ParserConfigurationException, TransformerException, FileNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element personsInfo = document.createElement("person-info");
        Element person;
        Element depCode;
        Element depJob;
        Element description;

        document.appendChild(personsInfo);
        Person[] people = {};
        people = hashSet.toArray(new Person[hashSet.size()]);
        for (int i = 0; i < people.length; i++) {
            person = document.createElement("person");
            personsInfo.appendChild(person);

            depCode = document.createElement("depCode");
            person.appendChild(depCode);
            depCode.appendChild(document.createTextNode(people[i].DepCode));

            depJob = document.createElement("depJob");
            person.appendChild(depJob);
            depJob.appendChild(document.createTextNode(people[i].DepJob));

            description = document.createElement("description");
            person.appendChild(description);
            description.appendChild(document.createTextNode(people[i].Description));
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream("result.xml")));
    }

}
