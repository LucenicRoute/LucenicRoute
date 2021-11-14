# LucenicRoute

### How to Run 

1. Begin by cloning this repository
``` sh
 git clone https://github.com/LucenicRoute/LucenicRoute.git
````

2. Navigate to the location of the repository 
```sh
cd <path to repository>
```

3. Use the Maven package manager to create the necessary jar files
```sh
mvn clean package
```

4. Finally run the `.jar` file 
```sh
java -jar target/LucenicRoute-1.0-SNAPSHOT.jar
```

5. To run `trec_eval` 
```sh
~/trec_eval/trec_eval <qrel file> <results file> 
````
 
