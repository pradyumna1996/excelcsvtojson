package com.kwant.excelandcsvtojson.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kwant.excelandcsvtojson.service.ExcelCSVToJsonService;
import com.kwant.excelandcsvtojson.util.ExcelCSVConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelCSVToJsonServiceImpl implements ExcelCSVToJsonService {



    @Override
    public List excelToJson(MultipartFile uploadedFile) {

        ExcelCSVConverter converter = new ExcelCSVConverter();

        List<ObjectNode> data = converter.excelToJsonAsListConsumingFile(uploadedFile);

        System.out.println("Excel file contains the Data:\n" + data);

        return data;
    }

    @Override
    public List csvToJson(MultipartFile file) {

        ExcelCSVConverter converter = new ExcelCSVConverter();

        List data = converter.csvToJson(file);

        System.out.println("Excel file contains the Data:\n" + data);

        return data;

    }


}
