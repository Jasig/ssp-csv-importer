ssp-csv-importer
==================


----------------------------------
- Options
----------------------------------
    --usage, --help    Prints this usage message and performs no processing

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

