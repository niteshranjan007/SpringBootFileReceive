package com.file.receive.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface FileReceiveService {
	
	public ByteArrayOutputStream downloadFile(String fileName);
	public List<String> listFiles();

}
