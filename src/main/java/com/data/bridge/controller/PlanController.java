package com.data.bridge.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.data.bridge.connection.ConnectionProvider;
import com.data.bridge.model.PlanData;

@RestController
@RequestMapping("/plan")
@CrossOrigin(origins = "http://localhost:8080")
public class PlanController {

	@GetMapping
	public ResponseEntity<List<PlanData>> getPlans(){
		List<PlanData> lpd=new ArrayList<PlanData>();
		try {
		Connection c=ConnectionProvider.getConnection();
		Statement stmt=c.createStatement();
		ResultSet rs=stmt.executeQuery("SELECT * FROM public.plans");
		while(rs.next()) {
			lpd.add(new PlanData(
					rs.getLong("id"),
					rs.getString("planName"),
					rs.getString("planDescription"),
					rs.getInt("uploadAllowed"),
					rs.getInt("downloadAllowed"),
					rs.getInt("paidAllowed"),
					rs.getInt("planPrice")
					));
		}
		}
		catch(ClassNotFoundException e) {
			return new ResponseEntity<List<PlanData>>(lpd,HttpStatus.NOT_FOUND);
		}
		catch(SQLException e) {
			return new ResponseEntity<List<PlanData>>(lpd,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(lpd.isEmpty()) {
			return new ResponseEntity<List<PlanData>>(lpd,HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<PlanData>>(lpd,HttpStatus.OK);
	}
	
}
