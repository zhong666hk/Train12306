package com.wbu.train.generator;

import com.wbu.train.generator.util.DbUtil;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MAIN {
    static String ftlPath = "generator/src/main/java/com/wbu/train/generator/ftl";
    static String genPath = "[model]/src/main/java/com/wbu/train/[model]/";
    static String genMapperXMLPath = "[model]/src/main/resource/";
    static final String[] FileNameArray=new String[]{"service","serviceImpl","controller"};
    static Template temp;
    public static void main(String[] args) throws Exception {
        String xmlPath = generatorXMLPath(); // src/main/resources/generator-config-passenger.xml
        String model=xmlPath.replace("src/main/resources/generator-config-","").replace(".xml","");
        System.out.println("model=  "+model); //passenger

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("generator/src/main/resources/generator-config-"+model+".xml"));
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

        genPath=genPath.replace("[model]",model);
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
        List<com.wbu.train.generator.generator.util.Field> fieldList = DbUtil.getColumnByTableName(tableName);
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

        // mapper的生成
        getFreeMarkerTemplate("mapper");
        ftlName=DoMain+"Mapper"+".java";
        generator(genPath+"mapper",ftlName,map);

        // mapper.xml的生成
        getFreeMarkerTemplate("mapper.xml");
        ftlName=DoMain+"Mapper"+".xml";
        genMapperXMLPath=genMapperXMLPath.replace("[model]",model);
        generator(genMapperXMLPath+"mapper",ftlName,map);

        // vue界面的生成
        map.put("readOnly",false);
        getFreeMarkerTemplate("vue");
        ftlName=tableName+".vue";
        generator(genPath,ftlName,map);

    }

    /**
     * 从获取的mysql类型转换成Java类型
     * @param fieldList 字段列表
     * @return
     */
    private static Set<String>  getJavaTypes(List<com.wbu.train.generator.generator.util.Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            com.wbu.train.generator.generator.util.Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
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

    //测试读取XML返回model的路径信息
    private static String generatorXMLPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read("generator/pom.xml");
        // 读取 pom中的节点configurationFile
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }
}
