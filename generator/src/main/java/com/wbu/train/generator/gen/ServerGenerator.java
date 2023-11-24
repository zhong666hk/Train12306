package com.wbu.train.generator.gen;
import com.wbu.train.generator.util.DbUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ServerGenerator {
    static boolean readOnly = true;
    static String vuePath = "admin/src/views/main/";
    static String serverPath = "[module]/src/main/java/com/wbu/train/[module]/";
    // 读取pom.xml的路径
    static String pomPath = "generator/pom.xml";
    static String module = "business";
    static {
        new File(serverPath).mkdirs();
    }

    public static void main(String[] args) throws Exception {
        // 获取mybatis-generator
        String generatorPath = getGeneratorPath();
        // 比如generator-config-member.xml，得到module = member
        module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);
        serverPath = serverPath.replace("[module]", module);
        // new File(servicePath).mkdirs();
        System.out.println("servicePath: " + serverPath);

        // 读取table节点
        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println(table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());

        // 为DbUtil设置数据源
        Node connectionURL = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        System.out.println("url: " + connectionURL.getText());
        System.out.println("user: " + userId.getText());
        System.out.println("password: " + password.getText());
        DbUtil.url = connectionURL.getText();
        DbUtil.user = userId.getText();
        DbUtil.password = password.getText();

        // 示例：表名 jiawa_test
        // Domain = JiawaTest
        String Domain = domainObjectName.getText();
        // domain = jiawaTest
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // do_main = jiawa-test
        String do_main = tableName.getText().replaceAll("_", "-");
        // 表中文名
        String tableNameCn = DbUtil.getTableComment(tableName.getText());
        List<com.wbu.train.generator.generator.util.Field> fieldList = DbUtil.getColumnByTableName(tableName.getText());
        Set<String> typeSet = getJavaTypes(fieldList);

        // 组装参数
        Map<String, Object> param = new HashMap<>();
        param.put("module", module);
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        param.put("tableNameCn", tableNameCn);
        param.put("fieldList", fieldList);
        param.put("typeSet", typeSet);
        param.put("readOnly", readOnly);
        System.out.println("组装参数：" + param);

        gen(Domain, param, "service", "service");
        gen(Domain, param, "controller/admin", "adminController");
        gen(Domain, param, "req", "saveReq");
        gen(Domain, param, "req", "queryReq");
        gen(Domain, param, "resp", "queryResp");

        genVue(do_main, param);
    }

    private static void gen(String Domain, Map<String, Object> param, String packageName, String target) throws IOException, TemplateException {
        com.wbu.train.generator.util.FreemarkerUtil.initConfig(target + ".ftl");
        String toPath = serverPath + packageName + "/";
        new File(toPath).mkdirs();
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
        String fileName = toPath + Domain + Target + ".java";
        System.out.println("开始生成：" + fileName);
        com.wbu.train.generator.util.FreemarkerUtil.generator(fileName, param);
    }

    private static void genVue(String do_main, Map<String, Object> param) throws IOException, TemplateException {
        com.wbu.train.generator.util.FreemarkerUtil.initConfig("vue.ftl");
        new File(vuePath + module).mkdirs();
        String fileName = vuePath + module + "/" + do_main + ".vue";
        System.out.println("开始生成：" + fileName);
        com.wbu.train.generator.util.FreemarkerUtil.generator(fileName, param);
    }

    // 读取pom.xml
    private static String getGeneratorPath() throws DocumentException {
        /**
         * SAXReader的方式会从xml文件从上到下依次读取其中的所有标签，但是自会读取一次，因此在此过程中注意数据的存放。
         */
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        // 读取 pom中的节点configurationFile
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }

    /**
     * 获取所有的Java类型，使用Set去重
     */
    private static Set<String> getJavaTypes(List<com.wbu.train.generator.generator.util.Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            com.wbu.train.generator.generator.util.Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }
}
