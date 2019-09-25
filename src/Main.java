import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashSet;

public class Main {

    static HashSet<Person> hashSet;

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        hashSet = new HashSet<>();

        Xml.parseXMLDocument(hashSet);
        Sql.updateDataBase(hashSet);

//        Sql.createXMLDocumentWithSQL();

//        Xml.createXMLDocument(hashSet);


        if (!hashSet.isEmpty())
            System.out.println(hashSet.size());
    }
}
