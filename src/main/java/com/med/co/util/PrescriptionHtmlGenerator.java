package com.med.co.util;
import java.time.LocalDate;
import java.time.Period;

import com.med.co.dto.request.PrescriptionRequestDto;
import com.med.co.entity.Appointment;
public class PrescriptionHtmlGenerator {
	
	public static String generatePrescriptionHtml(
	        Appointment appointment,
	        PrescriptionRequestDto requestDto) {

	    String html = getTemplate();

	    int age = Period.between(
	            appointment.getPatient().getDateOfBirth(),
	            LocalDate.now()).getYears();

	    html = html.replace("{{DOCTOR_NAME}}",
	            appointment.getDoctor().getFirstName() + " "
	            + appointment.getDoctor().getLastName());

	    html = html.replace("{{SPECIALIZATION}}",
	            appointment.getDoctor().getSpecialization());

	    html = html.replace("{{HOSPITAL_NAME}}", "Health Bridge");

	    html = html.replace("{{HOSPITAL_ADDRESS}}", "Bhubaneswar, Odisha");

	    html = html.replace("{{PATIENT_NAME}}",
	            appointment.getPatient().getFirstName() + " "
	            + appointment.getPatient().getLastName());

	    html = html.replace("{{AGE}}",
	            String.valueOf(age));

	    html = html.replace("{{DATE}}",
	            appointment.getAppointmentDate().toString());

	    html = html.replace("{{MRN_NO}}",
	            appointment.getPatient().getMrnNo());

	    html = html.replace("{{ADDRESS}}",
	            appointment.getPatient().getAddress());

	    html = html.replace("{{DIAGNOSIS}}",
	            requestDto.getDiagnosis());

	    html = html.replace("{{MEDICINES}}",
	            requestDto.getMedicines());

	    html = html.replace("{{ADVICE}}",
	            requestDto.getAdvice());

	    return html;
	}
	
	 public static String getTemplate() {

	
	return """
			<!DOCTYPE html>
			<html lang="en">
			<head>
			<meta charset="UTF-8">
			<meta name="viewport" content="width=device-width, initial-scale=1.0">
			<title>Medical Prescription</title>

			<style>

			*{
			    margin:0;
			    padding:0;
			    box-sizing:border-box;
			    font-family:Arial, Helvetica, sans-serif;
			}

			body{
			    background:#e9e9e9;
			    display:flex;
			    justify-content:center;
			    padding:30px;
			}

			.prescription{
			    width:850px;
			    min-height:1100px;
			    background:#fff;
			    padding:35px 40px;
			    position:relative;
			}

			.header{
			    display:flex;
			    justify-content:space-between;
			    align-items:flex-start;
			}

			.left{
			    display:flex;
			    gap:15px;
			}

			.logo{
			    font-size:42px;
			    color:#0d6a73;
			}

			.doctor h2{
			    color:#0d6a73;
			    font-size:30px;
			}

			.doctor h4{
			    color:#0d6a73;
			    font-size:20px;
			    margin-top:5px;
			}

			.doctor p{
			    color:#666;
			    margin-top:4px;
			}

			.right{
			    text-align:right;
			}

			.right h1{
			    color:#0d6a73;
			    font-size:55px;
			}

			.line{
			    border-top:2px solid #444;
			    margin:20px 0;
			}

			.info-row{
			    display:flex;
			    justify-content:space-between;
			    margin:12px 0;
			}

			.field{
			    width:48%;
			    font-size:18px;
			}

			.field span{
			    display:inline-block;
			    border-bottom:1px solid #000;
			    min-width:220px;
			    padding-left:8px;
			}

			.rx{
			    margin-top:50px;
			    font-size:70px;
			    color:#0d6a73;
			    font-weight:bold;
			}

			.medicine-area{
			    margin-top:20px;
			    min-height:550px;
			}

			.signature{
			    width:220px;
			    text-align:center;
			    position:absolute;
			    right:50px;
			    bottom:40px;
			}

			.signature hr{
			    border:1px solid #333;
			    margin-top:5px;
			}

			.signature p{
			    margin-top:8px;
			    font-size:18px;
			}

			</style>
			</head>

			<body>

			<div class="prescription">

			<div class="header">

			<div class="left">

			<div class="logo">⚕</div>

			<div class="doctor">

			<h2>{{DOCTOR_NAME}}</h2>

			<p>{{SPECIALIZATION}}</p>

			<h4>{{HOSPITAL_NAME}}</h4>

			<p>{{HOSPITAL_ADDRESS}}</p>

			</div>

			</div>

			<div class="right">

			<h1>MED.Co</h1>

			</div>

			</div>

			<div class="line"></div>

			<div class="info-row">

			<div class="field">

			Patient Name :

			<span>{{PATIENT_NAME}}</span>

			</div>

			<div class="field">

			Age :

			<span style="min-width:80px;">{{AGE}}</span>

			</div>

			</div>

			<div class="info-row">

			<div class="field">

			Date :

			<span>{{DATE}}</span>

			</div>

			<div class="field">

			MRN No :

			<span>{{MRN_NO}}</span>

			</div>

			</div>

			<div class="info-row">

			<div class="field">

			Address :

			<span>{{ADDRESS}}</span>

			</div>

			</div>

			<div class="line"></div>

			<div class="rx">℞</div>

			<div class="medicine-area">

			<h3>Diagnosis</h3>

			<p>{{DIAGNOSIS}}</p>

			<br>

			<h3>Medicines</h3>

			<p>{{MEDICINES}}</p>

			<br>

			<h3>Advice</h3>

			<p>{{ADVICE}}</p>

			</div>

			<div class="signature">

			<hr>

			<p>Signature</p>

			</div>

			</div>

			</body>

			</html>
			""";
	 }
}
