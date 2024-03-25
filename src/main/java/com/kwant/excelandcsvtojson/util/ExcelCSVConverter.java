package com.kwant.excelandcsvtojson.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kwant.excelandcsvtojson.responsehandler.MyResponseHandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelCSVConverter {

    private ObjectMapper mapper = new ObjectMapper();

    public List<ObjectNode> excelToJsonAsListConsumingFile(MultipartFile excel)
    {
        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> excelDataList = new ArrayList<>();

        try (InputStream inputStream = excel.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();

                Iterator<Row> rowIterator = sheet.iterator();
                List<String> headers = new ArrayList<>();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    if (row.getRowNum() == 0) {
                        // Read headers
                        for (int k = 0; k < row.getLastCellNum(); k++) {
                            Cell cell = row.getCell(k);

                            if (cell != null && cell.getCellType() == CellType.STRING) {
                                headers.add(cell.getStringCellValue());
                            }

                        }
                    } else {
                        // Read data rows
                        ObjectNode rowData = mapper.createObjectNode();

                        boolean hasData = false;


                        for (int k = 0; k < headers.size(); k++) {
                            Cell cell = row.getCell(k);
                            if (cell != null) {
                                hasData=true;
                                System.out.println(hasData + " "+ k);
                                switch (cell.getCellType()) {
                                    case FORMULA:
                                        rowData.put(headers.get(k), cell.getCellFormula());
                                        break;
                                    case BOOLEAN:
                                        rowData.put(headers.get(k), cell.getBooleanCellValue());
                                        break;
                                    case NUMERIC:
                                        DataFormatter dataFormatter = new DataFormatter();
                                        String formattedValue = dataFormatter.formatCellValue(cell);
                                        rowData.put(headers.get(k), formattedValue);

                                        break;
                                    case BLANK:

                                        //    rowData.put(headers.get(k), "");

                                        break;
                                    default:
                                        rowData.put(headers.get(k), cell.getStringCellValue());
                                        break;
                                }
                            } /*else {
                                rowData.put(headers.get(k), "");
                            }*/
                        }
                        if(hasData) {
                            excelDataList.add(rowData);
                        }
                    }
                }
            }

            return excelDataList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }


    public List csvToJson(MultipartFile file){


        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CSVParser csvParser = null;
            try {
                csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<ObjectNode> jsonDataList = new ArrayList<>();

            for (CSVRecord csvRecord : csvParser) {
                ObjectNode jsonData = new ObjectMapper().createObjectNode();
                csvRecord.toMap().forEach((key, value) -> {
                    if (!value.isEmpty()) {
                        jsonData.put(key, value);
                    }
                });
                if (jsonData.size() > 0) {
                    jsonDataList.add(jsonData);
                }
            }
            return jsonDataList;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    }

