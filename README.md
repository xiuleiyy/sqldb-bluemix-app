sqldb-bluemix-app
=================

This is a Bluemix application to show you how to use SQLDB(DB2) service


What you need?
=======================
* Bluemix ID https://ace.ng.bluemix.net
* Insall Cloud Foundry Command Line Tool http://docs.cloudfoundry.org/devguide/installcf

How to deploy sample application to Bluemix?
===============================================

* Dowload application code to your eclipse.
* Export application to sqldb-bluemix-app.war in your local machine, please change the war to another different name here, and replace your own name in below sections.
* Using below cf command to deploy sample application to Bluemix

Login: cf login -a https://api.ng.bluemix.net -u xxx@xxx.xxx -p xxxxx

Deploy: cf push sqldb-bluemix-app -p xxx\sqldb-bluemix-app.war 

After ran above two comands, congratulations, you have deployed this application to Bluemix, next I will show you how to use SQL Database service in your application.

Bind SQL Database service to application
============================================
* Log in Bluemix UI with your Bluemix ID. https://ace.ng.bluemix.net
* Click into the application you deployed in above section.
* In this application overview page, click "Add a service" button.
* In the service list, select "SQL Database" service and click the service icon.
* Follow the guide to create a SQL service instance and bind it to application.
* After you binding the service to your application, you need to re-push your application to make the service active.
* Using command "cf push sqldb-bluemix-app -p xxx\sqldb-bluemix-app.war" to repush your application

Access your application
==============================

After completed above steps, your application now is running on Bluemix with binded SQL Database service.
The application URL should like http://app_name.mybluemix.net (http://sqldb-bluemix-app.mybluemix.net/)
