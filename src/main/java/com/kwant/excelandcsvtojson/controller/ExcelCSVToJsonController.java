package com.kwant.excelandcsvtojson.controller;

import com.kwant.excelandcsvtojson.responsehandler.MyResponseHandler;
import com.kwant.excelandcsvtojson.service.ExcelCSVToJsonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
public class ExcelCSVToJsonController {


    private final ExcelCSVToJsonService excelCSVToJsonService;

    public ExcelCSVToJsonController(ExcelCSVToJsonService excelCSVToJsonService) {
        this.excelCSVToJsonService = excelCSVToJsonService;
    }


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




}
