package com.dropboxapi;

import android.os.Environment;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Joel on 2016-01-29.
 */
public class XmlWrite {


    private void XmlWright(ArrayList<XMLItem> items,String path){
        int count=0;
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("company");
            doc.appendChild(rootElement);
            while(count<items.size()) {
                // inputInfo 엘리먼트
                Element staff = doc.createElement("InputInfo");
                rootElement.appendChild(staff);
                // 속성값 정의
                Attr attr = doc.createAttribute("id");
                attr.setValue(String.valueOf(count));
                staff.setAttributeNode(attr);

                // Task 엘리먼트
                Element task = doc.createElement("task");
                task.appendChild(doc.createTextNode(items.get(count).getTask()));
                staff.appendChild(task);
                // x  엘리먼트
                Element xcoord = doc.createElement("xcoord");
                xcoord.appendChild(doc.createTextNode(items.get(count).getxInImage()));
                staff.appendChild(xcoord);
                // y 엘리먼트
                Element ycoord = doc.createElement("ycoord");
                ycoord.appendChild(doc.createTextNode(items.get(count).getyInImage()));
                staff.appendChild(ycoord);
                // event 엘리먼트
                Element event = doc.createElement("event");
                event.appendChild(doc.createTextNode(items.get(count).getEventTye()));
                staff.appendChild(event);
                // img 엘리먼트
                Element img = doc.createElement("img");
                img.appendChild(doc.createTextNode(items.get(count).getImgName()));
                staff.appendChild(img);
                // Time 전체기준 엘리먼트
                Element timeEntire = doc.createElement("timeEntire");
                timeEntire.appendChild(doc.createTextNode(items.get(count).getTimeEntire()));
                staff.appendChild(timeEntire);

                // img 엘리먼트
                Element timeImg = doc.createElement("timeImg");
                timeImg.appendChild(doc.createTextNode(items.get(count).getTimeImg()));
                staff.appendChild(timeImg);
                count++;
            }
            // XML 파일로 쓰기
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF - 8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            try {
                StreamResult result = new StreamResult(new FileOutputStream(new File(path+"UserData.xml")));

                transformer.transform(source, result);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            System.out.println("File saved!");
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (TransformerException tfe)
        {
            tfe.printStackTrace();
        }
    }

}
