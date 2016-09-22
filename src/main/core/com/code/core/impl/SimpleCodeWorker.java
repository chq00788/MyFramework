package com.code.core.impl;

import com.code.core.CodeWoker;
import com.code.entity.TableHeadBean;
import com.sys.util.ResourceUtil;
import com.sys.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.*;

/** 单表生成器
 * 
 * @pdOid e8ced0b6-9944-47de-97ee-9fd912a35d1e */
public class SimpleCodeWorker implements CodeWoker {
    private TableHeadBean tableHeadBean;
    private Map<String,String> sourceMap;

    public SimpleCodeWorker() {
        sourceMap= ResourceUtil.getResource("code_templates/SimpleTempResource.properties");
    }

    public SimpleCodeWorker(Map<String, String> sourceMap) {
        this.sourceMap = sourceMap;

    }
    private void initBasePath() {
        try{
            Map<String,String> pathMap=ResourceUtil.getResource("code_templates/codePathConfig.properties");
            sourceMap.putAll(pathMap);
            if(StringUtils.isBlank(tableHeadBean.getJavaBasePath())){
                if(sourceMap.get("basepath_java_code")==null)
                    throw new Exception("java基本路径不能为空");
                tableHeadBean.setJavaBasePath(sourceMap.get("basepath_java_code"));
            }
            if(StringUtils.isBlank(tableHeadBean.getViewBasePath())){
                if(sourceMap.get("basepath_view")==null)
                    throw new Exception("视图基本路径不能为空");
                tableHeadBean.setViewBasePath(sourceMap.get("basepath_view"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public boolean generateEntity() {
        boolean flag=true;
        String dir=createFolder("entity");
        for(String key:sourceMap.keySet()){
            if(key.toLowerCase().startsWith("entity")){
                SimpleTemplateUtil util=new SimpleTemplateUtil();
                flag = flag && util.generateFileFormTemplate((String)sourceMap.get(key),dir+File.separator+StringUtil.toEntityName(tableHeadBean.getTableName())+"Entity.java", tableHeadBean);
            }
        }
        return flag;
    }

    @Override
    public boolean generateDao() {
        boolean flag=true;
        String dir=createFolder("dao");
        for(String key:sourceMap.keySet()){
            if(key.toLowerCase().startsWith("dao")){
                SimpleTemplateUtil util=new SimpleTemplateUtil();
                flag = flag && util.generateFileFormTemplate((String)sourceMap.get(key),dir+File.separator+StringUtil.toEntityName(tableHeadBean.getTableName())+"Dao.java", tableHeadBean);
            }
        }
        return flag;
    }

    @Override
    public boolean generateService() {
        boolean flag=true;
        String iserviceDir=createFolder("service");
        String serviceDir= createFolder("service.impl");
        for(String key:sourceMap.keySet()){
            if(key.toLowerCase().startsWith("iservice")){
                SimpleTemplateUtil util=new SimpleTemplateUtil();
                flag = flag && util.generateFileFormTemplate((String)sourceMap.get(key),iserviceDir+File.separator+StringUtil.toEntityName(tableHeadBean.getTableName())+"Service.java", tableHeadBean);
            }
            if(key.toLowerCase().startsWith("service")){
                SimpleTemplateUtil util=new SimpleTemplateUtil();
                flag = flag && util.generateFileFormTemplate((String)sourceMap.get(key),serviceDir+File.separator+StringUtil.toEntityName(tableHeadBean.getTableName())+"ServiceImpl.java", tableHeadBean);
            }
        }
        return flag;
    }

    @Override
    public boolean generateController() {
        boolean flag=true;
        String dir=createFolder("controller");
        for(String key:sourceMap.keySet()){
            if(key.toLowerCase().startsWith("controller")){
                SimpleTemplateUtil util=new SimpleTemplateUtil();
                flag = flag && util.generateFileFormTemplate((String)sourceMap.get(key),dir+File.separator+StringUtil.toEntityName(tableHeadBean.getTableName())+"Controller.java", tableHeadBean);
            }
        }
        return flag;
    }

    @Override
    public boolean generateView() {
        boolean flag=true;
        String dirPath=StringUtil.getPathByPackageName(tableHeadBean.getViewBasePath(), tableHeadBean.getViewFolder());
        File webDir=new File(dirPath);
        if(!webDir.exists())
            webDir.mkdirs();
        for(String key:sourceMap.keySet()){

                if(key.toLowerCase().startsWith("view")){
                    String name=key.toLowerCase().substring(4,key.indexOf("_template"));
                    SimpleTemplateUtil util=new SimpleTemplateUtil();
                    flag = flag && util.generateFileFormTemplate((String)sourceMap.get(key),dirPath+File.separator+tableHeadBean.getTableName()+name+".jsp", tableHeadBean);
                }


        }
        return flag;
    }

    public TableHeadBean getTableHeadBean() {
        return tableHeadBean;
    }

    public void setTableHeadBean(TableHeadBean tableHeadBean) {
        this.tableHeadBean = tableHeadBean;
        initBasePath();
    }

    public Map<String, String> getSourceMap() {
        return sourceMap;
    }

    public void setSourceMap(Map<String, String> sourceMap) {
        this.sourceMap = sourceMap;
    }

    private String createFolder(String packageType){
        String path=StringUtil.getPathByPackageName(tableHeadBean.getJavaBasePath(), tableHeadBean.getBasePackageName() + "."+packageType);
        File packageDir=new File(path);
        if(!packageDir.exists())
            packageDir.mkdirs();
        return path;
    }


}