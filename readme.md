# Starbucks Near Me Recommendation Program

## Overview
A scala-based program that recommends the top 3 Starbucks Near Me based on my driving data.

## Technologies
- sbt
- HDFS
- YARN
- Hadoop
- MapReduce
- Docker
- Google's Distance Matrix API

## Features
- Show the top 5 most appeared addresses in the GPSTracking.csv using a MapReduce program.
- Display U.S. Starbucks stores that are only located in the same city and state as appeared in the addresses above.
- Compute travel distances between 5 addresses selected above and each Starbucks store location selected above.
- Recommend top 3 Starbucks stores that are most accessible to me based on my driving data.

## Getting Started / Usage
In order to run this program properly, you will need to do the following prerequisites:<br/>
- Be sure to create Distance Matrix API key.
- Be sure to have a mapreduced GPSTracking file and a mapreduced StarbucksLocation file ready.

If all of the prerequisites above are met, go ahead and clone this repo by using the command below:
```
git clone https://github.com/spark131008/Starbucks_Near_Me_Recommendation_Program.git
```
In order to run this program, use the command below:
```
sbt run
```
Once the program calculates the distance and makes the top 3 recommendations, the output will be shown in the console.

## Contributors
spark131008