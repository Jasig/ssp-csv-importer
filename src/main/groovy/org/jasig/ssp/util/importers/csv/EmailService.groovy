package org.jasig.ssp.util.importers.csv

import javax.mail.internet.*;
import javax.mail.*

//import groovy.util.AntBuilder
import static CsvImporterMeta.*
import org.apache.commons.lang.StringUtils
import groovy.util.logging.Log4j

@Log4j
class EmailService {

    static private String EMAIL_ERROR = "EMAIL ERROR: "
	//static private def ant = new AntBuilder()
	static private String toAddresses = ARGS[ARG_KEYS.EMAIL_TO_FLAG]
	static private String fromAddress = ARGS[ARG_KEYS.EMAIL_FROM_FLAG]
	static private String host = ARGS[ARG_KEYS.EMAIL_HOST_FLAG]
	static private Integer port = Integer.parseInt(ARGS[ARG_KEYS.EMAIL_PORT_FLAG].toString())
	static private String protocol = ARGS[ARG_KEYS.EMAIL_PROTOCOL_FLAG]
	static private String username = ARGS[ARG_KEYS.EMAIL_USERNAME_FLAG]
	static private String password = ARGS[ARG_KEYS.EMAIL_PASSWORD_FLAG]
	static private Boolean sslRequired = Boolean.parseBoolean(ARGS[ARG_KEYS.EMAIL_SSL_REQUIRED_FLAG].toString())
	
	static private  def getFromAddress(){
		return fromAddress
	}
	
	static private  def getToAddress(){
		return toAddresses
	}
	
	static public def notifyProcessingStarted(String fileNames){
		sendEmail("SSP IMPORT FILES PROCESSING STARTED", "Processing Started for files: \n" + fileNames )
	}
	
	static public def notifyProcessingCompleted(String fileNames){
		sendEmail("SSP IMPORT FILES PROCESSING COMPLETED", "Processing Completed for files: \n"  + fileNames)
	}
	
	static public def notifyException(exp){
		sendEmail("SSP Import threw exception on ${new Date()}", "Parsing Stopped. Exception thrown:" + exp.dump())
	}
	
	static public def notifyBeanNotUnique(msg){
		sendEmail("SSP Import tried to update non-unique bean ${new Date()}", msg)
	}
	
	static public def notifyValidationError(validationErrorMsg){
		sendEmail("SSP Import found invalid lines on ${new Date()}", "Invalid row/bean: " + validationErrorMsg)
	}
	
	static public def notifyExcessiveValidationErrors(file, validationErrorMsg){
		sendEmail("SSP Import Too Many Validation Errors found ${new Date()} for file" + file.getName(), 
		"Invalid Line: " + validationErrorMsg)
	}


    static private def sendEmail(subject, message)  {
        try{
        Session mailSession = null;
        if(sslRequired)
             mailSession = getSSLSession()
         else
            mailSession = getSession()

        MimeMessage msg = new MimeMessage(mailSession);
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddresses))

        msg.setFrom(InternetAddress.parse(toAddresses)[0])
        msg.setSubject(subject)
        msg.setText(message)
        msg.saveChanges()
        Transport transport = mailSession.getTransport(protocol)
        transport.connect (host, port, username, password)
        transport.sendMessage(msg, msg.getAllRecipients())
        transport.close()
        }catch(Exception exp){
            log.error EMAIL_ERROR + exp.getMessage()  + exp.getStackTrace()
        }
      }

    static private def getSSLSession(){

        Properties mailProps = getMailProperties()
        return Session.getDefaultInstance(mailProps);
    }

    static private def getSession(){
        Properties mailProps = getMailProperties()
        return Session.getDefaultInstance(mailProps, null)
    }

    static private def getMailProperties(){
        Properties mailProps = new Properties();
        mailProps.put("mail.smtps.auth", sslRequired.toString())
        mailProps.put("mail.smtps.starttls.enable", "true")
        return mailProps
    }
}