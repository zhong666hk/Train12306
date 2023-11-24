package com.wbu.train.generator;

import com.wbu.train.generator.generator.util.Field;
import com.wbu.train.generator.util.DbUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

/**
 * SAX解析方式
 * SAX（simple API for XML）是一种XML解析的替代方法。相比于DOM，SAX是一种速度更快，更有效的方法。它逐行扫描文档，一边扫描一边解析。而且相比于DOM，SAX可以在解析文档的任意时刻停止解析。 其优缺点分别为：
 * 优点： 解析可以立即开始，速度快，没有内存压力
 * 缺点： 不能对节点做修改

 * DOM解析方式
 * DOM：(Document Object Model, 即文档对象模型) 是 W3C 组织推荐的处理 XML 的一种方式。DOM解析器在解析XML文档时，会把文档中
 * 的所有元素，按照其出现的层次关系，解析成一个个Node对象(节点)。其优缺点分别为:
 * 优点:把xml文件在内存中构造树形结构，可以遍历和修改节点
 * 缺点： 如果文件比较大，内存有压力，解析的时间会比较长

 * JDOM和DOM4J解析方式
 * JAVA操作XML文档主要有四种方式，分别是DOM、SAX、JDOM和DOM4J，DOM和SAX是官方提供的，而JDOM和DOM4J则是引用第三方库的，其中用的最多的是DOM4J方式。

 * 运行效率和内存使用方面最优的是SAX，但是由于SAX是基于事件的方式，所以SAX无法在编写XML的过程中对已编写内容进行修改，但对于不用进行频繁修改的需求，还是应该选择使用SAX。
 */


public class GeneratorTest {
    @Test
    public void TestDom4j() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("src/main/resources/generator-config-member.xml"));

        // 将读取到的文档-->变为string
        //System.out.println(document.asXML());

        // 获取节点的根节点
        Element rootElement = document.getRootElement();
        System.out.println(rootElement.getName()); //generatorConfiguration

        // 定向查找---Xpath
        Element element1 = (Element)document.selectSingleNode("//jdbcConnection[@userId='root']");
        element1.attributes().forEach(attribute -> {
            System.out.println("key="+attribute.getName()+"\tvalue="+attribute.getValue());
        });

        // 遍历 子节点
        for (Iterator i = rootElement.elementIterator();i.hasNext();){
            Element element = (Element) i.next();
            System.out.println(element.getName()); //context
        }

        // 获取---该节点下的子节点--不能是孙节点
        Element driverClassElement=rootElement.element("jdbcConnection");// "member"是节点名
        System.out.println(driverClassElement);//null

        // 遍历所有的节点
        List<Element> elements = rootElement.elements(); // 会获取到当前节点的子节点的所有信息
        elements.forEach(element -> {
            System.out.println(element.getName());
            // 获取标签的属性值-- <context id="Mysql" targetRuntime="MyBatis3" defaultModelType="flat">
            String id = element.attributeValue("id");
            System.out.println("id="+id); //Mysql

            // 获取标签元素的类容--<student>张三</student>
            String student = element.elementText("student");
            System.out.println(student);// 张三

            // 获取子标签的值
            for (Iterator i = element.elementIterator("jdbcConnection"); i.hasNext();) {
                Element elementJDBC = (Element)i.next();
                List<Attribute> attributes = elementJDBC.attributes();
                attributes.forEach(attribute -> {
                    System.out.println(attribute.getName()+"="+attribute.getValue());//attribute.getText()是获取value值
                });
            }

            List<Node> content = element.content();// 获取当前node的所有信息-->包括孙节点
            content.forEach(node -> {
                System.out.println(node.asXML());
            });
        });

    }

    /**
     * 读取 pom.xml的时候xpath失效--。必需带上nameSpace
     * @throws DocumentException
     */
    @Test
    public void TestPomXml() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);

        Document document = saxReader.read(new File("pom.xml"));
        Element rootElement = document.getRootElement();
//        System.out.println(document.asXML());
//        Node node = document.selectSingleNode("//pom:configurationFile");
        /** 绝对路径
         ./generatorConfiguration/context/jdbcConnection[@userId='root']
         相对路径
         ./generatorConfiguration/context/jdbcConnection[@userId='root']
         全文收索
         //

        */
        // 全文查找
        Node node = rootElement.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
    }


    static String ftlPath = "src/main/java/com/wbu/train/generator/ftl";
    static String genPath = "[model]/src/main/java/com/wbu/train/[model]/";
    static final String[] FileNameArray=new String[]{"service","serviceImpl","controller"};
    static Template temp;
    @Test
    public void TestService() throws Exception {
        String xmlPath = generatorXMLPath(); // src/main/resources/generator-config-passenger.xml
        String model=xmlPath.replace("src/main/resources/generator-config-","").replace(".xml","");
        System.out.println("model=  "+model); //passenger
        genPath=genPath.replace("[model]",model);

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("src/main/resources/generator-config-"+model+".xml"));
        // 获取节点的根节点
        Element rootElement = document.getRootElement();
        System.out.println(rootElement.getName()); //generatorConfiguration

        // 定向查找---Xpath
        Element element1 = (Element)document.selectSingleNode("//jdbcConnection[@userId='root']");
        // 设置数据库的链接变量
        DbUtil.password=element1.attributeValue("password");
        DbUtil.user=element1.attributeValue("userId");
        DbUtil.url=element1.attributeValue("connectionURL");

        // 读取表名字
        Element table = (Element)document.selectSingleNode("//table");
        String tableName = table.attributeValue("tableName");

        String DoMain=tableName.substring(0,1).toUpperCase()+tableName.substring(1);
        // 生成对象的的值
        HashMap<String, Object> map = new HashMap<>();
        map.put("DoMain",DoMain);
        map.put("model",model);
        map.put("doMain",tableName);


        // 创建 controller、service、serviceImpl
        for (int i = 0; i < FileNameArray.length; i++) {
            String ftlName=DoMain+FileNameArray[i].substring(0,1).toUpperCase()+FileNameArray[i].substring(1)+".java";
            // 模板名字
            System.out.println(ftlName);
            // 加载模板
            getFreeMarkerTemplate(FileNameArray[i]);
            // 生成文件
            generator(genPath+FileNameArray[i],ftlName,map);
        }

        // 实体类

        // 查询表的中文表名
        String tableComment = DbUtil.getTableComment(tableName);
        // 查询字段对应名字
        List<Field> fieldList = DbUtil.getColumnByTableName(tableName);
        // 获取import要引入的对象包
        Set<String> javaTypes = getJavaTypes(fieldList);

        // 实体类变量的添加
        map.put("tableComment",tableComment); // 表名的注释
        map.put("typeSet",javaTypes);// 获取import要引入的对象包
        map.put("fieldList",fieldList);

        getFreeMarkerTemplate("SaveReq");
        String ftlName=DoMain+"SaveReq"+".java";
        generator(genPath+"req",ftlName,map);

        getFreeMarkerTemplate("queryReq");
        ftlName=DoMain+"QueryReq"+".java";
        generator(genPath+"req",ftlName,map);

        getFreeMarkerTemplate("queryResp");
        ftlName=DoMain+"QueryResp"+".java";
        generator(genPath+"resp",ftlName,map);

        // vue界面的生成
        map.put("readOnly",false);
        getFreeMarkerTemplate("vue");
        ftlName=tableName+".vue";
        generator(genPath,ftlName,map);
    }



    /**
     * 关闭freemarker的模板、自动生成文件
     * @param fileName 文件名
     * @param path 文件路径
     * @param map 要替换的参数
     */
    public static void generator(String path,String fileName, Map<String, Object> map) throws IOException, TemplateException {
        // 判断路径是否存在-->创建路径
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }

        FileWriter fw = new FileWriter(path+"/"+fileName);
        BufferedWriter bw = new BufferedWriter(fw);
        temp.process(map, bw);
        bw.flush();
        fw.close();
    }

    // 生成freemarker的模板
    private static void getFreeMarkerTemplate(String ftlName) throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File(ftlPath));
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_31));
        temp = cfg.getTemplate(ftlName);
    }

    /**
     * 包引入
     * @param fieldList
     * @return
     */
    private static Set<String>  getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }

    //测试读取XML返回model的路径信息
    private static String generatorXMLPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);

        Document document = saxReader.read(new File("pom.xml"));
        Element rootElement = document.getRootElement();
        Node node = rootElement.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }

    /**
     * 测试读取数据库
     */
    @Test
    public void Db() throws Exception {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("src/main/resources/generator-config-member.xml"));
        // 获取节点的根节点
        Element rootElement = document.getRootElement();
        System.out.println(rootElement.getName()); //generatorConfiguration

        // 定向查找---Xpath
        Element element1 = (Element)document.selectSingleNode("//jdbcConnection[@userId='root']");
        // 设置数据库的链接变量
        DbUtil.password=element1.attributeValue("password");
        DbUtil.user=element1.attributeValue("userId");
        DbUtil.url=element1.attributeValue("connectionURL");
        // 查询表的中文表名
        String tableComment = DbUtil.getTableComment("passenger");
        // 查询字段对应名字
        List<Field> fieldList = DbUtil.getColumnByTableName("passenger");
        // 获取import要引入的对象包
        Set<String> javaTypes = getJavaTypes(fieldList);
    }
}