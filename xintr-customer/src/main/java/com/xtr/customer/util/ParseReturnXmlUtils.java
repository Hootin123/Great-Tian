package com.xtr.customer.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * 解析微信返回值
 * Created by admin on 2016/9/5.
 */
public class ParseReturnXmlUtils {

    /**
     * 解析微信端发送包返回的xml 获得 return_code && return_msg
     * @param strXml
     * @return
     */
    public  static String parseXml(String strXml){

        String str =null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder
                    .parse(new InputSource(new StringReader(strXml)));

            Element root = doc.getDocumentElement();
            NodeList books = root.getChildNodes();
            if (books != null) {
                 Node book1 = books.item(0);//获得第一个节点的值
                 Node book2 = books.item(1);//获得第二个节点的值
                 str =book1.getFirstChild().getNodeValue()+","+book2.getFirstChild().getNodeValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return str;
    }


    public static void main(String[] args) {
        System.out.println(parseXml("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[系统繁忙,请稍后再试.]]></return_msg></xml>"));
    }

}
