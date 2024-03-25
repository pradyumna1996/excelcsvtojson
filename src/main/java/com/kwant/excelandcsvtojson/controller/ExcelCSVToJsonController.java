package com.kwant.excelandcsvtojson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kwant.excelandcsvtojson.responsehandler.MyResponseHandler;
import com.kwant.excelandcsvtojson.service.ExcelCSVToJsonService;
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



    @PostMapping("/spreadsheets/parse/csv")
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




    //Converting excelToJson without {}

    @PostMapping("/convert/json")
    public ResponseEntity<ArrayNode> convertExcelToJson(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming only one sheet
            Iterator<Row> rowIterator = sheet.iterator();

            ArrayNode jsonArray = new ObjectMapper().createArrayNode();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                ObjectNode jsonObject = new ObjectMapper().createObjectNode();
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String header = getHeaderName(sheet, cell.getColumnIndex());
                    jsonObject.put(header, getCellValueAsString(cell));
                }

                if (jsonObject.size() > 0) {
                    jsonArray.add(jsonObject);
                }
            }

            return ResponseEntity.ok(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getHeaderName(Sheet sheet, int columnIndex) {
        Row headerRow = sheet.getRow(0);
        return headerRow.getCell(columnIndex).getStringCellValue();
    }



    //Excel TO CSV FInal

    @PostMapping("/convert")
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



    @PostMapping("/convert/updated")
    public ResponseEntity<ArrayNode> updatedConvertExcelToJson(@RequestParam("file") MultipartFile file) {

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming only one sheet
            Iterator<Row> rowIterator = sheet.iterator();

            ArrayNode jsonArray = new ObjectMapper().createArrayNode();
            Row headerRow = rowIterator.next(); // Assuming the first row is the header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                ObjectNode jsonObject = new ObjectMapper().createObjectNode();

                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        String header = headerRow.getCell(i).getStringCellValue();
                        String cellValue = getCellValueAsString(cell);
                        if (cellValue != null && !cellValue.isEmpty()) {
                            jsonObject.put(header, cellValue);
                        }
                    }
                }

                if (jsonObject.size() > 0) {
                    jsonArray.add(jsonObject);
                }
            }

            return ResponseEntity.ok(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                DataFormatter dataFormatter = new DataFormatter();
                return dataFormatter.formatCellValue(cell);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;

        }

    }

}



