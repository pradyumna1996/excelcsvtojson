package com.kwant.excelandcsvtojson.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kwant.excelandcsvtojson.service.ExcelCSVToJsonService;
import com.kwant.excelandcsvtojson.util.ExcelCSVConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ExcelCSVToJsonServiceImpl implements ExcelCSVToJsonService {



    // 1. Excel To JSON
    @Override
    public List<Object> excelToJson(MultipartFile uploadedFile) {

        ExcelCSVConverter converter = new ExcelCSVConverter();

        List data = converter.excelToJson(uploadedFile);

        System.out.println("Excel file contains the Data:\n" + data);

        if(!(data==null)){
            return data;
        }

        return null;
    }


    // 2. Excel to CSV
    @Override
    public List excelToCSV(MultipartFile uploadedFile) {


        ExcelCSVConverter converter = new ExcelCSVConverter();

        List data = converter.excelToCSV(uploadedFile);

        System.out.println("Excel file contains the Data:\n" + data);

        return data;

    }


    // 3. CS TO Json
    @Override
    public List csvToJson(MultipartFile file) {

        ExcelCSVConverter converter = new ExcelCSVConverter();

        List data = converter.csvToJson(file);

        System.out.println("Excel file contains the Data:\n" + data);

        return data;

    }



}
