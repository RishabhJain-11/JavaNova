package xpath_evaluator;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.saxon.s9api.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Main {
    public static void main(String[] args) {
        File xmlFile = new File("src/main/java/student.xml");

        try {
            // Extract namespaces dynamically
            Map<String, String> namespaceMap = extractNamespaces(xmlFile);

            // Initialize Saxon Processor
            Processor processor = new Processor(false);
            net.sf.saxon.s9api.DocumentBuilder builder = processor.newDocumentBuilder();

            // Build the XML document
            XdmNode xmlDocument = builder.build(xmlFile);

            // Initialize XPath Compiler
            XPathCompiler xpathCompiler = processor.newXPathCompiler();

            // Declare extracted namespaces
            namespaceMap.forEach(xpathCompiler::declareNamespace);

            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.println("\nEnter an XPath expression (or type 'exit' to quit): ");
                    String xpathExpression = scanner.nextLine();

                    // Exit condition
                    if (xpathExpression.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting program.");
                        break;
                    }

                    try {
                        // Modify the XPath expression to apply normalize-space()
                        String normalizedXPathExpression = String.format(
                                "normalize-space(%s)", xpathExpression);

                        // Evaluate the XPath expression
                        XdmValue result = xpathCompiler.evaluate(normalizedXPathExpression, xmlDocument);

                        // Print the results
                        printResults(result);
                    } catch (SaxonApiException e) {
                        System.out.println("Invalid XPath expression or evaluation error: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing XML file: " + e.getMessage());
        }
    }

    private static Map<String, String> extractNamespaces(File xmlFile) throws Exception {
        Map<String, String> namespaceMap = new TreeMap<>();

        // Parse XML document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(xmlFile);

        // Get the root element
        Node root = document.getDocumentElement();

        // Extract namespaces from the root element
        NamedNodeMap attributes = root.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            String name = attribute.getNodeName();
            if (name.startsWith("xmlns")) {
                // Determine if it's a default namespace or a prefixed one
                String prefix = name.contains(":") ? name.split(":")[1] : "";
                String uri = attribute.getNodeValue();
                namespaceMap.put(prefix, uri);
            }
        }

        // Add the default namespace with an empty prefix, if present
        if (namespaceMap.containsKey("")) {
            namespaceMap.put(XMLConstants.DEFAULT_NS_PREFIX, namespaceMap.get(""));
            namespaceMap.remove("");
        }

        // Print extracted namespaces (for debugging)
        namespaceMap.forEach((key, value) ->
                System.out.println("Namespace: prefix='" + key + "' uri='" + value + "'"));

        return namespaceMap;
    }

    private static void printResults(XdmValue result) {
        if (result.size() == 0) {
            System.out.println("No results found.");
        } else {
            int index = 1;
            for (XdmItem item : result) {
                // Normalize the value
                String normalizedValue = item.getStringValue().trim().replaceAll("\\s+", " ");
                System.out.println(index++ + ". " + normalizedValue);
            }
        }
    }
}
