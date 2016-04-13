package com.jiao.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UpDownloadController extends HttpServlet {
	
	private static final long serialVersionUID = 5176141422652252510L;

	/**
	 * 上传文件到uploadpath 多文件上传
	 * 
	 */
	@RequestMapping(value = "upload.action")
	public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 上传的路径
		String uploadpath = "D:\\";

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1000 * 1024);
		ServletFileUpload sfu = new ServletFileUpload(factory);
		@SuppressWarnings("unchecked")
		List<FileItem> items = sfu.parseRequest(request);
		for (int i = 0; i < items.size(); i++) {
			byte[] bs = new byte[items.get(i).getInputStream().available()];
			items.get(i).getInputStream().read(bs);
			// get fileName
			String fileName = items.get(i).getName();
			System.out.println(fileName + "this is fileName");
			FileOutputStream fos = new FileOutputStream(uploadpath + fileName);
			fos.write(bs);
			fos.close();
		}
	}

	/**
	 * 下载src中的文件，String fn 是要下载的文件名字；
	 * fn文件放在scr目录下
	 */
	@RequestMapping(value = "Excel.action", method = RequestMethod.GET)
	public void download(HttpServletResponse res) {
		try {
			String fn = "/11225599.xls";
			URL url = getClass().getResource(fn);
			File f = new File(url.toURI());
			System.out.println("Loading file " + fn + "(" + f.getAbsolutePath() + ")");
			if (f.exists()) {
				res.setContentType("application/xls");
				res.setContentLength(new Long(f.length()).intValue());
				res.setHeader("Content-Disposition", "attachment; filename=Test.xls");
				FileCopyUtils.copy(new FileInputStream(f), res.getOutputStream());
			} else {
				System.out.println("File" + fn + "(" + f.getAbsolutePath() + ") does not exist");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
