/**
 * 
 */
package com.tenpay.sm.web.schema;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;

import org.jdom.DefaultJDOMFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXHandler;
import org.jdom.output.XMLOutputter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 基于Spring annotation的pojo的web模块的schema生成
 * 生成web模块的服务描述，包含类型描述的schema和允许的操作
 * 基于JAXB实现
 * @author li.hongtl
 *
 */
public class DefaultWebModuleSchemaGenerator implements
		WebModuleSchemaGenerator {
	/**
	 * operation的xml prefix
	 */
	private String prefix = "smdl";
	/**
	 * operation的xml uri
	 */
	private String uri = "http://www.tenpay.com/sm";
	
	/**
	 * 生成smdl
	 */
	public void generator(Class klass, Writer writer) throws IOException {
		Document doc = generateSchemaDocument(klass);
		XMLOutputter output = new XMLOutputter();
		output.output(doc,writer);
	}

	/* (non-Javadoc)
	 * @see com.tenpay.sm.web.schema.WebModuleSchemaGenerator#generator(java.lang.Class, java.io.InputStream, java.io.OutputStream)
	 * 生成smdl
	 */
	public void generator(final Class klass, final OutputStream os) throws IOException {		
		Document doc = generateSchemaDocument(klass);
		XMLOutputter output = new XMLOutputter();
		output.output(doc,os);
	}
	
	/**
	 * 生成完整的smdl xml Document
	 * @param klass
	 * @return
	 * @throws IOException
	 */
	protected Document generateSchemaDocument(Class klass) throws IOException {
		Document doc = null;//new Document(new Element("definitions",prefix,uri));
		
		try {
			doc = generateTypeSchema(klass);
		} catch (JAXBException e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		
		generateOperation(klass,doc);
		
		return doc;
	}
	
	/**
	 * 生成类型描述xsd的schema xml Document
	 * @param klass
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	protected Document generateTypeSchema(final Class klass) throws JAXBException, IOException {
		final LinkedHashSet<Class> klasses = new LinkedHashSet<Class>();
		//klasses.add(klass);
		ReflectionUtils.doWithMethods(klass, new ReflectionUtils.MethodCallback() {
			public void doWith(final Method method) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					klasses.add(method.getReturnType());
					for(int i =0; i<method.getParameterTypes().length;i++) {
						klasses.add(method.getParameterTypes()[i]);
					}
				}
			}
		});
		Class[] klassesArray = new Class[klasses.size()];
		int index = 0;
		for(Class theklass : klasses) {
			klassesArray[index++] = theklass;
		}
		

		final SAXHandler ch = new SAXHandler(new DefaultJDOMFactory());
		Document doc = ch.getDocument();
		JAXBContext context = JAXBContext.newInstance(klassesArray);
		context.generateSchema(new SchemaOutputResolver() {
			public Result createOutput(String namespaceUri,
					String suggestedFileName) throws IOException {
				SAXResult result = new SAXResult();
				result.setHandler(ch);
				result.setSystemId(klass.getName());
				return result;
			}
		});
		
		return doc;
	}
	
	/**
	 * 在xml Document中加入允许的operation的描述
	 * @param klass
	 * @param doc
	 */
	protected void generateOperation(final Class klass, final Document doc) {
		ReflectionUtils.doWithMethods(klass, new ReflectionUtils.MethodCallback() {
			public void doWith(final Method method) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					RequestMapping rm = AnnotationUtils.getAnnotation(method, RequestMapping.class);
					Element input = new Element("input",prefix,uri);
					for(Class parameterType : method.getParameterTypes()) {
						input.addContent(new Element("parameter",prefix,uri)
							.setAttribute("type",ClassUtils.getShortNameAsProperty(parameterType))
						);
					}
					Element output = new Element("output",prefix,uri)
						.addContent(new Element("returnValue",prefix,uri)
							.setAttribute("type",ClassUtils.getShortNameAsProperty(method.getReturnType())
						));
					Element operation = new Element("operation",prefix,uri)
						.setAttribute("name",method.getName())
						.addContent(input)
						.addContent(output);
					
					String httpMethod = "";
					if(rm.method()!=null) {
						for(RequestMethod httpRM : rm.method()) {
							httpMethod += (httpRM.name() + ",");
						}
					}
					if(httpMethod!=null && httpMethod.length()>1) {
						operation.setAttribute("httpMethod",httpMethod.substring(0,httpMethod.length()-1));
					}
					
					String httpParameter = "";
					if(rm.params()!=null) {
						for(String param : rm.params()) {
							httpParameter += (param + ",");
						}
					}
					if(httpParameter!=null && httpParameter.length()>1) {
						operation.setAttribute("httpParameter",httpParameter.substring(0,httpParameter.length()-1));
					}
					
					if(method.isAnnotationPresent(ModelAttribute.class)) {
						ModelAttribute ma = AnnotationUtils.getAnnotation(method, ModelAttribute.class);
						if(ma.value()!=null && !"".equals(ma.value())) {
							output.setAttribute("modelAttribute",ma.value());
						}
					}
					doc.getRootElement().addContent(operation);
				}
			}
		});
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	
}
