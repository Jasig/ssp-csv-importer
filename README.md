ssp-csv-importer
==================
Tool for importing csv data into the SSP database. Uses groovy.Sql and writes directly to the database.
Updates and Inserts are supported.  Currently, only atomic insertion is supported.  Validation is through the
use of bean annotations.

High-level workflow:

This program inserts data from csv files into a database. Csv file has some latitude in parsing including
whitespace, separator, quote, escape as well as ignore lines.  Csv files to be processed are placed in
a folder. Periodically, the program should be run to determine if the files in the input folder have not
been modified for a period of time (lag-time-min). Files are transferred to a process folder and processed.
If an archive folder is supplied files are moved to the archive folder and labeled with with modified date
and NOT PROCESSED if processing failed or did not occur.

Emails can be sent to notify of processing start, processing complete and any validation of other failures.

A config.groovy file is supplied that allows setting all parameters by env.

The expectation is that the program will be used as a cron job running periodically at an appropriate time.

Pre-requisites
==============

At a minimum you'll need a Java JDK install. This process can vary widely from
platform to platform, so you're on you're own for that one. This program will
theoretically work on JDK 1.5+, but you'll be best served by 1.6+.

Depending on how you plan to run the application you can either install Groovy
or Gradle. Gradle ships with an embedded Groovy, so you can technically avoid a
standalone Groovy install if you'd like. If you just want to clone this repo and
run the code with a minimum of fuss, Gradle is the way to go.

Gradle [download](http://www.gradle.org/downloads). The latest binary distro
should be fine. We've most recently tested with 1.4.

Gradle [install instructions](http://www.gradle.org/docs/current/userguide/installation.html).
The rest of this doc assumes you've added Gradle's `bin` directory to your PATH.

Groovy [download](http://groovy.codehaus.org/Download). The latest binary distro should be fine. The app has been
tested with 1.8.6 and that's the version Gradle will bundle with the binary
output.

Groovy [install instructions](http://groovy.codehaus.org/Installing+Groovy).
The rest of this doc assumes you've added Groovy's `bin` directory to your PATH.


Running the Program
===================

    The app can be executed in at least three different ways:

1. Gradle run command. This is the easiest way to run the app if you
are tweaking the source code:

	`%> gradle -q run -PsysProps="<opts>" -PcliArgs="<opts>"`
		
    Note that `<opts>` parsing is naive so spaces in argument values will not 
	work as expected.

2. Exploded Gradle-assembled application
     
	 `%> gradle installApp`
     `%> ./build/install/ssp-csv-importer/bin/ssp-csv-importer <opts>`

   `<opts>` splitting is handled by your shell in this case, so whitespace in argument values should work as expected.

	Use this execution mechanism if you've received a zip or tar of the
	application.

3. Without Gradle. Don't know why you'd want to do this as it is
 significantly more verbose and you'll need to do more legwork to
 make your JDBC driver visible:
       ```
         %>  groovy -cp "src/main/groovy/:src/main/resources:/path/to/your/jdbc/driver" \
            src/main/groovy/org/jasig/ssp/util/importers/csv/CsvImporterMain.groovy \
           <opts>
      ```			
    Output is a direct insert into the identified database.

    Options all have POSIX "long opt" format, i.e. --option-name=option-value.
    The "=" is optional and can be replaced with whitespace. E.g.:

    %> gradle -q run -PcliArgs="--env=prod --db-url=jdbc:postgresql://localhost:5432/ssp"

Options
=======
	
    --usage, --help    Prints this usage message and performs no processing

    - setting run environment -

    use either -PcliArgs="--env=environment" example: -PcliArgs="--env=prod" or set system environment variable env to environment

    - Database Settings -

    --db-url          [Optional] The full URL to the source database (default:jdbc:postgresql://localhost:5432/ssp_upload_test)
    --db-username     [Optional] The username for the source database  (default:sspadmin)
    --db-password     [Optional] The password for the source database. (default:sspadmin)
    --db-driver       [Optional] The driver to be used example: (default:org.postgresql.Driver) or net.sourceforge.jtds.jdbc.Driver

    - Location of input directories and validation -

    --input-dir       [Optional] full path to csv file location (default location: ./data/input/})
    --processing-dir  [Optional] full path to csv location where file transferred for processing (default location: ./data/process/)
    --archive-dir     [Optional] full path to archive dir, set blank to not store archived files  (default location: ./data/archive/)
    --lag-time-min    [Optional] set minutes file unmodified before beginning processing    (default: 10)
    --max-fail        [Optional] maximum invalid rows before a file is rejected (default: 100)
    --validate        [Optional] set true to validate rows before sending to database (default: true)

    - CSV Parsing settings -

    --column-name              [Optional] underscore or camelcase (default:underscore)
    --csv-sperator             [Optional] csv seperator (default: ,)
    --csv-quote                [Optional] csv quote (default: ")
    --csv-escape               [Optional] csv excape (default: \)
    --ignore-white-space       [Optional] csv ignore leading white space (default: true)
    --csv-default-skip-lines   [Optional] number of lines to file to skip before reading (default: 0)

    - Email settings -

     --email-active       [Optional] Activate email (default: true)
     --email-from         [Optional] address as string.  Address must follow RFC822 syntax. (default: "SSP DATA IMPORTER"<sysadmin@localhost>)
     --email-to           [Optional] Comma delimited list of addresses. Addresses must follow RFC822 syntax. (default: "SSP DATA IMPORTER"<sysadmin@localhost>)
     --email-host         [Optional] host name (default: localhost)
     --email-port         [Optional] port  (default: 25)
     --email-protocol     [Optional] protocol to be used (default: smtp)
     --email-ssl          [Optional] whether SSL/Authentication is to be used (default: true)
     --email-username     [Optional] username (default: sysadmin)
     --email-password     [Optional] password (default: ssp)

    NOTE:  All values can be set through config.groovy found at top level of distribution.  Values can be pinned to environment.