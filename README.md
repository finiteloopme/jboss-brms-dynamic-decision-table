# -{BRMS}- Loading decision tables dynamically

## Overview
Aim of this project is to showcase how [decision tables][23af6cd1] (like Excel spreadsheets) can be changed dynamically and loaded into rules engine of BRMS to facilitate consistent decision making.

By default, the project will load the decision table located on [here on Github](https://github.com/finiteloopme/jboss-brms-dynamic-decision-table/raw/ext-decision-table/insurance-rules.xls).

Source files are located at <kbd>src/main/java</kbd>  
Test file is located at <kbd>src/test/java</kbd>

The <kbd>setup</kbd> function in `DecisionTableTest.java` performs the task of dynamically loading decision table into a [knowledge session][d4e94e58].
Test-cases are built using simple [JUnit][a7f152b6] framework.

  [a7f152b6]: http://junit.org/junit4/ "JUnit testing framework"

## Prerequisites
It is assumed that the reader has a working knowledge of the following:
1. [Maven](maven.apache.org)
2. [JBoss BRMS](https://www.redhat.com/en/technologies/jboss-middleware/business-rules)
3. Java

  [d4e94e58]: https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_BRMS/6.3/html-single/Development_Guide/index.html#sect-KieSession "KieSession"

Requirements for the execution environment:
1. Internet connection
2. [Git][a0961b3a] client
3. Maven 3.x+
4. JDK 1.7

  [a0961b3a]: https://git-scm.com/ "Git SCM"

## How to execute the demo
1. Get the source code

    ```
    git clone https://github.com/finiteloopme/jboss-brms-dynamic-decision-table
    ```
2. Execute in-built test case

    ```
    mvn clean package
    ```

## Further research
This project re-uses artecacts available as part of two below projects.  Its highly recommended that user looks into these rules projects as well.
1. https://github.com/jboss-developer/jboss-brms-repository
2. https://github.com/jboss-developer/jboss-brms-quickstarts
  [23af6cd1]: https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_BRMS/6.3/html-single/Development_Guide/index.html#sect-Using_Decision_Tables_in_Spreadsheets "Decision Tables"
