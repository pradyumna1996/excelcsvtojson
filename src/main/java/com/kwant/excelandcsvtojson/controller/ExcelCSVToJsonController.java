package com.kwant.excelandcsvtojson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kwant.excelandcsvtojson.responsehandler.MyResponseHandler;
import com.kwant.excelandcsvtojson.service.ExcelCSVToJsonService;
import com.kwant.excelandcsvtojson.util.ExcelCSVConverter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class ExcelCSVToJsonController {


    private final ExcelCSVToJsonService excelCSVToJsonService;

    public ExcelCSVToJsonController(ExcelCSVToJsonService excelCSVToJsonService) {
        this.excelCSVToJsonService = excelCSVToJsonService;
    }


    //1. Converting excel to json

    @PostMapping("/spreadsheets/parse")
    public ResponseEntity<Object> convertToJsonUploadFileCustomResponseAsListConsuming(@RequestParam("file") MultipartFile uploadedFile) throws Exception{

        if (uploadedFile == null) {
            throw new RuntimeException("You must select the a file for uploading");
        }


        InputStream inputStream = uploadedFile.getInputStream();

        System.out.println(inputStream);

        System.out.println(inputStream.read());

        String originalName = uploadedFile.getOriginalFilename();
        String name = uploadedFile.getName();
        String contentType = uploadedFile.getContentType();

        System.out.println(originalName);

        List<Object> data = excelCSVToJsonService.excelToJson(uploadedFile);

        System.out.println("Excel file contains the Data:\n" + data);

        return  MyResponseHandler.generateResponse(Boolean.TRUE, HttpStatus.OK,originalName, contentType, uploadedFile.getSize(), data);
    }



    // 2. Converting excel to CSV
    @PostMapping("/spreadsheets/parse/csv")
    public List convertExcelToCsv(@RequestParam("file") MultipartFile uploadedFile) throws Exception {

        double fileSize = uploadedFile.getSize();
        String fileName= uploadedFile.getOriginalFilename();
        String contentType = uploadedFile.getContentType();


        System.out.println(fileName);

        Reader reader = new BufferedReader(new InputStreamReader(uploadedFile.getInputStream()));

        List<Object> data = excelCSVToJsonService.excelToCSV(uploadedFile);

        System.out.println("CSV file contains the Data:\n" + data);

        if ( data!= null){
            return  data;

        }
        return  data;
    }


    // 3. Converting CSV TO JSON
    @PostMapping("/convert/csv/json")
    public ResponseEntity<Object>     convertToJsonUploadCSVFileCustomResponseAsListConsumingFile(@RequestPart("file") MultipartFile uploadedFile) throws Exception {

        double fileSize = uploadedFile.getSize();
        String fileName= uploadedFile.getOriginalFilename();
        String contentType = uploadedFile.getContentType();


        System.out.println(fileName);

        Reader reader = new BufferedReader(new InputStreamReader(uploadedFile.getInputStream()));

        List<Object> data = excelCSVToJsonService.csvToJson(uploadedFile);



        System.out.println("CSV file contains the Data:\n" + data);

        if ( data!= null){
            return  MyResponseHandler.generateResponse(Boolean.TRUE, HttpStatus.OK,fileName, contentType, uploadedFile.getSize(), data);

        }

        return  MyResponseHandler.generateResponse(Boolean.FALSE, HttpStatus.NO_CONTENT,fileName, contentType, uploadedFile.getSize(), data);
    }




}



