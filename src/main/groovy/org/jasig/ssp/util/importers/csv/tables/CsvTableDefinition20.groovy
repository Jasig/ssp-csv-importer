package org.jasig.ssp.util.importers.csv.tables

import org.jasig.ssp.model.external.*
import org.jasig.ssp.util.importers.csv.TableMetaData
import org.jasig.ssp.util.importers.csv.tables.CsvTableDefinition

class CsvTableDefinition20 extends CsvTableDefinition {

	static def constructTableDefinitions(){
		List<TableMetaData> tables = [new TableMetaData("external_course", 	/*** external_course ***/
									"external_course.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalCourse'), 
									["school_id"],
									null),
	            new TableMetaData("external_course_program", 		/*** external_course_program ***/
									"external_course_program.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalCourseProgram'), 
									null, 
									null),
	            new TableMetaData("external_course_requisite", 				/*** external_course_requisite ***/
									"external_course_requisite.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalCourseRequisite'), 
									null, 
									null),
	            new TableMetaData("external_course_tag", 					/*** external_course_tag ***/
									"external_course_tag.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalCourseTag'), 
									["school_id","test_code","sub_test_code","test_date"],
									 CsvTableDefinition.editorsExternalStudentTest),			
	            new TableMetaData("external_department", 	/*** external_department ***/
									"external_department.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalDepartment'), 
									null, 
									CsvTableDefinition.editorsExternalStudentTranscriptCourse),
	            new TableMetaData("external_division", 		/*** external_division ***/
									"external_student_academic_program.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalDivision'), 
									null,
									null),
	            new TableMetaData("external_person_note",   /*** external_person_note ***/
									"external_person_note.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalPersonNote'), 
									["school_id", "term_code"],
									CsvTableDefinition.editorsRegistrationStatusByTerm),
	            new TableMetaData("external_person_planning_status", 			/*** external_person_planning_status ***/
									"external_person_planning_status.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalPersonPlanningStatus'), 
									["school_id"],
									null),
	            new TableMetaData("external_program", 							/*** external_program ***/
									"external_program.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalProgram'), 
									["code"],
									null),
				 new TableMetaData("external_student_financial_aid", 			/*** external_student_financial_aid ***/
									"external_student_financial_aid.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalStudentFinancialAid'), 
									["code"],
									null),
				new TableMetaData("external_student_transcript_course", 			/*** external_student_transcript_course ***/
									"external_student_transcript_course.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalStudentTranscriptCourse'), 
									["code"],
									null),
				new TableMetaData("external_student_transcript_term", 			/*** external_student_transcript_term ***/
									"external_student_transcript_term.csv", 
									Class.forName('org.jasig.ssp.model.external.ExternalStudentTranscriptTerm'), 
									["code"],
									null)]
			tables.add(super.getTables())
			return tables;
	}
}
