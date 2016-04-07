package testeDOM4J;

import java.io.File;
import java.util.List;
import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class TesteDOM4J {
    public static void main(String[] args) {
        try {
           File inputFile = new File("curriculo.xml");
           SAXReader reader = new SAXReader();
           Document document = reader.read( inputFile );
           
           Element root = document.getRootElement();
           System.out.println("Elemento raiz :" + root.getName());

           String nome;
           nome = document.selectSingleNode("//DADOS-GERAIS").valueOf("@NOME-COMPLETO");
           System.out.println(nome);

           List<Node> nodes = document.selectNodes("//ARTIGO-PUBLICADO[DADOS-BASICOS-DO-ARTIGO[@ANO-DO-ARTIGO=2011]]" );
           System.out.println("----------------------------");
           for (Node node : nodes) {            
              System.out.println("Nome do artigo: " + 
              node.selectSingleNode("DADOS-BASICOS-DO-ARTIGO").valueOf("@TITULO-DO-ARTIGO") );

              //System.out.println("\nCurrent Element :" + node.getName());
              //node.selectSingleNode("lastname").getText());            
           }
        
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
        try {
            StringWriter stringWriter = new StringWriter();

            XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();	
            XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);
   
            xMLStreamWriter.writeStartDocument();
         xMLStreamWriter.writeStartElement("CURRICULO-VITAE");
   
         xMLStreamWriter.writeStartElement("ARTIGOS-PUBLICADOS");
      
         xMLStreamWriter.writeStartElement("ARTIGO-PUBLICADO");	
         
         xMLStreamWriter.writeStartElement("DADOS-BASICOS-DO-ARTIGO");	
         xMLStreamWriter.writeAttribute("ANO-DO-ARTIGO", "2011");
         xMLStreamWriter.writeAttribute("TITULO-DO-ARTIGO", "teste1");
         xMLStreamWriter.writeEndElement();//end-DADOS-BASICOS-DO-ARTIGO
         
         xMLStreamWriter.writeEndElement();//end-ARTIGO-PUBLICADO

         xMLStreamWriter.writeEndElement();//end-ARTIGOS-PUBLICADOS
         
         xMLStreamWriter.writeEndElement();//end-CURRICULO-VITAE
         xMLStreamWriter.writeEndDocument();

         xMLStreamWriter.flush();
         xMLStreamWriter.close();

         String xmlString = stringWriter.getBuffer().toString();

         stringWriter.close();

         System.out.println(xmlString);

      } catch (XMLStreamException e) {
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
        
        
   }//endMain
}//endClass