package testeDOM4J;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


/*
 http://dom4j.sourceforge.net/dom4j-1.6.1/apidocs/org/dom4j/Document.html
 http://dom4j.sourceforge.net/dom4j-1.6.1/apidocs/org/dom4j/Node.html
 http://dom4j.sourceforge.net/dom4j-1.6.1/apidocs/
 */

public class testeDOM4J2 {
    
    //##### UTIL
	//######################################################################
	//######################################################################
	//######################################################################
    
    public static Document abrirCurriculoXML( String path ){
    	try 
    	{
    		File inputFile = new File( path ); //"curriculo.xml"
            SAXReader reader = new SAXReader();
            Document documentoXML = reader.read(inputFile);
            return documentoXML;
    	}
    	catch (DocumentException e) 
    	{
    		e.printStackTrace();
    	}
		return null;
    	
    	
    }
    
    //

    public static void escreverXML(Document document) {
        
		try {
			XMLWriter writer;
			writer = new XMLWriter(
			        new FileWriter("output.xml")
			);
			writer.write(document);
	        writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    //
    
    public static void printXML(Document document) throws IOException 
    {    	    	
    	try {
    		// Pretty print the document to System.out
    		OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(System.out, format);
			writer.write(document);writer.close();
			
			/*
			// Compact format to System.out
	        format = OutputFormat.createCompactFormat();
	        writer = new XMLWriter(System.out, format);
	        writer.write(document);writer.close();
	        */
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //  
    
    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array)
            if (e == v || v != null && v.equals(e))
                return true;

        return false;
    }
    
    
    // 
    public static void addNoArray(String name, ArrayList<String> array){
    	if ( !array.contains(name) /*Arrays.asList(array).contains(name)*/){
    		array.add( name );
    	}
    }
    
    //######################################################################
  //######################################################################
  //######################################################################
    
    //
    public static void extrairMetadados(Document doc) {		
    	ArrayList<String> array = new ArrayList<String>();
    	Element root = doc.getRootElement();    	
    	extrairMetadadosRecursivo(array, root);
    	System.out.println( array );
    	for ( String name : array ) {
    		System.out.println( name );
    	}
    
	}	
    
    //
    
    public static void extrairMetadadosRecursivo(ArrayList<String> array, Element proximo){ //já enviar o root
    	
    	//Cria array onde seram inseridos os nomes dos metadados -> Evoluir para um Map, chave valor, para usar na seleção de metadados
    	    	
    	String metadado = proximo.getName();
    	addNoArray(metadado, array);
    	
    	if (proximo.hasContent()) {
    		List<Element> elementos = proximo.elements();
    		List<Attribute> atributos = proximo.attributes();
    		
    		//atributo iterator
    		if ( !atributos.isEmpty() ){
    			for ( Iterator a = proximo.attributeIterator(); a.hasNext(); ) {
    	            Attribute attribute = (Attribute) a.next();
    	            addNoArray( attribute.getName(), array);
    	        }
    		}//EndAtrituboIterator
    		
    		if ( !elementos.isEmpty() ){
    			for ( Iterator b = proximo.elementIterator(); b.hasNext(); ) {
    	            Element element = (Element) b.next();
    	            addNoArray( element.getName(), array);
    	            if ( element.hasContent() ){
    	            	extrairMetadadosRecursivo(array, element);
    	            }
    	        }
    		}    		
    		
    	}//endHasContent
    }
    
    //    
    
    public static void exemplo(Document document) throws IOException
    {
    	Document output = DocumentHelper.createDocument();
		// Cria documento
		Element root = output.addElement( document.getRootElement().getName() );
		// Cria elemento root

		String nome = document.selectSingleNode("//DADOS-GERAIS").valueOf("@NOME-COMPLETO");
		// Extraio nome do curriculo
		Element dadosGerais = root.addElement("DADOS-GERAIS").addAttribute("NOME-COMPLETO", nome);
		// Adiciono ele no novo XML ao root
		
		Element artigosPublicados = root.addElement("ARTIGOS-PUBLICADOS");
		// Adiciono Elemento "ARTIGOS-PUBLICADOS" o qual todos os artigos serão filhos

		List<Node> nodes = document.selectNodes("//ARTIGO-PUBLICADO");
		// Seleciono os nos com nome "ARTIGO-PUBLICADO" utilizando xpath para a busca
		
		//List<Node> nodes = document.selectNodes(
		//        "//ARTIGO-PUBLICADO[DADOS-BASICOS-DO-ARTIGO[@ANO-DO-ARTIGO=2011]]");
		// Seleciono os artigos publicados no ano 2011 utilizando xpath para a busca
		
		for (Node node : nodes) {
			// iteração com os elementos retornados da busca
		    String ano = node.selectSingleNode("DADOS-BASICOS-DO-ARTIGO").valueOf("@ANO-DO-ARTIGO");
		    // extraio o ano do artigo
		    String titulo = node.selectSingleNode("DADOS-BASICOS-DO-ARTIGO").valueOf("@TITULO-DO-ARTIGO");
		    // extraio o titulo do artigo
		    
		    Element artigoPublicado = artigosPublicados.addElement("ARTIGO-PUBLICADO");
		    // adiciono a tag artigos publicos a tag artigo publicado
		    Element dadosBasicosArtigo = artigoPublicado.addElement("DADOS-BASICOS-DO-ARTIGO")
		            .addAttribute("ANO-DO-ARTIGO", ano)
		            .addAttribute("TITULO-DO-ARTIGO", titulo);
		    // adiciono os atributos a tag artigo publicado
		}
		
		escreverXML(output);
		printXML(output);
		
		
		/* 
		
		#### PARA EXTRAIR PARA O EXEMPLO
		
		<OUTRA-PRODUCAO>
			<ORIENTACOES-CONCLUIDAS>
				<ORIENTACOES-CONCLUIDAS-PARA-MESTRADO SEQUENCIA-PRODUCAO="51">...</ORIENTACOES-CONCLUIDAS-PARA-MESTRADO>	
				<ORIENTACOES-CONCLUIDAS-PARA-DOUTORADO SEQUENCIA-PRODUCAO="42">...</ORIENTACOES-CONCLUIDAS-PARA-DOUTORADO>
				<OUTRAS-ORIENTACOES-CONCLUIDAS*/
		
		/*<ATUACOES-PROFISSIONAIS>
			<ATUACAO-PROFISSIONAL>
				<ATIVIDADES-DE-PARTICIPACAO-EM-PROJETO>
					<PARTICIPACAO-EM-PROJETO
		
		#### LINHA DE PESQUISA
		
		<ATIVIDADES-DE-PESQUISA-E-DESENVOLVIMENTO>
		<PESQUISA-E-DESENVOLVIMENTO SEQUENCIA-FUNCAO-ATIVIDADE="11" FLAG-PERIODO="ATUAL" MES-INICIO="05" ANO-INICIO="2009" MES-FIM="" ANO-FIM="" CODIGO-ORGAO="047002000993"NOME-ORGAO="Campus João Pessoa" CODIGO-UNIDADE="" NOME-UNIDADE="">
		<LINHA-DE-PESQUISA SEQUENCIA-LINHA="1" TITULO-DA-LINHA-DE-PESQUISA="Integração de Dados

		*/
		
    }
    
    //
    
    public static void exemploAreaConhecimento(Document document) 
    {    	
    	
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	// A tag <AREAS-DO-CONHECIMENTO> aparenta ter a possibiliade de ter mais de um filho, pois, foi encontrato
    	//a tag <AREA-DO-CONHECIMENTO-1>, ou seja, enumerada.
    	List<Node> trabalhoEmEventos = document.selectNodes("//TRABALHO-EM-EVENTOS/AREAS-DO-CONHECIMENTO");   
    	List<Node> artigosPublicados = document.selectNodes("//ARTIGO-PUBLICADO/AREAS-DO-CONHECIMENTO"); 
    	List<Node> livrosPublicadosOuOrg = document.selectNodes("//LIVRO-PUBLICADO-OU-ORGANIZADO/AREAS-DO-CONHECIMENTO"); 
    	List<Node> capLivrosPublicados = document.selectNodes("//CAPITULO-DE-LIVRO-PUBLICADO/AREAS-DO-CONHECIMENTO"); 
    	
    	
    	extrairQuantidadeAreaDoConhecimento(  trabalhoEmEventos , map );
    	extrairQuantidadeAreaDoConhecimento(  artigosPublicados , map );
    	extrairQuantidadeAreaDoConhecimento(  livrosPublicadosOuOrg , map );
    	extrairQuantidadeAreaDoConhecimento(  capLivrosPublicados , map );
    	System.out.println(map);   
    }
    
    //
    
    public static void extrairQuantidadeAreaDoConhecimento( List<Node> nodes, Map<String, Integer> map )
    {
    	for (Node node : nodes) 
    	{
    		if ( node.hasContent() ) 
    		{    			
    			Element element = (Element) node;
    			List<Element> Elementos  = element.elements();
    			for ( Element elemento : Elementos )
    			{
    				
    				String attValue = elemento.attribute("NOME-DA-AREA-DO-CONHECIMENTO").getValue();
    				if ( !map.containsKey(attValue) ){
    					map.put (elemento.attribute("NOME-DA-AREA-DO-CONHECIMENTO").getValue(), 1 );
    				}
    				else {    					
    					map.put(attValue, map.get(attValue) + 1);
    				}
    								
    			}
    			
    		}
		}
    }
    
    public static void extrairQuantidadeAreaDoConhecimento2( Document document ){
    	
    	List<Attribute> Atributos = document.selectNodes("//@NOME-DA-AREA-DO-CONHECIMENTO"); 
    	
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	
    	for ( Attribute atributo : Atributos )
		{
    		if ( atributo.getValue() != "" ) {
    			String attValue = atributo.getValue();
    			if ( !map.containsKey(attValue) ){
    				map.put (atributo.getValue(), 1 );
    			}
    			else {    					
    				map.put(attValue, map.get(attValue) + 1);
    			}
    		}			
							
		}
    	
    	
    	for (String key : map.keySet()) {
	    System.out.println(key + " = " + map.get(key));
		}
		
    	
    	//System.out.println(map); SUB-AREA-DO-CONHECIMENTO
    	
    }
    
    public static void extrairQuantidadeSubAreaDoConhecimento( Document document ){
    	
    	List<Attribute> Atributos = document.selectNodes("//@NOME-DA-SUB-AREA-DO-CONHECIMENTO"); 
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	
    	for ( Attribute atributo : Atributos )
		{
    		if ( atributo.getValue() != "" ) {
    			String attValue = atributo.getValue();
    			if ( !map.containsKey(attValue) ){
    				map.put (atributo.getValue(), 1 );
    			}
    			else {    					
    				map.put(attValue, map.get(attValue) + 1);
    			}
    		}			
							
		}
    	
    	
    	for (String key : map.keySet()) {
	    System.out.println(key + " = " + map.get(key));
		}		
    	
    	//System.out.println(map);
    	
    }
    
    public static void extrairQuantidadePalavrasChave( Document document ){
    	
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	List<Element> Elementos = document.selectNodes("//PALAVRAS-CHAVE");     	    	
    	
    	for ( Element elemento : Elementos )
		{
			
			List<Attribute> atributos = elemento.attributes();
			
			for ( Attribute atributo : atributos ){
				if ( atributo.getValue() != "" ) {
					String attValue = atributo.getValue();
					if ( !map.containsKey(attValue) ){
						map.put (atributo.getValue(), 1 );
					}
					else {    					
						map.put(attValue, map.get(attValue) + 1);
					}
				}
				
			}							
		}
    	int count = 0;
    	for (String key : map.keySet()) {
    		if (count == 3) {  System.out.println(); count = 0;}
    	    System.out.print("["+key + " = " + map.get(key) + "] , ");
    	    count+=1;
    	}
    	
    	//System.out.println(map);    	
    }
    
    //
    
    public static void qtdTags( Document document, String findStr )
    {    	
        String text = document.asXML();
        int lastIndex = 0;
        int count = 0;

        while ((lastIndex = text.indexOf(findStr, lastIndex)) != -1) {
            count++;
            lastIndex += findStr.length() - 1;
        }

        System.out.println(count);
        
    }
    
    
    // ################ maaain #####################
    public static void main(String[] args) throws IOException {
        Document document = abrirCurriculoXML( "curriculo.xml" );
        
        //exemplo(document);       
        //exemploAreaConhecimento(document) ;
        //System.out.println(" AREAS DE ATUACAO :\n");
        //extrairQuantidadeAreaDoConhecimento2( document );
        //System.out.println("\n SUBAREAS DE ATUACAO :\n");
        //extrairQuantidadeSubAreaDoConhecimento( document );
        extrairQuantidadePalavrasChave(  document );
       //extrairMetadados( document );
        //qtdTags( document, "Data" );


    }//endMain
}
