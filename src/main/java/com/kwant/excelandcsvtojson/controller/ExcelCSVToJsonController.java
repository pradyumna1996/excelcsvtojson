package com.kwant.excelandcsvtojson.controller;

import com.kwant.excelandcsvtojson.responsehandler.responsehandler.MyResponseHandler;
import com.kwant.excelandcsvtojson.service.ExcelCSVToJsonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


@RestController
public class ExcelCSVToJsonController {


    private final ExcelCSVToJsonService excelCSVToJsonService;

    public ExcelCSVToJsonController(ExcelCSVToJsonService excelCSVToJsonService) {
        this.excelCSVToJsonService = excelCSVToJsonService;
    }


    //1. Converting excel to json

    @PostMapping("/spreadsheets/parse")
    public ResponseEntity<Object> convertToJsonUploadFileCustomResponseAsListConsumingException(@RequestParam("file") MultipartFile uploadedFile){


        List<Object> data = excelCSVToJsonService.excelToJson(uploadedFile);

        System.out.println("Excel file contains the Data:\n" + data);

      /*  if(data ==null){

            throw new RuntimeException();
        }*/



        if(!(data ==null)){

            return  MyResponseHandler.generateResponse(Boolean.TRUE, HttpStatus.OK, uploadedFile.getOriginalFilename() , uploadedFile.getContentType(),  uploadedFile.getSize(), data);
        }

        return  MyResponseHandler.generateResponse(Boolean.TRUE, HttpStatus.OK, uploadedFile.getOriginalFilename() , uploadedFile.getContentType(),  uploadedFile.getSize(), data);


    }


    // 2. Converting excel to CSV
    @PostMapping("/spreadsheets/parse/csv")
    public ResponseEntity<Object> convertExcelToCsv(@RequestParam("file") MultipartFile uploadedFile) throws Exception {


        List<Object> data = excelCSVToJsonService.excelToCSV(uploadedFile);

        System.out.println("CSV file contains the Data:\n" + data);

        if(!(data ==null)){

            return  MyResponseHandler.generateResponse(Boolean.TRUE, HttpStatus.OK, uploadedFile.getOriginalFilename() , uploadedFile.getContentType(),  uploadedFile.getSize(), data);
        }

        return MyResponseHandler.generateResponse(Boolean.FALSE, HttpStatus.NO_CONTENT, uploadedFile.getOriginalFilename() , uploadedFile.getContentType(),  uploadedFile.getSize(), data);


    }


    // 3. Converting CSV TO JSON
    @PostMapping("/convert/csv/json")
    public ResponseEntity<Object>     convertToJsonUploadCSVFileCustomResponseAsListConsumingFile(@RequestPart("file") MultipartFile uploadedFile) throws Exception {

        List<Object> data = excelCSVToJsonService.csvToJson(uploadedFile);

        System.out.println("CSV file contains the Data:\n" + data);

        if (!(data == null)) {
            return MyResponseHandler.generateResponse(Boolean.TRUE, HttpStatus.OK, uploadedFile.getOriginalFilename(), uploadedFile.getContentType(), uploadedFile.getSize(), data);
        }
        return MyResponseHandler.generateResponse(Boolean.FALSE, HttpStatus.NO_CONTENT, uploadedFile.getOriginalFilename(), uploadedFile.getContentType(), uploadedFile.getSize(), data);
    }



    //4. Changing to throw custom exception




}



