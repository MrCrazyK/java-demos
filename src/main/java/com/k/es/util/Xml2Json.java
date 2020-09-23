package com.k.es.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by chengsheng on 2015/8/19.
 */
public class Xml2Json {
    public static void main(String[] args) throws Exception {
        String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?><DATAAREA><CHANGE_FLAG>0</CHANGE_FLAG><LAWS><LAW><CODE>07B933F0D681430B871FD368CA047F3A</CODE><NAME>中华人民共和国传染病防治法</NAME><LAW_BASIS>6</LAW_BASIS><OFFICE>全国人民代表大会常务委员会</OFFICE><LAW_NUMBER>中华人民共和国主席令第17号</LAW_NUMBER><CONTENT>第二十九条：“……饮用水供水单位从事生产或者供应活动，应当依法取得卫生许可证。”</CONTENT><FILE_ID>http://www.chinalawindex.cn/</FILE_ID></LAW><LAW><CODE>57AE3560BC9647DDA57152B1D1559394</CODE><NAME>《生活饮用水卫生监督管理办法》</NAME><LAW_BASIS>2</LAW_BASIS><OFFICE>卫生部、住房和城乡建设部</OFFICE><LAW_NUMBER>建设部、卫生部令第53号</LAW_NUMBER><CONTENT>第七条：“集中式供水单位取得工商行政管理部门颁发的营业执照后，还应当取得县级以上地方人民政府卫生计生主管部门颁发的卫生许可证，方可供水。”</CONTENT><FILE_ID>http://www.chinalawindex.cn/</FILE_ID></LAW><LAW><CODE>8C2A94AE915646DDB3C3284DD82F080B</CODE><NAME>国务院对确需保留的行政审批项目设定行政许可的决定</NAME><LAW_BASIS>5</LAW_BASIS><OFFICE>中华人民共和国国务院</OFFICE><LAW_NUMBER>中华人民共和国国务院令第412号</LAW_NUMBER><CONTENT>附件第204项：“供水单位卫生许可，实施机关：县级以上地方人民政府卫生行政主管部门。”</CONTENT><FILE_ID>http://www.chinalawindex.cn/</FILE_ID></LAW></LAWS></DATAAREA>";
        JSONObject json = Xml2Json.xml2Json(xmlStr);
        System.out.println("xml2Json:" + json.toJSONString());

    }

    public static String readFile(String path) throws Exception {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteBuffer bb = ByteBuffer.allocate(new Long(file.length()).intValue());
        //fc向buffer中读入数据
        fc.read(bb);
        bb.flip();
        String str = new String(bb.array(), "UTF8");
        fc.close();
        fis.close();
        return str;

    }

    /**
     * xml转json
     *
     * @param xmlStr
     * @return
     * @throws DocumentException
     */
    public static JSONObject xml2Json(String xmlStr) throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlStr);
        JSONObject json = new JSONObject();
        dom4j2Json(doc.getRootElement(), json);
        return json;
    }

    /**
     * xml转json
     *
     * @param element
     * @param json
     */
    public static void dom4j2Json(Element element, JSONObject json) {
        //如果是属性
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute) o;
            if (!isEmpty(attr.getValue())) {
                json.put("@" + attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl = element.elements();
        if (chdEl.isEmpty() && !isEmpty(element.getText())) {//如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }

        for (Element e : chdEl) {//有子元素
            if (!e.elements().isEmpty()) {//子元素也有子元素
                JSONObject chdjson = new JSONObject();
                dom4j2Json(e, chdjson);
                Object o = json.get(e.getName());
                if (o != null) {
                    JSONArray jsona = null;
                    if (o instanceof JSONObject) {//如果此元素已存在,则转为jsonArray
                        JSONObject jsono = (JSONObject) o;
                        json.remove(e.getName());
                        jsona = new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if (o instanceof JSONArray) {
                        jsona = (JSONArray) o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                } else {
                    if (!chdjson.isEmpty()) {
                        json.put(e.getName(), chdjson);
                    }
                }


            } else {//子元素没有子元素
                for (Object o : element.attributes()) {
                    Attribute attr = (Attribute) o;
                    if (!isEmpty(attr.getValue())) {
                        json.put("@" + attr.getName(), attr.getValue());
                    }
                }
                if (!e.getText().isEmpty()) {
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }

    public static boolean isEmpty(String str) {

        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }
}