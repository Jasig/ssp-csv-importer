package org.jasig.ssp.util.importers.csv

import org.jasig.ssp.util.importers.csv.editors.BigDecimalEditor
import org.jasig.ssp.util.importers.csv.editors.DateEditor
import org.jasig.ssp.util.importers.csv.editors.IntegerEditor
import org.jasig.ssp.model.external.*


/**
 * Created with IntelliJ IDEA.
 * User: jamesstanley
 * Date: 8/23/13
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
class CsvTableDefinition {
    /*********** Editors need for converting string values to bean values, most likely a more generic solution is available   *************/

    private static final editorsExternalPerson = ["birthDate":new DateEditor("yyyy-MM-dd"), "actualStartYear": new IntegerEditor(), "balanceOwed": new BigDecimalEditor()]
    private static final editorsExternalStudentTest = ["testDate":new DateEditor("yyyy-MM-dd"),"score": new BigDecimalEditor()]
    private static final editorsExternalStudentTranscript = ["creditHoursForGpa": new BigDecimalEditor(),
            "creditHoursAttempted": new BigDecimalEditor(),
            "totalQualityPoints": new BigDecimalEditor(),
            "gradePointAverage": new BigDecimalEditor()]

    private static final editorsExternalStudentTranscriptCourse = ["creditEarned": new BigDecimalEditor()]

    private static final editorsRegistrationStatusByTerm = ["registeredCourseCount": new IntegerEditor()]

    private static final editorsTerm = ["startDate": new DateEditor("yyyy-MM-dd"),
            "endDate": new DateEditor("yyyy-MM-dd"),
            "reportYear": new IntegerEditor()]

/*********** END EDITORS   *************/

    static def TABLES = [new TableMetaData("external_person", 	/*** external_person ***/
								"external_person.csv", 
								ExternalPerson.class, 
								["school_id"],
								editorsExternalPerson),
            new TableMetaData("external_faculty_course_roster", 		/*** external_faculty_course_roster ***/
								"external_faculty_course_roster.csv", 
								ExternalFacultyCourseRoster.class, 
								null, 
								null),
            new TableMetaData("external_faculty_course", 				/*** external_faculty_course ***/
								"external_faculty_course.csv", 
								FacultyCourse.class, 
								null, 
								null),
            new TableMetaData("external_student_test", 					/*** external_student_test ***/
								"external_student_test.csv", 
								ExternalStudentTest.class, 
								null, 
								editorsExternalStudentTest),			
            new TableMetaData("external_student_transcript_course", 	/*** external_student_transcript_course ***/
								"external_student_transcript_course.csv", 
								ExternalStudentTranscriptCourse.class, 
								null, 
								editorsExternalStudentTranscriptCourse),
            new TableMetaData("external_student_academic_program", 		/*** external_student_academic_program ***/
								"external_student_academic_program.csv", 
								ExternalStudentAcademicProgram.class, 
								null, 
								null),
            new TableMetaData("external_registration_status_by_term",   /*** external_registration_status_by_term ***/
								"external_registration_status_by_term.csv", 
								RegistrationStatusByTerm.class, 
								null, 
								editorsRegistrationStatusByTerm),
            new TableMetaData("external_student_transcript", 			/*** external_student_transcript ***/
								"external_student_transcript.csv", 
								ExternalStudentTranscript.class, 
								null, 
								editorsExternalStudentTranscript),
            new TableMetaData("external_term", 							/*** external_term ***/
								"external_term.csv", 
								Term.class, 
								null, 
								editorsTerm)]

}
