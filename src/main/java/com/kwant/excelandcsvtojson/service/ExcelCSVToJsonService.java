package com.kwant.excelandcsvtojson.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ExcelCSVToJsonService {


    public List<Object> excelToJson(MultipartFile file);

    public List<Object> csvToJson(MultipartFile file);

    List<Object> excelToCSV(MultipartFile uploadedFile);

}
