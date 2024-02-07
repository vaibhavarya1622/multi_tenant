# multi_tenant

## Initial Setup
Create a kafka topic with name db_operator. Here the data that is to be saved to the tenant database will come from kafka.

Then Create below three database:
1. db_operator
2. india_db
3. usa_db

Here db_operator will store the required configurations for tenant databases
And india_db and usa_db are the two tenant databases.

Now execute below scripts in the respective db.
1. db_operator.sql
2. india_db
3. usa_db

Now finally run the MultiApplicationTests.java.

It will store the data coming from kafka in the respective tenant database.
