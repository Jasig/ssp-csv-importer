switch (environment) {
    case 'prod':
        log4j {
            rootLogger="ERROR, file"
            appender.file="org.apache.log4j.RollingFileAppender"
            appender.'file.File'="./logs/csvimport.log"
            appender.'file.MaxFileSize'="1MB"
            appender.'file.MaxBackupIndex'="1"
            appender.'file.layout'="org.apache.log4j.PatternLayout"
            appender.'file.layout.ConversionPattern'="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"
        }
        break
    case 'test':
        log4j {
            rootLogger="INFO, stdout, file"
            appender.file="org.apache.log4j.RollingFileAppender"
            appender.'file.File'="./logs/csvimport.log"
            appender.'file.MaxFileSize'="1MB"
            appender.'file.MaxBackupIndex'="1"
            appender.'file.layout'="org.apache.log4j.PatternLayout"
            appender.'file.layout.ConversionPattern'="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"

            log4j.appender.stdout="org.apache.log4j.ConsoleAppender"
            log4j.appender.'stdout.Target'="System.out"
            log4j.appender.'stdout.layout'="org.apache.log4j.PatternLayout"
            log4j.appender.'stdout.layout.ConversionPattern'="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"
        }
        break
    default:
        log4j {
            rootLogger="INFO, stdout"
            appender.file="org.apache.log4j.RollingFileAppender"
            appender.'file.File'="./logs/csvimport.log"
            appender.'file.MaxFileSize'="1MB"
            appender.'file.MaxBackupIndex'="1"
            appender.'file.layout'="org.apache.log4j.PatternLayout"
            appender.'file.layout.ConversionPattern'="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"

            log4j.appender.stdout="org.apache.log4j.ConsoleAppender"
            log4j.appender.'stdout.Target'="System.out"
            log4j.appender.'stdout.layout'="org.apache.log4j.PatternLayout"
            log4j.appender.'stdout.layout.ConversionPattern'="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"
        }
}


