import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import models.*;

public class DOMparser {
    public static class SimpleErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
        public void error(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
        public void fatalError(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
    }

    public static Showsalon parse(String path) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new SimpleErrorHandler());
        Document doc = builder.parse(new File(path));
        doc.getDocumentElement().normalize();

        Showsalon showsalon = new Showsalon();
        NodeList nodes = doc.getElementsByTagName("Manufacture");

        for(int i = 0; i < nodes.getLength(); ++i) {
            Element n = (Element)nodes.item(i);
            Manufacture manufacture = new Manufacture();
            manufacture.setId(Integer.parseInt(n.getAttribute("id")));
            manufacture.setName(n.getAttribute("name"));
            showsalon.addManufacture(manufacture);
        }

        nodes = doc.getElementsByTagName("Brand");
        for(int j =0; j < nodes.getLength(); ++j) {
            Element e = (Element) nodes.item(j);
            Brand brand = new Brand();
            brand.setId(Integer.parseInt(e.getAttribute("id")));
            brand.setManufactureID(Integer.parseInt(e.getAttribute("manufactureID")));
            brand.setName(e.getAttribute("name"));
            brand.setPrice(Integer.parseInt(e.getAttribute("price")));
            showsalon.addBrand(brand, e.getAttribute("manufactureID"));
        }

        return showsalon;
    }

    public static void write(Showsalon showsalon, String path) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("Showsalon");
        doc.appendChild(root);

        Map<String, Manufacture> authors = showsalon.getManufactures();
        for(Map.Entry<String, Manufacture> entry : authors.entrySet()) {
            Element gnr = doc.createElement("Manufacture");
            gnr.setAttribute("id", Integer.toString(entry.getValue().getId()));
            gnr.setAttribute("name", entry.getValue().getName());
            root.appendChild(gnr);

            for(Brand brand: entry.getValue().getBrands()) {
                Element mv = doc.createElement("Brand");
                mv.setAttribute("id", Integer.toString(brand.getId()));
                mv.setAttribute("manufactureID", Integer.toString(brand.getManufactureID()));
                mv.setAttribute("name", brand.getName());
                mv.setAttribute("price", String.valueOf(brand.getPrice()));
                gnr.appendChild(mv);
            }
        }
        Source domSource = new DOMSource(doc);
        Result fileResult = new StreamResult(new File(path));
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer transformer = tfactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "manufacture.dtd");
        transformer.transform(domSource, fileResult);
    }
}