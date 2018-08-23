# Fn with Oracle Autonomous Transaction Processing

## Assumptions

- Oracle ATP has been [setup](https://docs.oracle.com/en/cloud/paas/atp-cloud/atpug/getting-started.html#GUID-0B230036-0A05-4CA3-AF9D-97A255AE0C08)
- you have [created a user](http://www.oracle.com/webfolder/technetwork/tutorials/obe/cloud/atp/obe_provisioning%20autonomous%20transaction%20processing/provisioning_autonomous_transaction_processing.html#CreateaUserinyourAutonomousTransactionProcessingDatabase) (in addition to default `ADMIN` user created during setup)

## Set up

- `git clone https://github.com/abhirockzz/fn-oracledb-atp-sample`
- create base docker image for (maven) build
	- `cd fn-oracledb-atp-sample/oracle_atp_docker_build`
	- download `ojdbc8.jar` and copy it to `oracle_atp_docker_build` folder
	- create Docker image - `docker build -t oracle-atp-base-build .`
- create base docker image for running the function
	- `cd fn-oracledb-atp-sample/oracle_atp_docker_run`
	- [download client credentials from ATP](https://docs.oracle.com/en/cloud/paas/atp-cloud/atpug/connect-download-wallet.html#GUID-B06202D2-0597-41AA-9481-3B174F75D4B1) and unzip contents in `oracle_atp_docker_run` folder
	- create Docker image - `docker build -t oracle-atp-base-run .`
- create employee table
	- Connect to Oracle ATP (one way is using [SQL Developer](https://docs.oracle.com/en/cloud/paas/atp-cloud/atpug/connect-sql-dev182.html#GUID-14217939-3E8F-4782-BFF2-021199A908FD))
	- run `seed-db.sql` to create the table
- edit `func.yaml` for all the functions - populate all parameters in `config` section (except `CLIENT_CREDENTIALS` unless you have changed the location in Dockerfile for `oracle-atp-base-run` image)

## Get, set, go...

- `fn start`

Configure Docker

- `docker login` (use your docker registry credentials)
- `export FN_REGISTRY=<name of your docker repository>`

> your function docker image name will end up being - `<docker repo name>/<function name in func.yaml>:<version in func.yaml>`

Moving on....

- `cd fn-oracledb-atp-sample`
- `fn -v deploy --all` (`-v` will activate verbose mode)

> adding `--local` to `fn deploy` will build & push docker images locally (and run it from there). Remove it if you want use a dedicated/external Docker registry

All your functions (create, read, update, delete) should now be deployed. Check it using `fn inspect app fn-oracledb-atp-app` and `fn list routes fn-oracledb-atp-app`

## Test

.. with Fn CLI using `fn call`

### Create

`echo -n '{"emp_email": "a@b.com","emp_name": "abhishek","emp_dept": "Product Divison"}' | fn call fn-oracledb-atp-app /create`

Create as many as you want

### Read

- `fn call fn-oracledb-atp-app /read` (to fetch all employees)
- `echo a@b.com | fn call fn-oracledb-atp-app /read` (to fetch employee with email `a@b.com`)

### Update

It is possible to update the department of an employee

`echo -n '{"emp_email": "a@b.com", "emp_dept": "Support Operations"}' | fn call fn-oracledb-atp-app /update`

> check to make sure - `echo a@b.com | fn call fn-oradb-java-app /read`

### Delete

Use employee email to specify which employee record you want to delete

`echo -n a@b.com | fn call fn-oracledb-atp-app /delete`

> check to make sure - `echo a@b.com | fn call fn-oracledb-atp-app /read`
