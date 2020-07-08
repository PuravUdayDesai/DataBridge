package com.data.bridge.controller;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.data.bridge.aspect.FileUploaderAspect;
import com.data.bridge.connection.ConnectionProvider;
import com.data.bridge.model.FileUploadData;
import com.data.bridge.model.MemberSingUpData;
import com.data.bridge.model.MemberViewData;

@RestController
@RequestMapping("/member")
@CrossOrigin(origins = "http://localhost:8080")
public class MemberController {

	@PostMapping("/singUp")
	public ResponseEntity<Long> createMember(@RequestBody @Valid MemberSingUpData msd) throws ClassNotFoundException, SQLException, AddressException, MessagingException
	{
		Long memberId=null;
		try {
		Connection c=ConnectionProvider.getConnection();
		CallableStatement stmt=c.prepareCall("SELECT public.\"fn_createMember\"(?,?,?,?,?,?,?,?,?,?);");
		stmt.setString(1, msd.getUserName());
		stmt.setString(2, msd.getEmailId());
		stmt.setString(3, msd.getContactNumber());
		stmt.setString(4, msd.getPassword());
		stmt.setString(5, msd.getSecurityQuestion1());
		stmt.setString(6, msd.getSecurityQuestion2());
		stmt.setString(7, msd.getSecurityQuestion3());
		stmt.setString(8, msd.getDescription());
		stmt.setInt(9, msd.getPlanId());
		stmt.setTimestamp(10, msd.getCreatedOn());
		ResultSet rs=stmt.executeQuery();
		
		if(rs.next()) {
			memberId=rs.getLong("fn_createMember");
		}
		c.commit();
		rs.close();
		stmt.close();
		c.close();
		}
		catch(ClassNotFoundException e) {
			return new ResponseEntity<Long>((long)0,HttpStatus.NOT_FOUND);
		}
		catch(SQLException e) {
			System.out.println(e);
			return new ResponseEntity<Long>((long)0,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String host="smtp.gmail.com";  
	 	final String user="<EMAIL_ID>";//change accordingly  
 		final String password="<PASSWORD>";//change accordingly  
	 	Properties props = new Properties();  
 		props.put("mail.smtp.host",host);  
 		props.put("mail.smtp.starttls.enable","true");
 		props.put("mail.smtp.auth", "true");  
 		props.put("mail.smtp.port","587"); 
 		Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() 
 		{  
 	 		protected PasswordAuthentication getPasswordAuthentication()
 	 		{	  
			 		return new PasswordAuthentication(user,password);  
  	 		}  
 		});  
 		
		 MimeMessage message = new MimeMessage(session);
		 
		message.setFrom(new InternetAddress(user));
 	 	message.addRecipient(Message.RecipientType.TO,new InternetAddress(msd.getEmailId())); 
		message.setSubject("DataBridge: Congratulations Your Account has been created");
		message.setContent("Congratulations"+ msd.getUserName()+", you can login using your provided email and password.","text/plain");
		Transport.send(message);	
		return new ResponseEntity<Long>(memberId,HttpStatus.CREATED);
	}
	
	@PostMapping("/singUp/image/{memberId}")
	public ResponseEntity<Void> addMemberImage(@PathVariable Long memberId,@RequestPart("file") MultipartFile file) throws IOException, AddressException, MessagingException
	{	
		FileUploadData fud=FileUploaderAspect.fileUpload(file,"member\\image\\"+memberId);
		if(fud.getFilePath().isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		try {
		Connection c=ConnectionProvider.getConnection();
		PreparedStatement stmt=c.prepareStatement("UPDATE public.\"mainMember\" SET \"memberImageURL\"=? WHERE id=?");
		stmt.setString(1,fud.getFilePath());
		stmt.setLong(2, memberId);
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
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@GetMapping("login/{emailId}")
	public ResponseEntity<Void> checkEmailId(@PathVariable String emailId){
		Connection c;
		Boolean rsMain=false;
		try {
			c = ConnectionProvider.getConnection();
			PreparedStatement stmt=c.prepareCall("SELECT COUNT(id) FROM public.\"login\" WHERE email=?");
			stmt.setString(1, emailId);
			ResultSet rs=stmt.executeQuery();
			if(rs.next()) {
				if(rs.getLong("count")==1) {
					rsMain=true;
				}
			}
			rs.close();
			stmt.close();
			c.close();
			
		} catch (ClassNotFoundException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} catch (SQLException e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(rsMain) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("login")
	public ResponseEntity<Long> checkMemberLogin(@RequestParam("email") String email,@RequestParam("password") String password)
	{
		Boolean rsMain=false;
		Long id=null;
		try {
		Connection c=ConnectionProvider.getConnection();
		PreparedStatement stmt=c.prepareStatement("SELECT id FROM public.\"login\" WHERE email=? AND password=?");
		stmt.setString(1,email);
		stmt.setString(2, password);
		ResultSet rs=stmt.executeQuery();
		if(rs.next()) {
			Connection c2=ConnectionProvider.getConnection();
			PreparedStatement stmt2=c2.prepareStatement("SELECT id FROM public.\"mainMember\" WHERE email=?");
			stmt2.setString(1, email);
			ResultSet rs2=stmt2.executeQuery();
			if(rs2.next()) {
			id=rs2.getLong("id");
			System.out.println(id);
			}
			rs2.close();
			stmt2.close();
			c2.close();
		}
		rs.close();
		stmt.close();
		c.close();
		} catch (ClassNotFoundException e) {
			return new ResponseEntity<Long>((long)0,HttpStatus.NOT_FOUND);
		} catch (SQLException e) {
			return new ResponseEntity<Long>((long)0,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(id!=null) {
			rsMain=true;
		}
		if(rsMain) {
			return new ResponseEntity<Long>(id,HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Long>((long)0,HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping("subscribe/{memberId}/{memberIdTo}")
	public ResponseEntity<Void> subscribeMember(@PathVariable Long memberId,@PathVariable Long memberIdTo)
	{
		
		Integer downloadAllowed=null;
		
		
		try {
		Connection c=ConnectionProvider.getConnection();
		PreparedStatement stmt=c.prepareStatement("SELECT \"planId\" FROM public.\"mainMember\" WHERE id=?");
		stmt.setLong(1, memberId);
		ResultSet rs=stmt.executeQuery();
		Integer planId=null;
		if(rs.next())
		{
		planId=rs.getInt("planId");
		}
		rs.close();
		stmt.close();
		c.close();
			if(planId==1) {
				Connection c2=ConnectionProvider.getConnection();
				PreparedStatement stmt2=c2.prepareStatement("SELECT \"downloadAllowed\" FROM public.\"plans\" WHERE id=?");
				stmt2.setInt(1,planId);
				ResultSet rs2=stmt2.executeQuery();
				if(rs2.next()) {
					downloadAllowed=rs2.getInt("downloadAllowed");
				}
				rs2.close();
				stmt2.close();
				c2.close();
				Long subscriptionsMade=null;
				Connection c3=ConnectionProvider.getConnection();
				PreparedStatement stmt3=c3.prepareStatement("SELECT COUNT(id) FROM public.\"memberSubscriptions\" WHERE \"memberId\"=?;");
				stmt3.setLong(1, memberId);
				ResultSet rs3=stmt3.executeQuery();
				if(rs3.next()) {
					subscriptionsMade=rs3.getLong("count");
				}
				rs3.close();
				stmt3.close();
				c3.close();
				if(subscriptionsMade<downloadAllowed) {
					Connection cMain=ConnectionProvider.getConnection();
					PreparedStatement stmtMain=cMain.prepareStatement("INSERT INTO public.\"memberSubscriptions\"(\"memberId\",\"memberIdTo\")VALUES(?,?)");
					stmtMain.setLong(1, memberId);
					stmtMain.setLong(2, memberIdTo);
					int insertResult=stmtMain.executeUpdate();
					cMain.commit();
					stmtMain.close();
					cMain.close();
					if(insertResult!=0)
					{
						//Allow Access of all the free documents in memberTo to member
						Connection cTrans=ConnectionProvider.getConnection();
						PreparedStatement stmtTrans=cTrans.prepareStatement("SELECT id FROM public.\"memberDocument\" WHERE \"memberId\"=? AND \"economicAccess\"=0;");
						stmtTrans.setLong(1, memberIdTo);
						List<Long> documentId=new ArrayList<Long>();
						ResultSet rsTrans=stmtTrans.executeQuery();
						while(rsTrans.next()) {
							documentId.add(rsTrans.getLong("id"));
						}
						rsTrans.close();
						stmtTrans.close();
						cTrans.close();
						ListIterator<Long> li=documentId.listIterator();
						while(li.hasNext()) {
							Connection cIns=ConnectionProvider.getConnection();
							PreparedStatement stmtIns=cIns.prepareStatement("INSERT INTO public.\"memberDocumentAccess\"(\"memberId\",\"documentId\",\"paidOrFree\")VALUES(?,?,?)");
							stmtIns.setLong(1, memberId);
							stmtIns.setLong(2, li.next());
							stmtIns.setBoolean(3,false);
							int insertStatus=stmtIns.executeUpdate();
							cIns.commit();
							if(insertStatus==0) {
								return new ResponseEntity<Void>(HttpStatus.CREATED);
							}
							stmtIns.close();
							cIns.close();
						}
						return new ResponseEntity<Void>(HttpStatus.CREATED);
					}
				}
				else{
					return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
				}
				
			}
			else {
				Connection cMain=ConnectionProvider.getConnection();
				PreparedStatement stmtMain=cMain.prepareStatement("INSERT INTO public.\"memberSubscriptions\"(\"memberId\",\"memberIdTo\")VALUES(?,?)");
				
				stmtMain.setLong(1, memberId);
				stmtMain.setLong(2, memberIdTo);
				int insertResult=stmtMain.executeUpdate();
				cMain.commit();
				stmtMain.close();
				cMain.close();
				if(insertResult!=0)
				{
					//Allow Access of all the free documents in memberTo to member
					Connection cTrans=ConnectionProvider.getConnection();
					PreparedStatement stmtTrans=cTrans.prepareStatement("SELECT id FROM public.\"memberDocument\" WHERE \"memberId\"=? AND \"economicAccess\"=0;");
					stmtTrans.setLong(1, memberIdTo);
					List<Long> documentId=new ArrayList<Long>();
					ResultSet rsTrans=stmtTrans.executeQuery();
					while(rsTrans.next()) {
						documentId.add(rsTrans.getLong("id"));
					}
					rsTrans.close();
					stmtTrans.close();
					cTrans.close();
					ListIterator<Long> li=documentId.listIterator();
					while(li.hasNext()) {
						Connection cIns=ConnectionProvider.getConnection();
						PreparedStatement stmtIns=cIns.prepareStatement("INSERT INTO public.\"memberDocumentAccess\"(\"memberId\",\"documentId\",\"paidOrFree\")VALUES(?,?,?)");
						stmtIns.setLong(1, memberId);
						stmtIns.setLong(2, li.next());
						stmtIns.setBoolean(3,false);
						int insertStatus=stmtIns.executeUpdate();
						cIns.commit();
						if(insertStatus==0) {
							return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
						}
						stmtIns.close();
						cIns.close();
					}
					return new ResponseEntity<Void>(HttpStatus.CREATED);
				}
			}
		
		
		
		} catch (ClassNotFoundException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} catch (SQLException e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		
	}
	
	@GetMapping("/planId/{memberId}")
	public ResponseEntity<Integer> getMemberPlanId(@PathVariable Long memberId){
		Integer planId=null;
		try {
		Connection c=ConnectionProvider.getConnection();
		PreparedStatement stmt=c.prepareStatement("SELECT \"planId\" FROM public.\"mainMember\" WHERE \"id\"=? ");
		stmt.setLong(1, memberId);
		ResultSet rs=stmt.executeQuery();
		planId=null;
		if(rs.next()) {
			planId=rs.getInt("planId");
		}
		} catch (ClassNotFoundException e) {
			return new ResponseEntity<Integer>((int)0,HttpStatus.NOT_FOUND);
		} catch (SQLException e) {
			return new ResponseEntity<Integer>((int)0,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Integer>(planId,HttpStatus.OK);
	}

	@GetMapping("memberAccess/{memberId}")
	public ResponseEntity<List<MemberViewData>> getMemberAccessList(@PathVariable Long memberId){
		List<MemberViewData> lmd=new ArrayList<MemberViewData>();
		try {
		Connection c=ConnectionProvider.getConnection();
		PreparedStatement stmt=c.prepareStatement("SELECT id,name FROM public.\"mainMember\" WHERE id IN (SELECT \"memberIdTo\" FROM public.\"memberSubscriptions\" WHERE \"memberId\"=?);");
		stmt.setLong(1, memberId);
		ResultSet rs=stmt.executeQuery();
		while(rs.next()) {
			Long memberIdRs=rs.getLong("id");
			String memberName=rs.getString("name");
			Connection c2=ConnectionProvider.getConnection();
			PreparedStatement stmt2=c2.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocument\" WHERE \"memberId\"=?;");
			stmt2.setLong(1, memberIdRs);
			ResultSet rs2=stmt2.executeQuery();
			Long numberOfUploads=(long)0;
			if(rs2.next()) {
				numberOfUploads=rs2.getLong("count");
			}
			rs2.close();
			stmt2.close();
			c2.close();
			Connection c3=ConnectionProvider.getConnection();
			PreparedStatement stmt3=c3.prepareStatement("SELECT COUNT(id) FROM public.\"memberSubscriptions\" WHERE \"memberIdTo\"=?;");
			stmt3.setLong(1, memberIdRs);
			ResultSet rs3=stmt3.executeQuery();
			Long numberOfSubscribers=(long)0;
			if(rs3.next()) {
				numberOfSubscribers=rs3.getLong("count");
			}
			rs3.close();
			stmt3.close();
			c3.close();
			lmd.add(new MemberViewData(
									memberIdRs,
									memberName,
									numberOfUploads,
									numberOfSubscribers,
									true));
		}
		rs.close();
		stmt.close();
		c.close();
		} catch (ClassNotFoundException e) {
			return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.NOT_FOUND);
		} catch (SQLException e) {
			System.out.println(e);
			return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.OK);
	}
	
	@GetMapping("/top3/{memberId}")
	public  ResponseEntity<List<MemberViewData>> getTop3Members(@PathVariable Long memberId){
		List<MemberViewData> lmd=new ArrayList<MemberViewData>();
		try {
		Connection c=ConnectionProvider.getConnection();
		Statement stmt=c.createStatement();
		ResultSet rs=stmt.executeQuery("SELECT \"memberIdTo\" FROM public.\"memberSubscriptions\" WHERE \"memberIdTo\" IN \r\n" + 
										"(SELECT DISTINCT(\"memberIdTo\") FROM public.\"memberSubscriptions\")\r\n" + 
										"GROUP BY \"memberIdTo\"\r\n" + 
										"ORDER BY COUNT(id) DESC\r\n" + 
										"LIMIT 3;");
		while(rs.next()) {
			Long memberIdRs=rs.getLong("memberIdTo");
			Connection cName=ConnectionProvider.getConnection();
			PreparedStatement stmtName=cName.prepareCall("SELECT name FROM public.\"mainMember\" WHERE id=?;");
			stmtName.setLong(1, memberIdRs);
			ResultSet rsName=stmtName.executeQuery();
			String memberName="";
			if(rsName.next()) {
			 memberName=rsName.getString("name");
			}
			rsName.close();
			stmtName.close();
			cName.close();
			Connection c2=ConnectionProvider.getConnection();
			PreparedStatement stmt2=c2.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocument\" WHERE \"memberId\"=?;");
			stmt2.setLong(1, memberIdRs);
			ResultSet rs2=stmt2.executeQuery();
			Long numberOfUploads=(long)0;
			if(rs2.next()) {
				numberOfUploads=rs2.getLong("count");
			}
			rs2.close();
			stmt2.close();
			c2.close();
			Connection c3=ConnectionProvider.getConnection();
			PreparedStatement stmt3=c3.prepareStatement("SELECT COUNT(id) FROM public.\"memberSubscriptions\" WHERE \"memberIdTo\"=?;");
			stmt3.setLong(1, memberIdRs);
			ResultSet rs3=stmt3.executeQuery();
			Long numberOfSubscribers=(long)0;
			if(rs3.next()) {
				numberOfSubscribers=rs3.getLong("count");
			}
			rs3.close();
			stmt3.close();
			c3.close();
			Connection c4=ConnectionProvider.getConnection();
			PreparedStatement stmt4=c4.prepareStatement("SELECT COUNT(id) FROM public.\"memberSubscriptions\" WHERE \"memberId\"=? AND \"memberIdTo\"=?;");
			stmt4.setLong(1, memberId);
			stmt4.setLong(2, memberIdRs);
			ResultSet rs4=stmt4.executeQuery();
			Boolean access=false;
			if(rs4.next()) {
				if(rs4.getLong("count")!=0) {
					access=true;
				}
			}
			lmd.add(new MemberViewData(
									memberIdRs,
									memberName,
									numberOfUploads,
									numberOfSubscribers,
									access));
			
		}
		rs.close();
		stmt.close();
		c.close();

		} catch (ClassNotFoundException e) {
			return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.NOT_FOUND);
		} catch (SQLException e) {
			System.out.println(e);
			return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.OK);
	}
	
	@GetMapping("/top15/{memberId}")
	public  ResponseEntity<List<MemberViewData>> getTop15Members(@PathVariable Long memberId){
		List<MemberViewData> lmd=new ArrayList<MemberViewData>();
		try {
		Connection c=ConnectionProvider.getConnection();
		Statement stmt=c.createStatement();
		ResultSet rs=stmt.executeQuery("SELECT \"memberIdTo\" FROM public.\"memberSubscriptions\" WHERE \"memberIdTo\" IN \r\n" + 
										"(SELECT DISTINCT(\"memberIdTo\") FROM public.\"memberSubscriptions\")\r\n" + 
										"GROUP BY \"memberIdTo\"\r\n" + 
										"ORDER BY COUNT(id) DESC\r\n" + 
										"LIMIT 15;");
		while(rs.next()) {
			Long memberIdRs=rs.getLong("memberIdTo");
			Connection cName=ConnectionProvider.getConnection();
			PreparedStatement stmtName=cName.prepareCall("SELECT name FROM public.\"mainMember\" WHERE id=?;");
			stmtName.setLong(1, memberIdRs);
			ResultSet rsName=stmtName.executeQuery();
			String memberName="";
			if(rsName.next()) {
			 memberName=rsName.getString("name");
			}
			rsName.close();
			stmtName.close();
			cName.close();
			Connection c2=ConnectionProvider.getConnection();
			PreparedStatement stmt2=c2.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocument\" WHERE \"memberId\"=?;");
			stmt2.setLong(1, memberIdRs);
			ResultSet rs2=stmt2.executeQuery();
			Long numberOfUploads=(long)0;
			if(rs2.next()) {
				numberOfUploads=rs2.getLong("count");
			}
			rs2.close();
			stmt2.close();
			c2.close();
			Connection c3=ConnectionProvider.getConnection();
			PreparedStatement stmt3=c3.prepareStatement("SELECT COUNT(id) FROM public.\"memberSubscriptions\" WHERE \"memberIdTo\"=?;");
			stmt3.setLong(1, memberIdRs);
			ResultSet rs3=stmt3.executeQuery();
			Long numberOfSubscribers=(long)0;
			if(rs3.next()) {
				numberOfSubscribers=rs3.getLong("count");
			}
			rs3.close();
			stmt3.close();
			c3.close();
			Connection c4=ConnectionProvider.getConnection();
			PreparedStatement stmt4=c4.prepareStatement("SELECT COUNT(id) FROM public.\"memberSubscriptions\" WHERE \"memberId\"=? AND \"memberIdTo\"=?;");
			stmt4.setLong(1, memberId);
			stmt4.setLong(2, memberIdRs);
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
			lmd.add(new MemberViewData(
									memberIdRs,
									memberName,
									numberOfUploads,
									numberOfSubscribers,
									access));
			
		}
		rs.close();
		stmt.close();
		c.close();

		} catch (ClassNotFoundException e) {
			return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.NOT_FOUND);
		} catch (SQLException e) {
			System.out.println(e);
			return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.OK);
	}
	
	@GetMapping("/{memberId}")
	public ResponseEntity<List<MemberViewData>> getAllMembers(@PathVariable Long memberId){
		List<MemberViewData> lmd=new ArrayList<MemberViewData>();
		try {
		Connection c=ConnectionProvider.getConnection();
		PreparedStatement stmt=c.prepareStatement("SELECT id,name FROM public.\"mainMember\" WHERE \"id\"<>?;");
		stmt.setLong(1, memberId);
		ResultSet rs=stmt.executeQuery();
		while(rs.next()) {
			Long memberIdRs=rs.getLong("id");
			Connection cName=ConnectionProvider.getConnection();
			PreparedStatement stmtName=cName.prepareCall("SELECT name FROM public.\"mainMember\" WHERE id=?;");
			stmtName.setLong(1, memberIdRs);
			ResultSet rsName=stmtName.executeQuery();
			String memberName="";
			if(rsName.next()) {
			 memberName=rsName.getString("name");
			}
			rsName.close();
			stmtName.close();
			cName.close();
			Connection c2=ConnectionProvider.getConnection();
			PreparedStatement stmt2=c2.prepareStatement("SELECT COUNT(id) FROM public.\"memberDocument\" WHERE \"memberId\"=?;");
			stmt2.setLong(1, memberIdRs);
			ResultSet rs2=stmt2.executeQuery();
			Long numberOfUploads=(long)0;
			if(rs2.next()) {
				numberOfUploads=rs2.getLong("count");
			}
			rs2.close();
			stmt2.close();
			c2.close();
			Connection c3=ConnectionProvider.getConnection();
			PreparedStatement stmt3=c3.prepareStatement("SELECT COUNT(id) FROM public.\"memberSubscriptions\" WHERE \"memberIdTo\"=?;");
			stmt3.setLong(1, memberIdRs);
			ResultSet rs3=stmt3.executeQuery();
			Long numberOfSubscribers=(long)0;
			if(rs3.next()) {
				numberOfSubscribers=rs3.getLong("count");
			}
			rs3.close();
			stmt3.close();
			c3.close();
			Connection c4=ConnectionProvider.getConnection();
			PreparedStatement stmt4=c4.prepareStatement("SELECT COUNT(id) FROM public.\"memberSubscriptions\" WHERE \"memberId\"=? AND \"memberIdTo\"=?;");
			stmt4.setLong(1, memberId);
			stmt4.setLong(2, memberIdRs);
			ResultSet rs4=stmt4.executeQuery();
			Boolean access=false;
			if(rs4.next()) {
				if(rs4.getLong("count")!=0) {
					access=true;
				}
			}
			lmd.add(new MemberViewData(
									memberIdRs,
									memberName,
									numberOfUploads,
									numberOfSubscribers,
									access));
			
		}
		rs.close();
		stmt.close();
		c.close();
		} catch (ClassNotFoundException e) {
			return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.NOT_FOUND);
		} catch (SQLException e) {
			System.out.println(e);
			return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<MemberViewData>>(lmd,HttpStatus.OK);
	}
}
