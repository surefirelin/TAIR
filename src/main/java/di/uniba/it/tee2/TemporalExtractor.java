/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.tee2;

import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.HeidelTimeStandalone;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;
import di.uniba.it.tee2.data.TaggedText;
import di.uniba.it.tee2.data.TimeEvent;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author pierpaolo
 */
public class TemporalExtractor {

    private HeidelTimeStandalone heidelTagger;

    private static final Logger logger = Logger.getLogger(TemporalExtractor.class.getName());

    private final Language langObj;

    public TemporalExtractor(String language) {
        langObj = Language.getLanguageFromString(language);
    }

    public void init() {
        heidelTagger = new HeidelTimeStandalone(langObj, DocumentType.NARRATIVES, OutputType.TIMEML, "lib3rd/config.props");
    }

    private String escapeXML(String xmlString) throws Exception {
        int startIndex = xmlString.indexOf("<TimeML>");
        int endIndex = xmlString.indexOf("</TimeML>");
        if (startIndex >= 0 && endIndex >= 0) {
            String content = StringEscapeUtils.escapeXml11(xmlString.substring(startIndex + 8, endIndex));
            StringBuilder sb = new StringBuilder(xmlString);
            return sb.replace(startIndex + 8, endIndex, content).toString();
        } else {
            throw new Exception("No valid TimeML doc");
        }
    }

    public TaggedText process(String text) throws Exception {
        Date currentTime = Calendar.getInstance(TimeZone.getDefault()).getTime();
        TaggedText taggedText = new TaggedText();
        taggedText.setText(text);
        String timemlOutput = heidelTagger.process(text, currentTime);
        timemlOutput = escapeXML(timemlOutput);
        taggedText.setTaggedText(timemlOutput);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(timemlOutput)));

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new StringWriter());
        transformer.transform(source, result);

        //get all TIMEX3 tags in the document
        NodeList nodes = doc.getElementsByTagName("TIMEX3");

        Integer ind = 0, offset_start = 0, offset_end = 0;
        for (int i = 0; i < nodes.getLength(); i++) { //for each tag timex3
            if (("DATE").equals(nodes.item(i).getAttributes().getNamedItem("type").getNodeValue()) && nodes.item(i).getAttributes().getNamedItem("value") != null) {
                String normalizedTime = null;
                String timeValueString = nodes.item(i).getAttributes().getNamedItem("value").getNodeValue();
                try {
                    normalizedTime = TEEUtils.normalizeTime(timeValueString);
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "Error to normalize time: ", ex);
                }
                if (normalizedTime != null) {
                    offset_start = (text.indexOf(nodes.item(i).getTextContent(), ind) + 1);
                    offset_end = (text.indexOf(nodes.item(i).getTextContent(), ind) + nodes.item(i).getTextContent().length());
                    ind = text.indexOf(nodes.item(i).getTextContent(), ind) + nodes.item(i).getTextContent().length() + 1;
                    String normalizeTime = TEEUtils.normalizeTime(nodes.item(i).getAttributes().getNamedItem("value").getNodeValue());
                    TimeEvent event = new TimeEvent(offset_start, offset_end, normalizeTime);
                    event.setEventString(nodes.item(i).getTextContent());
                    taggedText.getEvents().add(event);
                }
            }
        }
        return taggedText;
    }

    public void close() {
        heidelTagger = null;
        System.gc();
    }

}
