package xpath_new;

import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SaxonApiException {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <library xmlns="http://example.com/library" xmlns:bk="http://example.com/book">
                  <bk:book>
                    <bk:title>Effective Java</bk:title>
                    <bk:author>Joshua Bloch</bk:author>
                    <bk:publishedDate>2008-05-08</bk:publishedDate>
                  </bk:book>
                  <bk:book>
                    <bk:title>Clean Code</bk:title>
                    <bk:author>Robert C. Martin</bk:author>
                    <bk:publishedDate>2008-08-11</bk:publishedDate>
                  </bk:book>
                  <librarian>
                    <name>Jane Doe</name>
                    <employeeId>12345</employeeId>
                  </librarian>
                </library>
                """;

        Processor processor = new Processor(false);

        try {
            XdmNode xdm = processor.newDocumentBuilder().build(new StreamSource(new StringReader(xml)));

            Map<String, String> namespaces = extractNamespaces(xdm);

            XPathCompiler xpath = processor.newXPathCompiler();

            for(Map.Entry<String, String> entry: namespaces.entrySet()) {
                xpath.declareNamespace(entry.getKey(), entry.getValue());
            }

            Scanner scanner = new Scanner(System.in);
            String query;

            while(true) {
                System.out.println("Enter the XPath query (or type exit) : ");
                query = scanner.nextLine();

                if("exit".equalsIgnoreCase(query)) {
                    break;
                }

                try {
                    XdmValue result = xpath.evaluate(query, xdm);

                    StringBuilder sb = new StringBuilder();

                    for(int i = 0; i < result.size(); i++) {
                        String normalizedValue = xpath.evaluateSingle("normalize-space(.)", result.itemAt(i)).getStringValue();
                        sb.append(normalizedValue);

                        if(i + 1 != result.size()) {
                            sb.append(", ");
                        }
                    }
                    System.out.println("Result : " + sb);
                } catch (Exception e) {
                    System.out.println("Error evaluating XPath query : " + e.getMessage());
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Map<String, String>  extractNamespaces(XdmNode node) {
        Map<String, String> namespaces = new HashMap<>();
        extractNamespaces(node, namespaces);
        return namespaces;
    }

    private static void extractNamespaces(XdmNode node, Map<String, String> namespaces) {
        Iterator<XdmNode> iterator = node.axisIterator(Axis.NAMESPACE);
        while (iterator.hasNext()) {
            XdmNode ns = iterator.next();
            String prefix = ns.getNodeName() == null ? "" : ns.getNodeName().getLocalName();
            namespaces.put(prefix, ns.getStringValue());
        }
        Iterator<XdmNode> children = node.axisIterator(Axis.CHILD);

        while(children.hasNext()) {
            extractNamespaces(children.next(), namespaces);
        }
    }
}
