package ro.ubbcluj.cs.email_client.repository;

import org.w3c.dom.Node;

import ro.ubbcluj.cs.data.domain.Validator;
import ro.ubbcluj.cs.data.repository.AbstractCRUDRepository;
import ro.ubbcluj.cs.data.repository.AbstractXMLRepository;
import ro.ubbcluj.cs.email_client.domain.User;
import ro.ubbcluj.cs.email_client.domain.validators.UserValidator;

import java.util.NoSuchElementException;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class UserRepository extends AbstractXMLRepository<User,Integer>{

	public UserRepository(Validator<User> validator, String filename) {
		super(validator,filename);
	}
	public UserRepository() {
		super("users.xml");
	}

	@Override
	protected void setEntityId(User e, int id) {
		e.setID(id);
	}

	@Override
	protected Integer getEntityId(User e) {
		return e.getID();
	}
	
	public User findByUsername(String username) {
		try {
			return elements.stream().filter((user)->user.getUsername().equals(username)).findFirst().get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public Node child(User e) {
		Element user = doc.createElement(getTagName());
		
		Attr id = doc.createAttribute("id");
		id.setValue(String.valueOf(e.getID()));
		user.setAttributeNode(id);
		
		Element username = doc.createElement("username");
		username.appendChild(doc.createTextNode(String.valueOf(e.getUsername())));
		user.appendChild(username);

		Element password = doc.createElement("password");
		password.appendChild(doc.createTextNode(String.valueOf(e.getPassword())));
		user.appendChild(password);
		return user;
	}


	@Override
	protected User saveXmlElement(Element eElement) {
		User user=new User(eElement.getElementsByTagName("username").item(0).getTextContent(),eElement.getElementsByTagName("password").item(0).getTextContent());
		user.setID(Integer.parseInt(eElement.getAttribute("id")));
		return user;
		
	}

	@Override
	protected String getTagName() {
		return User.class.getSimpleName();
	}

	@Override
	protected boolean idIsSet(User u) {
		return u.getID()!=-1;
	}
	
}
