package com.lijie.tpc.com.json;

import com.google.common.collect.Lists;
import com.lijie.tpc.com.log.Log;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * lijie2pc on 2015/3/22.
 */
public abstract class AbstractJsonFile<T> {

    private List<T> objectList;

    private Class<T> entityClass;

    private File jsonFile;

    public AbstractJsonFile(File jsonFile){
        this.jsonFile = jsonFile;
        if(jsonFile.exists()){
            ObjectMapper mapper = new ObjectMapper();
            try {
                objectList = mapper.readValue(jsonFile,
                        mapper.getTypeFactory().constructParametricType(ArrayList.class, getEntityClass()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                FileUtils.forceMkdir(jsonFile.getParentFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            objectList = Lists.newArrayList();
        }
    }


    public List<T> getObjectList(){

        return objectList;
    }

    public void setObjectList(List<T> objectList){
        this.objectList = objectList;
    }

    public void write(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(jsonFile, objectList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAndWrite(T t){
        objectList.add(t);
        write();
    }

    private Class<T> getEntityClass() {

        if (entityClass == null) {
            Type genericType = getClass().getGenericSuperclass();
            Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            entityClass = (Class<T>) actualTypes[0];
        }
        return entityClass;
    }
}
