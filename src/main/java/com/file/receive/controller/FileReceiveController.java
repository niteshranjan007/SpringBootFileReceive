package com.file.receive.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.receive.service.FileReceiveService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(value = "download", description = "Rest API for Downloading Files", tags = "Download API")
public class FileReceiveController {
	
	@Autowired
	FileReceiveService fileReceiveService;
	
	/*
     * Download Files
     */
	@GetMapping("/file/{fileName}")
	@ApiOperation(value = "Download File")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Downloaded"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
	public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
		ByteArrayOutputStream downloadInputStream = fileReceiveService.downloadFile(fileName);
	
		return ResponseEntity.ok()
					.contentType(contentType(fileName))
					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + fileName + "\"")
					.body(downloadInputStream.toByteArray());	
	}
	
	/*
	 * List ALL Files
	 */
	@GetMapping("/files")
	@ApiOperation(value = "View list of Files Available",produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
	public List<String> listAllFiles(){
		return fileReceiveService.listFiles();
	}
	
	private MediaType contentType(String fileName) {
		String[] arr = fileName.split("\\.");
		String type = arr[arr.length-1];
		switch(type) {
			case "txt": return MediaType.TEXT_PLAIN;
			case "png": return MediaType.IMAGE_PNG;
			case "jpg": return MediaType.IMAGE_JPEG;
			default: return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
}
