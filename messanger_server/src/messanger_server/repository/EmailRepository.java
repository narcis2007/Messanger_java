package messanger_server.repository;

import java.util.Comparator;


import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import messanger_api.models.Email;
import messanger_api.models.Status;
import messanger_api.models.validators.Validator;
import messanger_server.repository.data.AbstractPagingAndSortingRepository;

//import ro.ubbcluj.cs.data.domain.Validator;
//import ro.ubbcluj.cs.data.repository.AbstractPagingAndSortingRepository;
//import ro.ubbcluj.cs.email_client.domain.Email;
//import ro.ubbcluj.cs.email_client.domain.Status;


public class EmailRepository extends AbstractPagingAndSortingRepository<Email,Integer>{

	
	public EmailRepository() {
		
		super(
				new Comparator<Email>(){
			@Override
			public int compare(Email e1,Email e2) {
				Integer id1=e1.getID();
				Integer id2=e2.getID();
				return Integer.compare(id2, id2);
			};},"emails.xml");
		/*setComarator(new Comparator<Email>(){
		@Override
		public int compare(Email e1,Email e2) {
			return e1.getTitle().compareTo(e2.getTitle());
		};});
		*/
	}
	
	public EmailRepository(Validator<Email> validator,String filename) {
		
		super(validator,
				new Comparator<Email>(){
			@Override
			public int compare(Email e1,Email e2) {
				Integer id1=e1.getID();
				Integer id2=e2.getID();
				return Integer.compare(id2, id2);
			};},
				filename);
		/*setComarator(new Comparator<Email>(){
		@Override
		public int compare(Email e1,Email e2) {
			return e1.getTitle().compareTo(e2.getTitle());
		};});
		*/
	}

	@Override
	protected void setEntityId(Email e, int id) {
		e.setID(id);
		
	}

	@Override
	protected Integer getEntityId(Email e) {
		return e.getID();
	}

	@Override
	protected Email saveXmlElement(Element eElement) {
		String title = eElement.getElementsByTagName("title").item(0).getTextContent();
		String content = eElement.getElementsByTagName("content").item(0).getTextContent();
		String owner = eElement.getElementsByTagName("owner").item(0).getTextContent();
		Status status = Status.valueOf(eElement.getElementsByTagName("status").item(0).getTextContent());
		String receiver=eElement.getElementsByTagName("receiver").item(0).getTextContent();
		Email email=new Email(title,content,owner);
		email.setID(Integer.parseInt(eElement.getAttribute("id")));
		email.setStatus(status);
		email.setReceiver(receiver);
		return email;
		
	}

	@Override
	protected String getTagName() {
		return Email.class.getSimpleName();
	}

	@Override
	public Node child(Email e) {
		Element email = doc.createElement(getTagName());
		
		Attr id = doc.createAttribute("id");
		id.setValue(String.valueOf(e.getID()));
		email.setAttributeNode(id);
		
		Element title = doc.createElement("title");
		title.appendChild(doc.createTextNode(String.valueOf(e.getTitle())));
		email.appendChild(title);

		Element content = doc.createElement("content");
		content.appendChild(doc.createTextNode(String.valueOf(e.getContent())));
		email.appendChild(content);
		
		Element owner = doc.createElement("owner");
		owner.appendChild(doc.createTextNode(String.valueOf(e.getOwner())));
		email.appendChild(owner);
		
		Element receiver = doc.createElement("receiver");
		receiver.appendChild(doc.createTextNode(String.valueOf(e.getReceiver())));
		email.appendChild(receiver);
		
		Element status = doc.createElement("status");
		status.appendChild(doc.createTextNode(String.valueOf(e.getStatus())));
		email.appendChild(status);
		return email;
	}

	@Override
	protected boolean idIsSet(Email e) {
		return e.getID()!=-1;
	}
	
}
