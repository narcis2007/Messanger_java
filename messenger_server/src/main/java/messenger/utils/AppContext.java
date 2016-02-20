package messenger.utils;


import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class AppContext {
    public static final String BEANS = "beans";
    public static final String BEAN = "bean";
    public static final String PROPERTY = "property";

    private static final Logger log = Logger.getLogger( AppContext.class.getSimpleName() );

    static class BeanInfo {
        Object bean;
        Map<String, String> props = new HashMap<String, String>();
    }

    private HashMap<String, BeanInfo> beans;

    public AppContext(String filename) {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        try {
            InputStream fileInputStream = new FileInputStream(filename);
            if (fileInputStream == null) {
                log.severe("App context - file not found "+filename);
                throw new ServiceException("App context file not found");
            }
            XMLStreamReader reader = factory.createXMLStreamReader(fileInputStream);
            BeanInfo currentBeanInfo = null;
            while (reader.hasNext()) {
                reader.next();
                int eventType = reader.getEventType();
                String elementName = null;
                switch (eventType) {
                    case XMLStreamReader.START_ELEMENT:
                        elementName = reader.getLocalName();
                        if (elementName.equals(BEANS)) {
                            beans = new HashMap<String, BeanInfo>();
                        } else if (elementName.equals(BEAN)) {
                            String id = reader.getAttributeValue(null, "id");
                            String className = reader.getAttributeValue(null, "class");
                            currentBeanInfo = createBean(id, className);
                        } else if (elementName.equals(PROPERTY)) {
                            String name = reader.getAttributeValue(null, "name");
                            String ref = reader.getAttributeValue(null, "ref");
                            currentBeanInfo.props.put(name, ref);
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        elementName = reader.getLocalName();
                        if (elementName.equals(BEANS)) {
                            bindBeans();
                        } else if (elementName.equals(BEAN)) {

                        } else if (elementName.equals(PROPERTY)) {

                        }
                        break;
                }
            }
            log.info("App context created");
        } catch (XMLStreamException e) {
            log.severe("App context - invalid format");
            throw new ServiceException(e);
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }

    private void bindBeans() {
        for(BeanInfo bi: beans.values()) {
            for(String fieldName : bi.props.keySet()) {
                Object fieldValue = beans.get(bi.props.get(fieldName)).bean;
                Object bean = bi.bean;
                try {
//                    Field field = bean.getClass().getDeclaredField(fieldName);
                    Field field = getFieldRec(bean.getClass(), fieldName);
                    field.setAccessible(true);
                    field.set(bean, fieldValue);
                } catch (NoSuchFieldException e) {
                    log.severe("No such field " + fieldName + " for " + bean.getClass().getName());
                    throw new ServiceException(e);
                } catch (IllegalAccessException e) {
                    log.severe("Illegal access " + fieldName);
                    throw new ServiceException(e);
                }
            }
        }
    }

    private Field getFieldRec(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() == null) {
                throw e;
            } else {
                return getFieldRec(clazz.getSuperclass(), fieldName);
            }
        }

    }

    private BeanInfo createBean(String id, String className) {
        try {
            Object bean = Class.forName(className).newInstance(); 	//MODIFIC SA FAC CU CONSTRUCTOR SI PARAMETRI - retin in map si values sau value ale parametrilor
            														//MODIFIC SI IN XML SA TRIMIT TIPUL PARAMETRULUI SI PARAMETRUL DIN CONSTRUCTOR
            //merge : bean = Class.forName("ro.ubbcluj.cs.email_client.messenger.repository.EmailRepository").getDeclaredConstructor(EmailValidator.class,String.class).newInstance(new EmailValidator(),"emails.xml");
            BeanInfo bi = new BeanInfo();
            bi.bean = bean;
            beans.put(id, bi);
            return bi;
        } catch (InstantiationException e) {
            log.severe("No default constructor for class " + className );
            throw new ServiceException(e);
        } catch (IllegalAccessException e) {
            log.severe("Illegal access");
            throw new ServiceException(e);
        } catch (ClassNotFoundException e) {
            log.severe("Class not found");
            throw new ServiceException(e);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        for (BeanInfo bi: beans.values()) {
            if (clazz.isInstance(bi.bean)) {
                return (T) bi.bean;
            }
        }
        return null; //beans.get();
    }
}

