package com.data.bridge.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.data.bridge.aspect.FileUploaderAspect;
import com.data.bridge.connection.ConnectionProvider;
import com.data.bridge.model.DocumentDetailView;
import com.data.bridge.model.DocumentUploadDetails;
import com.data.bridge.model.DocumentView;
import com.data.bridge.model.FileUploadData;

@RestController
@RequestMapping("/document")
@CrossOrigin(origins = "http://localhost:8080")
public class DocumentController {
	
	private static String urlCreator(String filePath,String fileName) {
		//http://localhost:8080/fileDownload/view?filePath=member/documents/1/9&fileName=JSP complete reference.pdf
		String protocol="http://";
		String host="localhost";
		String portNumber="8080";
		String basePath="/fileDownload/view";
		String url=protocol+host+":"+portNumber+basePath+"?filePath="+filePath.replace("\\", "/")+"&fileName="+fileName;
		return url;
	}

	private static String getFileType(String fileType) {
		String newFileType="";
		Boolean checkIn=false;
		for(int i=0;i<fileType.length();i++) {
			
			if(checkIn) {
				newFileType+=(""+fileType.charAt(i));
			}
			if(fileType.charAt(i)=='/') {
				checkIn=true;
			}
		}
		return newFileType;
	}
	
	@PostMapping("/upload")
	public ResponseEntity<Long> uploadDocumentDetails(@RequestBody @Valid DocumentUploadDetails dud)
	{
		Long documentId=null;
		try {
		Connection c=ConnectionProvider.getConnection();
		PreparedStatement stmt=c.prepareCall("INSERT INTO public.\"memberDocument\"(\"memberId\",\"economicAccess\",\"creationDate\",\"documentName\",\"documentDescription\")VALUES (?,?,?,?,?) RETURNING id;");
		stmt.setLong(1, dud.getMemberId());
		stmt.setDouble(2, dud.getEconomicAccess());
		stmt.setTimestamp(3, dud.getCreationDate());
		stmt.setString(4, dud.getDocumentName());
		stmt.setString(5, dud.getDocumentDescription());
		ResultSet rs=stmt.executeQuery();
		c.commit();
		if(rs.next()) {
			documentId=rs.getLong("id");
		}
		if(dud.getEconomicAccess()==0) {
			Connection c2=ConnectionProvider.getConnection();
			PreparedStatement stmt2=c2.prepareStatement("SELECT \"memberId\" FROM public.\"memberSubscriptions\" WHERE \"memberIdTo\"=?;");
			stmt2.setLong(1, dud.getMemberId());
			ResultSet rs2=stmt2.executeQuery();
			List<Long> memberIds=new ArrayList<Long>();
			while(rs2.next()) {
				memberIds.add(rs2.getLong("memberId"));
			}
			rs2.close();
			stmt2.close();
			c2.close();
			ListIterator<Long> li=memberIds.listIterator();
			while(li.hasNext()) {
				Connection cMain=ConnectionProvider.getConnection();
				PreparedStatement stmtMain=cMain.prepareStatement("INSERT INTO \"memberDocumentAccess\"(\"memberId\",\"documentId\",\"paidOrFree\")VALUES(?,?,?)");
				stmtMain.setLong(1, li.next());
				stmtMain.setLong(2, documentId);
				stmtMain.setBoolean(3, false);
				int insertStatus=stmtMain.executeUpdate();
				cMain.commit();
				if(insertStatus==0) {
					stmtMain.close();
					cMain.close();
				}
				stmtMain.close();
				cMain.close();
			}
			
		}
		Connection cMain=ConnectionProvider.getConnection();
		PreparedStatement stmtMain=cMain.prepareStatement("INSERT INTO \"memberDocumentAccess\"(\"memberId\",\"documentId\",\"paidOrFree\")VALUES(?,?,?)");
		stmtMain.setLong(1,  dud.getMemberId());
		stmtMain.setLong(2, documentId);
		stmtMain.setBoolean(3, (dud.getEconomicAccess()==0?false:true));
		int insertStatus=stmtMain.executeUpdate();
		cMain.commit();
		if(insertStatus==0) {
			stmtMain.close();
			cMain.close();
		}
		stmtMain.close();
		cMain.close();
		rs.close();
		stmt.close();
		c.close();
		
		} catch (ClassNotFoundException e) {
			return new ResponseEntity<Long>((long)0,HttpStatus.NOT_FOUND);
		} catch (SQLException e) {
			System.out.println(e);
			return new ResponseEntity<Long>((long)0,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Long>(documentId,HttpStatus.CREATED);
	}
	
	@PostMapping("/upload/{memberId}/{documentId}")
	public ResponseEntity<Void> uploadDocument(@PathVariable Long memberId,@PathVariable Long documentId,@RequestPart("file") MultipartFile file) throws IOException{
		FileUploadData fud=FileUploaderAspect.fileUpload(file,"member//documents//"+memberId+"//"+documentId);
		if(fud.getFilePath().isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		String filePath=fud.getFilePath();
		Long fileSize=fud.getFileSize();
		String contentType=fud.getFileType();
		String fileURL=DocumentController.urlCreator("member//documents//"+memberId+"//"+documentId,StringUtils.cleanPath(file.getOriginalFilename()));
		try {
		Connection c=ConnectionProvider.getConnection();
		PreparedStatement stmt=c.prepareCall("UPDATE public.\"memberDocument\" SET	\"documentPath\"=?,\"fileSize\"=?,\"fileURL\"=?,\"fileType\"=? WHERE id=?;");
		stmt.setString(1,filePath);
		stmt.setLong(2,fileSize);
		stmt.setString(3, fileURL);
		stmt.setString(4, contentType);
		stmt.setLong(5,documentId);
		int updateStatus=stmt.executeUpdate();
		if(updateStatus==0)
		{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		c.commit();
		stmt.close();
		c.close();
		}
		catch(ClassNotFoundException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		catch(SQLException e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//If the document is free then add the document id in all the members who have subscribed the member uploading
		
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
		
	}
	
	@PostMapping("/buy/{memberId}/{documentId}")
	public ResponseEntity<String> buyDocument(@PathVariable Long memberId,@PathVariable Long documentId){
		try {
			Connection cPrice =ConnectionProvider.getConnection();
			PreparedStatement stmtPrice=cPrice.prepareStatement("SELECT \"economicAccess\" FROM public.\"memberDocument\" WHERE id=?");
			stmtPrice.setLong(1, documentId);
			ResultSet rsPrice=stmtPrice.executeQuery();
			Double economicAccess=0.0;
			if(rsPrice.next()) {
				economicAccess=rsPrice.getDouble("economicAccess");
			}
			rsPrice.close();
			stmtPrice.close();
			cPrice.close();
			Connection c=ConnectionProvider.getConnection();
			PreparedStatement stmt=c.prepareStatement("SELECT \"planId\" FROM \"mainMember\" WHERE id=? ");
			stmt.setLong(1, memberId);
			ResultSet rs=stmt.executeQuery();
			Long planId=(long)0;
			if(rs.next()) {
				planId=rs.getLong("planId");
			}
			rs.close();
			stmt.close();
			c.close();
			if(planId==1) {
				System.out.println("Here I have Plan as 1");
				return new ResponseEntity<String>("Sorry Your Selected Plan Doesn't permit it",HttpStatus.BAD_REQUEST);
			}
			if(planId==2) {
				//Payment
				System.out.println("Payment");
				Connection cMain=ConnectionProvider.getConnection();
				PreparedStatement stmtMain=cMain.prepareStatement("INSERT INTO \"memberDocumentAccess\"(\"memberId\",\"documentId\",\"paidOrFree\")VALUES(?,?,?)");
				stmtMain.setLong(1, memberId);
				stmtMain.setLong(2, documentId);
				stmtMain.setBoolean(3, true);
				int insertStatus=stmtMain.executeUpdate();
				cMain.commit();
				stmtMain.close();
				cMain.close();
				
				
				if(insertStatus==1) {
					System.out.println("Hey pay And Go");
					return new ResponseEntity<String>(economicAccess+" Rs. Debited from account",HttpStatus.OK);
				}
				else {
					return new ResponseEntity<String>("Sorry An Error Has Occured Please Try Later",HttpStatus.BAD_REQUEST);
				}
				
			}
			if(planId==3) {

				Connection c2=ConnectionProvider.getConnection();
				PreparedStatement stmt2=c2.prepareStatement("SELECT COUNT(id) FROM \"memberDocumentAccess\" WHERE \"memberId\"=? AND \"paidOrFree\"=true ");
				stmt2.setLong(1, memberId);
				ResultSet rs2=stmt2.executeQuery();
				Long count=(long)0;
				if(rs2.next()) {
					count=rs2.getLong("count");
				}
				rs2.close();
				stmt2.close();
				c2.close();
				if(count>4) {
					//Payment
					System.out.println("Payment");
					Connection cMain=ConnectionProvider.getConnection();
					PreparedStatement stmtMain=cMain.prepareStatement("INSERT INTO \"memberDocumentAccess\"(\"memberId\",\"documentId\",\"paidOrFree\")VALUES(?,?,?)");
					stmtMain.setLong(1, memberId);
					stmtMain.setLong(2, documentId);
					stmtMain.setBoolean(3, true);
					int insertStatus=stmtMain.executeUpdate();
					cMain.commit();
					stmtMain.close();
					cMain.close();
					if(insertStatus==1) {
						System.out.println("Hey pay And Go");
						return new ResponseEntity<String>(economicAccess+" Rs. Debited from account",HttpStatus.OK);
					}
					else {
						return new ResponseEntity<String>("Sorry An Error Has Occured Please Try Later",HttpStatus.BAD_REQUEST);
					}
				}
				else {
					Connection cMain=ConnectionProvider.getConnection();
					PreparedStatement stmtMain=cMain.prepareStatement("INSERT INTO \"memberDocumentAccess\"(\"memberId\",\"documentId\",\"paidOrFree\")VALUES(?,?,?)");
					stmtMain.setLong(1, memberId);
					stmtMain.setLong(2, documentId);
					stmtMain.setBoolean(3, true);
					int insertStatus=stmtMain.executeUpdate();
					cMain.commit();
					stmtMain.close();
					cMain.close();
					if(insertStatus==1) {
						System.out.println("Hey Don't pay And Go");
						return new ResponseEntity<String>("Access Permited",HttpStatus.OK);
					}
					else {
						return new ResponseEntity<String>("Sorry An Error Has Occured Please Try Later",HttpStatus.BAD_REQUEST);
					}
				}
			}
			
			
		}
		catch(ClassNotFoundException e) {
			return new ResponseEntity<String>("Sorry An Error Has Occured Please Try Later",HttpStatus.NOT_FOUND);
		}
		catch(SQLException e) {
			System.out.println(e);
			return new ResponseEntity<String>("Sorry An Error Has Occured Please Try Later",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Access Permited",HttpStatus.OK);
	}
	
	@GetMapping("/documentAccess/{memberId}")
	public ResponseEntity<List<DocumentView>> getMemberDocumentAccess(@PathVariable Long memberId){
		List<DocumentView> ldv=new ArrayList<DocumentView>();
		try
		{
		Connection c=ConnectionProvider.getConnection();
		PreparedStatement stmt=c.prepareStatement("SELECT id,\"documentName\",\"economicAccess\" FROM public.\"memberDocument\" WHERE id IN(SELECT \"documentId\" FROM public.\"memberDocumentAccess\" WHERE \"memberId\"=? );");
		stmt.setLong(1, memberId);
	
		ResultSet rs=stmt.executeQuery();
		while(rs.next()) {
			Long documentId=rs.getLong("id");
			String documentName=rs.getString("documentName");
			Double economicAccess=rs.getDouble("economicAccess");
			Long memberCanAccess=(long)0;
			Connection c2=ConnectionProvider.getConnection();
			PreparedStatement stmt2=c2.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocumentAccess\" WHERE \"documentId\"=?;");
			stmt2.setLong(1, documentId);
			ResultSet rs2=stmt2.executeQuery();
			if(rs2.next()) {
				memberCanAccess=rs2.getLong("count");
			}
			rs2.close();
			stmt2.close();
			c2.close();
			ldv.add(new DocumentView(
					documentId,
					documentName,
					economicAccess,
					memberCanAccess,
					true));
			
		}
		rs.close();
		stmt.close();
		c.close();
		}
		catch(ClassNotFoundException e) {
			return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.NOT_FOUND);
		}
		catch(SQLException e) {
			return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.OK);
	}
	
	@GetMapping("/bestseller/top3/{memberId}")
	public  ResponseEntity<List<DocumentView>> getTop3Bestsellers(@PathVariable Long memberId){
		List<DocumentView> ldv=new ArrayList<DocumentView>();
		try
		{
			Connection c=ConnectionProvider.getConnection();
			Statement stmt=c.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT \"documentId\" FROM public.\"memberDocumentAccess\" WHERE \"documentId\" IN \r\n" + 
					"(SELECT DISTINCT(\"documentId\") FROM public.\"memberDocumentAccess\")\r\n" + 
					"GROUP BY \"documentId\"\r\n" + 
					"ORDER BY COUNT(id) DESC\r\n" + 
					"LIMIT 3;");
			while(rs.next()) {
				Long documentId=rs.getLong("documentId");
				Connection cName=ConnectionProvider.getConnection();
				PreparedStatement stmtName=cName.prepareCall("SELECT \"documentName\",\"economicAccess\" FROM public.\"memberDocument\" WHERE \"id\"=? ");
				stmtName.setLong(1,documentId);
				String documentName="";
				Double economicAccess=(double)0.0;
				ResultSet rsName=stmtName.executeQuery();
				if(rsName.next()) {
					documentName=rsName.getString("documentName");
					economicAccess=rsName.getDouble("economicAccess");
				}
				rsName.close();
				stmtName.close();
				cName.close();
				Long memberCanAccess=(long)0;
				Connection c2=ConnectionProvider.getConnection();
				PreparedStatement stmt2=c2.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocumentAccess\" WHERE \"documentId\"=?;");
				stmt2.setLong(1, documentId);
				ResultSet rs2=stmt2.executeQuery();
				if(rs2.next()) {
					memberCanAccess=rs2.getLong("count");
				}
				rs2.close();
				stmt2.close();
				c2.close();
				
				Connection c4=ConnectionProvider.getConnection();
				PreparedStatement stmt4=c4.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocumentAccess\" WHERE \"memberId\"=? AND \"documentId\"=?;");
				stmt4.setLong(1, memberId);
				stmt4.setLong(2, documentId);
				ResultSet rs4=stmt4.executeQuery();
				Boolean access=false;
				if(rs4.next()) {
					if(rs4.getLong("count")!=0) {
						access=true;
					}
				}
				rs4.close();
				stmt4.close();
				c4.close();
				ldv.add(new DocumentView(
						documentId,
						documentName,
						economicAccess,
						memberCanAccess,
						access));
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch(ClassNotFoundException e) {
			return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.NOT_FOUND);
		}
		catch(SQLException e) {
			System.out.println(e);
			return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.OK);
		
	}
	
	@GetMapping("/bestseller/top15/{memberId}")
	public  ResponseEntity<List<DocumentView>> getTop15Bestsellers(@PathVariable Long memberId){
		List<DocumentView> ldv=new ArrayList<DocumentView>();
		try
		{
			Connection c=ConnectionProvider.getConnection();
			Statement stmt=c.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT \"documentId\" FROM public.\"memberDocumentAccess\" WHERE \"documentId\" IN \r\n" + 
					"(SELECT DISTINCT(\"documentId\") FROM public.\"memberDocumentAccess\")\r\n" + 
					"GROUP BY \"documentId\"\r\n" + 
					"ORDER BY COUNT(id) DESC\r\n" + 
					"LIMIT 15;");
			while(rs.next()) {
				Long documentId=rs.getLong("documentId");
				Connection cName=ConnectionProvider.getConnection();
				PreparedStatement stmtName=cName.prepareCall("SELECT \"documentName\",\"economicAccess\" FROM public.\"memberDocument\" WHERE \"id\"=? ");
				stmtName.setLong(1,documentId);
				String documentName="";
				Double economicAccess=(double)0.0;
				ResultSet rsName=stmtName.executeQuery();
				if(rsName.next()) {
					documentName=rsName.getString("documentName");
					economicAccess=rsName.getDouble("economicAccess");
				}
				rsName.close();
				stmtName.close();
				cName.close();
				Long memberCanAccess=(long)0;
				Connection c2=ConnectionProvider.getConnection();
				PreparedStatement stmt2=c2.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocumentAccess\" WHERE \"documentId\"=?;");
				stmt2.setLong(1, documentId);
				ResultSet rs2=stmt2.executeQuery();
				if(rs2.next()) {
					memberCanAccess=rs2.getLong("count");
				}
				rs2.close();
				stmt2.close();
				c2.close();
				
				Connection c4=ConnectionProvider.getConnection();
				PreparedStatement stmt4=c4.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocumentAccess\" WHERE \"memberId\"=? AND \"documentId\"=?;");
				stmt4.setLong(1, memberId);
				stmt4.setLong(2, documentId);
				ResultSet rs4=stmt4.executeQuery();
				Boolean access=false;
				if(rs4.next()) {
					if(rs4.getLong("count")!=0) {
						access=true;
					}
				}
				rs4.close();
				stmt4.close();
				c4.close();
				ldv.add(new DocumentView(
						documentId,
						documentName,
						economicAccess,
						memberCanAccess,
						access));
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch(ClassNotFoundException e) {
			return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.NOT_FOUND);
		}
		catch(SQLException e) {
			System.out.println(e);
			return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.OK);
		
	}
	
	@GetMapping("/{memberId}/{documentId}")
	public ResponseEntity<DocumentDetailView> getDocumentDetails(@PathVariable Long documentId,@PathVariable Long memberId){
		DocumentDetailView ddv=new DocumentDetailView();
		try {
		Connection c=ConnectionProvider.getConnection();
		PreparedStatement stmt=c.prepareStatement("SELECT id,\"memberId\",\"documentName\",\"documentDescription\",\"economicAccess\",\"fileURL\",\"fileType\",date(\"creationDate\") FROM public.\"memberDocument\" WHERE id=?;");
		stmt.setLong(1, documentId);
		ResultSet rs=stmt.executeQuery();
		Boolean access=false;
		String fileURL="http://localhost:8080/document/buy/"+memberId+"/"+documentId;
		while(rs.next()) {
			Connection c2=ConnectionProvider.getConnection();
			PreparedStatement stmt2=c2.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocumentAccess\" WHERE \"memberId\"=? AND \"documentId\"=?;");
			stmt2.setLong(1, memberId);
			stmt2.setLong(2, documentId);
			ResultSet rs2=stmt2.executeQuery();
			if(rs2.next()) {
				if(rs2.getLong("count")!=0) {
					access=true;
				}
			}
			rs2.close();
			stmt2.close();
			c2.close();
			if(!access&&rs.getDouble("economicAccess")==0.0) {
				fileURL="http://localhost:8080/member/subscribe/"+memberId+"/"+rs.getLong("memberId");
			}
			if(access) {
				fileURL=rs.getString("fileURL");
			}
		ddv=new DocumentDetailView(rs.getLong("id"),
				rs.getString("documentName"),
				rs.getString("documentDescription"),
				rs.getDouble("economicAccess"),
				rs.getDate("date"),
				getFileType(rs.getString("fileType")),
				access,
				fileURL);
		}
		rs.close();
		stmt.close();
		c.close();
		}
		catch(ClassNotFoundException e) {
			return new ResponseEntity<DocumentDetailView>(ddv,HttpStatus.NOT_FOUND);
		}
		catch(SQLException e) {
			System.out.println(e);
			return new ResponseEntity<DocumentDetailView>(ddv,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<DocumentDetailView>(ddv,HttpStatus.OK);
	}
	
	@GetMapping("/member/{memberId}/{memberIdCurrent}")
	public ResponseEntity<List<DocumentView>> getDocumentListByMemberId(@PathVariable Long memberId,@PathVariable Long memberIdCurrent){
		List<DocumentView> ldv=new ArrayList<DocumentView>();
		try
		{
			Connection c=ConnectionProvider.getConnection();
			PreparedStatement stmt=c.prepareStatement("SELECT id FROM public.\"memberDocument\" WHERE \"memberId\"=?");
			stmt.setLong(1,memberId);
			ResultSet rs=stmt.executeQuery();
			while(rs.next()) {
				Long documentId=rs.getLong("id");
				Connection cName=ConnectionProvider.getConnection();
				PreparedStatement stmtName=cName.prepareCall("SELECT \"documentName\",\"economicAccess\" FROM public.\"memberDocument\" WHERE \"id\"=? ");
				stmtName.setLong(1,documentId);
				String documentName="";
				Double economicAccess=(double)0.0;
				ResultSet rsName=stmtName.executeQuery();
				if(rsName.next()) {
					documentName=rsName.getString("documentName");
					economicAccess=rsName.getDouble("economicAccess");
				}
				rsName.close();
				stmtName.close();
				cName.close();
				Long memberCanAccess=(long)0;
				Connection c2=ConnectionProvider.getConnection();
				PreparedStatement stmt2=c2.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocumentAccess\" WHERE \"documentId\"=?;");
				stmt2.setLong(1, documentId);
				ResultSet rs2=stmt2.executeQuery();
				if(rs2.next()) {
					memberCanAccess=rs2.getLong("count");
				}
				rs2.close();
				stmt2.close();
				c2.close();
				
				Connection c4=ConnectionProvider.getConnection();
				PreparedStatement stmt4=c4.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocumentAccess\" WHERE \"memberId\"=? AND \"documentId\"=?;");
				stmt4.setLong(1, memberIdCurrent);
				stmt4.setLong(2, documentId);
				ResultSet rs4=stmt4.executeQuery();
				Boolean access=false;
				if(rs4.next()) {
					if(rs4.getLong("count")!=0) {
						access=true;
					}
				}
				rs4.close();
				stmt4.close();
				c4.close();
				ldv.add(new DocumentView(
						documentId,
						documentName,
						economicAccess,
						memberCanAccess,
						access));
			}
			rs.close();
			stmt.close();
			c.close();
		}
		catch(ClassNotFoundException e) {
			return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.NOT_FOUND);
		}
		catch(SQLException e) {
			System.out.println(e);
			return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<DocumentView>>(ldv,HttpStatus.OK);
	}
	
	
}
