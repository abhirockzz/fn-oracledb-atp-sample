# Fn with Oracle Autonomous Transaction Processing

Fn functions executing `CRUD` operations on Oracle ATP. This sample uses a simple `Employee` entity for demonstration purposes 

##  Oracle ATP setup

### Provision Oracle ATP

If you haven't already provisioned an Oracle ATP instance, follow the [setup guide](https://docs.oracle.com/en/cloud/paas/atp-cloud/atpug/getting-started.html#GUID-0B230036-0A05-4CA3-AF9D-97A255AE0C08)

### Get client credentials

[Download client credentials from ATP](https://docs.oracle.com/en/cloud/paas/atp-cloud/atpug/connect-download-wallet.html#GUID-B06202D2-0597-41AA-9481-3B174F75D4B1) and save it.Log into your OCI a/c and locate the Oracle ATP instance you want to work with and access it's **Service Console**

![](images/download-1.jpg)

Download the ZIP file

![](images/download-2.jpg)

### Connect to Oracle ATP

One way is using [SQL Developer](https://docs.oracle.com/en/cloud/paas/atp-cloud/atpug/connect-sql-dev182.html#GUID-14217939-3E8F-4782-BFF2-021199A908FD)

### Create a user

- [Create a user](http://www.oracle.com/webfolder/technetwork/tutorials/obe/cloud/atp/obe_provisioning%20autonomous%20transaction%20processing/provisioning_autonomous_transaction_processing.html#CreateaUserinyourAutonomousTransactionProcessingDatabase) (in addition to default `ADMIN` user created during setup)

> Replace `username` and `password` with appropriate values

   ![](images/userinput.png)
   >```
   > create user <username> identified by "<password>";
     grant dwrole to <username>;
   >```


> **Clone or download this repository before proceeding further**

### Seed database

Create `Employee` table - run `seed-db.sql` to create the table


## Application setup

### Create base docker image for (Maven) build

- `cd fn-oracledb-atp-sample/oracle_atp_docker_build`
- download `ojdbc8.jar` [from this link](https://www.oracle.com/technetwork/database/features/jdbc/jdbc-ucp-122-3110062.html) and copy it to `oracle_atp_docker_build` folder

Create Docker image

   ![](images/userinput.png)
   >```
   > docker build -t oracle-atp-base-build .
   >```


### Create base docker image for running the function
- `cd ../oracle_atp_docker_run`
- Unzip contents of your client credentials (ZIP file you downloaded earlier) in `oracle_atp_docker_run` folder

Create Docker image

   ![](images/userinput.png)
   >```
   > docker build -t oracle-atp-base-run .
   >```




Ensure you are running the latest fn cli (v0.4.153 or above) and fn server (v0.3.545 or above. If you have older version please update the CLI and the server

- To update the fn cli run the following command

   ![](images/userinput.png)
   >```
   > curl -LSs https://raw.githubusercontent.com/fnproject/cli/master/install | sh
   >```

Switch context

   ![](images/userinput.png)
   >```
   > fn use context <your context>
   >```

Log into to OCIR Docker registry - follow steps **1 to 4** in [this section of the OCIR documentation](https://docs.cloud.oracle.com/iaas/Content/Registry/Tasks/registrypushingimagesusingthedockercli.htm?tocpath=Services%7CRegistry%7C_____5)

   ![](images/userinput.png)
   >```
   > docker login <region-code>.ocir.io
   >```


Set Docker registry

   ![](images/userinput.png)
   >```
   > export FN_REGISTRY=<oci-region-code>.ocir.io/<your tenancy>/fn-oracle-atp-app
   >```

e.g. `export FN_REGISTRY=iad.ocir.io/odx-jafar/fn-oracle-atp-app`

## Create an app with required database configuration

   ![](images/userinput.png)
   >```
   > fn create app --annotation oracle.com/oci/subnetIds='["<subnet OCID>"]' --config DB_PASSWORD=<password> --config DB_URL=<db URL> --config DB_USER=<db user> --config TRUSTSTORE_PASSWORD=<truststore password> --config KEYSTORE_PASSWORD=<keystore password> --config CLIENT_CREDENTIALS=/function fn-oracle-atp-app
   >```

e.g. `fn create app --annotation oracle.com/oci/subnetIds='["ocid1.subnet.oc1.iad.aaaaaaaagsf27namkycd5l2aib33cc2nxukia6hadsbl7ww7pfd2v6bofyfq"]' --config DB_PASSWORD=Password@123 --config DB_URL=jdbc:oracle:thin:@kehsihbaoraatp_high --config DB_USER=kehsihba --config TRUSTSTORE_PASSWORD=PasswOrd_123# --config KEYSTORE_PASSWORD=PasswOrd_123# --config CLIENT_CREDENTIALS=/function fn-oracle-atp-app`


## Deploy

Deploy one function at a time. For example, to deploy the `create` function

   ![](images/userinput.png)
   >```
   > cd ../create
   >```


   ![](images/userinput.png)
   >```
   > fn -v deploy --app fn-oracle-atp-app --no-bump
   >```

For `read` function deployment

   ![](images/userinput.png)
   >```
   > cd ../read
   >```


   ![](images/userinput.png)
   >```
   > fn -v deploy --app fn-oracle-atp-app --no-bump
   >```


*Repeat for other functions i.e. `delete` and `update`*


### [TEMPORARY SOLUTION] Make the OCIR repo public

On the OCIR console, make the following Docker repositories (created by `fn deploy`) public

> Note: the OCIR Docker repository name format will be as follows - `<region>.ocir.io/<OCI tenancy name>/fn-oracle-atp-app/<function name>`

e.g.

- `iad.ocir.io/odx-jafar/fn-oracle-atp-app/read`
- `iad.ocir.io/odx-jafar/fn-oracle-atp-app/create`
- `iad.ocir.io/odx-jafar/fn-oracle-atp-app/update`
- `iad.ocir.io/odx-jafar/fn-oracle-atp-app/delete`

## Check

Run `fn inspect app fn-oracle-atp-app` to check your app (and its config). You should get back a response *similar* to one listed below


	{
	        "annotations": {
	                "oracle.com/oci/appCode": "gzs5oyynzca",
	                "oracle.com/oci/compartmentId": "ocid1.compartment.oc1..aaaaaaaaxmrampww6livwdw3usqxlrmn5fiwi3dbkwtl3waigzbwl5olu5pa",
	                "oracle.com/oci/subnetIds": [
	                        "ocid1.subnet.oc1.iad.aaaaaaaagsf27namkycd5l2aib33cc2nxukia6hadsbl7ww7pfd2v6bofyfq"
	                ],
	                "oracle.com/oci/tenantId": "ocid1.tenancy.oc1..aaaaaaaapsj3hr6pl4abnz52jm3wkgf2gfxymbeofzswhcp5jdem3fhjmkeq"
	        },
	        "config": {
	                "CLIENT_CREDENTIALS": "/function",
	                "DB_PASSWORD": "Password@123",
	                "DB_URL": "jdbc:oracle:thin:@kehsihbaoraatp_high",
	                "DB_USER": "kehsihba",
	                "KEYSTORE_PASSWORD": "PasswOrd_123#",
	                "TRUSTSTORE_PASSWORD": "PasswOrd_123#"
	        },
	        "created_at": "2018-09-10T10:35:36.000Z",
	        "id": "ocid1.fnapp.oc1.iad.aaaaaaaaahkevttusfn3tcstzqfm5ifyfp5glo53fxfkitb3mgzs5oyynzca",
	        "name": "fn-oracle-atp-app",
	        "updated_at": "2018-09-10T10:35:49.000Z"
	}



Run `fn list functions fn-oracle-atp-app` to check associated functions

	NAME    IMAGE
	create  iad.ocir.io/odx-jafar/fn-oracle-atp-app/create:0.0.1
	delete  iad.ocir.io/odx-jafar/fn-oracle-atp-app/delete:0.0.1
	read    iad.ocir.io/odx-jafar/fn-oracle-atp-app/read:0.0.1
	update  iad.ocir.io/odx-jafar/fn-oracle-atp-app/update:0.0.1



## Test

.. with Fn CLI using `fn invoke`

### Create

`echo -n '{"emp_email": "a@b.com","emp_name": "abhishek","emp_dept": "Product Divison"}' | fn invoke fn-oracle-atp-app create`

Create as many as you want

### Read

- `fn invoke fn-oracle-atp-app read` (to fetch all employees)
- `echo -n 'a@b.com' | fn invoke fn-oracle-atp-app read` (to fetch employee with email `a@b.com`)

### Update

It is possible to update the department of an employee

`echo -n '{"emp_email": "a@b.com", "emp_dept": "Support Operations"}' | fn invoke fn-oracle-atp-app update`

> check to make sure - `echo -n 'a@b.com' | fn invoke fn-oracle-atp-app read`

### Delete

Use employee email to specify which employee record you want to delete

`echo -n 'a@b.com' | fn invoke fn-oracle-atp-app /delete`

> check to make sure - `echo -n 'a@b.com' | fn invoke fn-oracle-atp-app read`
