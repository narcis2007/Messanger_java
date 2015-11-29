package messanger_server.repository.data;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import messanger_api.models.validators.Validator;


public abstract class AbstractXMLRepository<E,ID> extends AbstractCRUDRepository<E,ID> implements  FileRepository<E, ID> {

	protected String filename;
	protected Document doc;
	protected static ExecutorService executorService =Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());	
	protected Object monitor=new Object();
	
	protected AbstractXMLRepository(Validator<E> validator,String filename) {
		super(validator);
		this.filename=filename;
		executorService.submit(()->loadMemory());
		
	}
	
	public AbstractXMLRepository(String filename) {
		super();
		this.filename=filename;
		executorService.submit(()->loadMemory());
	}

	@Override
	public synchronized void updateFile() {
		try {

	        DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
	        DocumentBuilder build = dFact.newDocumentBuilder();
	        doc = build.newDocument();

	        Element root = doc.createElement(getTagName().concat("s"));
	        doc.appendChild(root);

	        
	        elements.stream().forEach((e)->root.appendChild(child(e)));

	        // Save the document to the disk file
	        TransformerFactory tranFactory = TransformerFactory.newInstance();
	        Transformer aTransformer = tranFactory.newTransformer();

	        // format the XML nicely
	        /*aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
	         */
	        aTransformer.setOutputProperty(
	                "{http://xml.apache.org/xslt}indent-amount", "4");
	        aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
	         
	        DOMSource source = new DOMSource(doc);
	        try {
	            // location and name of XML file you can change as per need
	            FileWriter fos = new FileWriter(filename);
	            StreamResult result = new StreamResult(fos);
	            aTransformer.transform(source, result);
	            if(elements.size()>0)
	            	log.info(elements.get(0).getClass().getSimpleName() + " data saved in file");

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    } catch (TransformerException ex) {
	        System.out.println("Error outputting document");

	    } catch (ParserConfigurationException ex) {
	        System.out.println("Error building document");
	    }
		
	}

	public abstract Node child(E e) ;

	@Override
	public void loadMemory() {
		try {
			
			File fXmlFile = new File(filename);
			if(fXmlFile.isFile()) { 
				
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				Element root = doc.createElement(getTagName().concat("s"));
		        doc.appendChild(root);
		        
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
	
						
				NodeList nList = doc.getElementsByTagName(getTagName());
				
				for (int temp = 0; temp < nList.getLength(); temp++) {
	
					Node nNode = nList.item(temp);
							
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						
						E e=saveXmlElement(eElement);
						super.save(e);
					}
				}
				synchronized(monitor){
					monitor.notify();
				}
				if(elements.size()>0)
					log.info(elements.get(0).getClass().getSimpleName() + " data loaded");
			}
			else{
				DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
		        DocumentBuilder build = dFact.newDocumentBuilder();
		        doc = build.newDocument();

		        Element root = doc.createElement(getTagName().concat("s"));
		        doc.appendChild(root);
		        
				TransformerFactory tranFactory = TransformerFactory.newInstance();
		        Transformer aTransformer = tranFactory.newTransformer();

		        // format the XML nicely
		        /*aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		         */
		        aTransformer.setOutputProperty(
		                "{http://xml.apache.org/xslt}indent-amount", "4");
		        aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
		         
		        DOMSource source = new DOMSource(doc);
		        
		     // location and name of XML file you can change as per need
	            FileWriter fos = new FileWriter(filename);
	            StreamResult result = new StreamResult(fos);
	            aTransformer.transform(source, result);
			}
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}

	protected abstract E saveXmlElement(Element eElement);

	protected abstract String getTagName();

	@Override
	public void setFile(String filename) {
		this.filename=filename;
	}

	@Override
	public void save(E e) {
		super.save(e);
		executorService.submit(()->updateFile());
	}

	@Override
	public boolean update(E e) {
		boolean ok= super.update(e);
		if(ok)
			executorService.submit(()->updateFile());
		return ok;
	}

	@Override
	public boolean delete(ID id) {
		boolean ok=  super.delete(id);
		if(ok)
			executorService.submit(()->updateFile());
		return ok;
	}





}
