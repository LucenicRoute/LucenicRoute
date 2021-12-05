# LucenicRoute

### How to Run 

**1. Begin by cloning this repository**
``` sh
 git clone https://github.com/LucenicRoute/LucenicRoute.git
```

**2. Navigate to the location of the repository**
```sh
cd <path to repository>
```

**3. Compile, run and evaluate the project using the run script**
This will compile the project using maven, then run the queries and use `trec_eval` to display the MAP score.
```sh
./run.sh
```

### To index the document collection
The document collection will only be indexed if no index exists or if a flag is passed when running the queries. This is due to the indexing taking some time, ~5 minutes. If you wish to re-index the document collection, pass the `-ci` flag to the run script. Alternatively the index folder can be removed.
```sh
./run.sh -ci
```