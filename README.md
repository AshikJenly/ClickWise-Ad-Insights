# ClickStream Analysis in a Web Application

## Tools and Environment Used in Development and Deployment
### Cloud Service Used

<img src="https://github.com/AshikJenly/ClickStream-Analysis-in-a-Web-Application/assets/116492348/cd629f7b-37c9-4465-84af-d0d4e31f1e2a" alt="image" style="width:400px;height:400px;">

### Operating System
- Ubuntu 22.0 Azure Virtual Machine

### Programming Languages
- Scala
- Python
- JavaScript

### Data Processing Tools
- Azure DataBricks (Spark)

### Data Storage Tools
- Azure MySQL Flexible Server
- Azure Data Lake
- Azure CosmosDB (MongoDB)
- Apache Hadoop (POC)
- Apache Kafka (POC)

### Data Streaming Tools
- Azure EventHubs
- Azure DataBricks (Spark)

### Software Frameworks
- Play Framework (Scala)

### Visualization Tools
- Chart.js

## Spark Execution Mode : Cluster 
### Cluster Details:
```json
{
    "num_workers": 3,
    "cluster_name": "Ashik's Cluster - MultiNode",
    "spark_version": "14.2.x-scala2.12",
    "spark_conf": {},
    "azure_attributes": {
        "first_on_demand": 1,
        "availability": "ON_DEMAND_AZURE",
        "spot_bid_max_price": -1
    },
    "node_type_id": "Standard_DS3_v2",
    "driver_node_type_id": "Standard_DS3_v2",
    "ssh_public_keys": [],
    "custom_tags": {},
    "spark_env_vars": {},
    "autotermination_minutes": 120,
    "enable_elastic_disk": true,
    "init_scripts": [],
    "single_user_name": "<username>.onmicrosoft.com",
    "enable_local_disk_encryption": false,
    "data_security_mode": "SINGLE_USER",
    "runtime_engine": "PHOTON",
    "cluster_id": "<cluster-id>"
}
```
# Project Overview

# ARCHITECTURE

![alt text](.resources/image.png)

## BACKEND

The backend component of the project, located in the `BACKEND/clickstreambackend` directory, is organized into the following key components:

- **Controllers:** Handle incoming requests and define the application's behavior.
- **DAO (Data Access Objects):** Responsible for interacting with the database.
- **Models:** Define the structure of the data used in the application.
- **Services:** Implement business logic and data processing.
- **Views:** Define the presentation layer of the application.

### Project Structure
- **Configuration:**
  - `application.conf`: Application-specific configurations.
  - `logback.xml`: Logging configuration.
  - `routes`: Defines the application's URL routes.

- **Public Resources:**
  - Static files such as JavaScript (`getads.js`, `main.js`, `producedata.js`), stylesheets (`main.css`), and images.

- **Logs:**
  - `application.log`: Log files capturing application events.

- **Testing:**
  - Unit tests for controllers located in the `test/controllers` directory.

## STREAMING-ETL

The streaming ETL (Extract, Transform, Load) component, located in the `STREAMING-ETL` directory, consists of both a Proof of Concept (POC) and production-grade code for data processing.

### POC
- Scala (`backp.scala`, `DataLakeToCosmosDB.scala`, `EventHubToDataLakeETL.scala`) and Python (`main.py`) scripts for data processing.
- Shell script (`ex.sh`) for execution.

### PRODUCTION
- Two separate projects:
  - **DataLakeToCosmos:**
    - Scala project for processing and transferring data from Data Lake to CosmosDB.
  - **EventHubToDataLake:**
    - Scala project for processing and storing data from Event Hub to Data Lake.

## TESTING

### GenerateSampleData
- Located in the `TESTING/GenerateSampleData` directory.
- Python (`consumer.py`, `generate_to_event_hubs.py`) and Scala (`main.scala`) scripts for generating and consuming sample data.

## VISUALIZATION

### Live User Dashboard
- Located in the `VISUALIZATION/live_user_dashboard` directory.
- HTML (`index.html`), CSS (`loader.css`, `styles.css`), and JavaScript (`sample_data.js`, `script.js`) files for creating a live user dashboard.

