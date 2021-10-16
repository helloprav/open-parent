###########################################################################################################################################
################################################ One Time Set Up ##########################################################################
###########################Database Setup Guide Taken From: https://www.youtube.com/watch?v=kaxrn_n7Jsg####################################
###########################################################################################################################################

0. To Build and Deploy Spring Boot App run (bin\ofds-maven-build.bat)
1. Download MySQL (zip format) DB from https://dev.mysql.com/downloads/mysql/
2. Unzip the mysql database in this distribution-root-directory.
3. Rename the mysql database directory to [mysql-8.0.19-winx64]

4. run the script: distribution-root-directory\bin\db-init.bat
5. Allow Access if Windows Firewall is asking for it.
6. Close the mysql console by pressing Ctrl+C 

7. open the "Windows PowerShell (Admin)" by right click "windows" or "start menu"
8. navigate to the distribution-root-directory\bin directory
9. run the script: distribution-root-directory\bin\db-service_admin.bat

###########################################################################################################################################
################################################## Start & Stop App #######################################################################
###########################################################################################################################################
A. start the windows service mysql-8.0.19-winx64 (registered in step 7,8,9 above) if not running, also make it automatic.
B. start the ofds db  (distribution-root-directory\bin\db-start.bat)
C. start the ofds app (distribution-root-directory\bin\ofds-start.bat)



## To create db triggers/ for history data
10. run the script: distribution-root-directory\bin\db-service_admin.bat
